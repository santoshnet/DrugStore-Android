package com.frontendsource.drugstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.adapter.OrderAdapter;
import com.frontendsource.drugstore.adapter.PrescriptionAdapter;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.model.Prescription;
import com.frontendsource.drugstore.model.PrescriptionResult;
import com.frontendsource.drugstore.model.Token;
import com.frontendsource.drugstore.model.User;
import com.frontendsource.drugstore.util.FilePath;
import com.frontendsource.drugstore.util.FileUtils;
import com.frontendsource.drugstore.util.localstorage.LocalStorage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrescriptionActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {

    private static final String TAG = "PrescriptionActivity";
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, PICK_PDF_REQUEST = 3;
    File prescription;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 101;
    String[] category = { "Select Report Type","Medical Reports", "Blood Reports", "Scan Reports", "Medicine Prescriptions"};
    ImageView imageView;
    LocalStorage localStorage;
    Gson gson = new Gson();
    User user;
    String _title="";
    String _remark;
    TextView remarkTV;
    Button upload_btn;
    List<Prescription>prescriptionList = new ArrayList<>();
    RecyclerView recyclerView;
    PrescriptionAdapter mAdapter;
    FrameLayout upload_frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        checkMultiplePermissions();
         imageView = (ImageView) findViewById(R.id.image_preview);
         remarkTV =  findViewById(R.id.prescription_remark);
         upload_btn =  findViewById(R.id.upload_btn);
         recyclerView =  findViewById(R.id.recycler_view);
         upload_frame =  findViewById(R.id.upload_frame);
         localStorage = new LocalStorage(getApplicationContext());
         String userString = localStorage.getUserLogin();
         user = gson.fromJson(userString,User.class);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Prescription");


        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,category);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);


        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUploadPerscription();
            }
        });


        getPrescriptionData();


    }

    private void getPrescriptionData() {
        Token token = new Token(user.getToken());
     Call<PrescriptionResult>call = RestClient.getRestService(getApplicationContext()).getPrescription(token);
     call.enqueue(new Callback<PrescriptionResult>() {
         @Override
         public void onResponse(Call<PrescriptionResult> call, Response<PrescriptionResult> response) {
             if(response!=null){
                 PrescriptionResult prescriptionResult = response.body();
                 prescriptionList = prescriptionResult.getPrescriptions();
                 setupRecyclerview();

             }
         }

         @Override
         public void onFailure(Call<PrescriptionResult> call, Throwable t) {

         }
     });
    }

    private void setupRecyclerview() {
        mAdapter = new PrescriptionAdapter(prescriptionList, PrescriptionActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if(position>0){
            _title = category[position];
        }
        Toast.makeText(getApplicationContext(),category[position] , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onSelectImage(View view) {
        if(ContextCompat.checkSelfPermission(PrescriptionActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(PrescriptionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            checkMultiplePermissions();
        }else{
            TedImagePicker.with(PrescriptionActivity.this)
                    .start(new OnSelectedListener() {
                        @Override
                        public void onSelected(@NotNull Uri uri) {
                            Log.d(TAG, "onSelected: "+uri);
                            File file = new File(FileUtils.getPath(getApplicationContext(), uri));
                            if (file != null) {
                                prescription = file;
                            } else {
                                Toast.makeText(getApplicationContext(), "Please select image from another location", Toast.LENGTH_SHORT).show();
                            }

                            Picasso.get().load(uri).into(imageView);
                        }
                    });
        }
    }


    private void checkMultiplePermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> permissionsNeeded = new ArrayList<String>();
            List<String> permissionsList = new ArrayList<String>();

            if (!addPermission(permissionsList, Manifest.permission.CAMERA)) {
                permissionsNeeded.add("CAMERA");
            }

            if (!addPermission(permissionsList, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                permissionsNeeded.add("Read Storage");
            }

            if (permissionsList.size() > 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                return;
            }
        }
    }



    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23)

            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);

                // Check for Rationale Option
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<String, Integer>();
                // Initial
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                    return;
                } else {
                    // Permission Denied
                    if (Build.VERSION.SDK_INT >= 23) {
                        Toast.makeText(
                                getApplicationContext(),
                                "My App cannot run without Camera and Storage " +
                                        "Permissions.\nRelaunch My App or allow permissions" +
                                        " in Applications Settings",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void uploadDocumentFile(File file) {
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        prescription
                );
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part prescriptionFile =
                MultipartBody.Part.createFormData("prescription", prescription.getName(), requestFile);

    }

    public void onUploadPerscription() {
        _remark = remarkTV.getText().toString();
        if(_title.length()==0){
            Toast.makeText(this, "Please select title", Toast.LENGTH_SHORT).show();
        }else if(prescription==null){
            Toast.makeText(this, "Please select Prescription/Report", Toast.LENGTH_SHORT).show();
        }else{
            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("*/*"),
                            prescription
                    );
            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part prescriptionFile =
                    MultipartBody.Part.createFormData("image", prescription.getName(), requestFile);
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), user.getName());
            RequestBody email = null;
            if(user.getEmail()!=null){
                 email = RequestBody.create(MediaType.parse("text/plain"), user.getEmail());
            }else{
                 email = RequestBody.create(MediaType.parse("text/plain"), "");
            }
            RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"), user.getMobile());
            RequestBody token = RequestBody.create(MediaType.parse("text/plain"), user.getToken());
            RequestBody title = RequestBody.create(MediaType.parse("text/plain"), _title);
            RequestBody remark = RequestBody.create(MediaType.parse("text/plain"), _remark);

            Call<Prescription>call = RestClient.getRestService(getApplicationContext()).uploadPrescription(name,email,mobile,remark,title,prescriptionFile,token);
            call.enqueue(new Callback<Prescription>() {
                @Override
                public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                    if(response!=null){
                        Prescription prescription = response.body();
                        if(prescription.getStatus()==200){
                            Toast.makeText(getApplicationContext(),prescription.getMessage(),Toast.LENGTH_LONG).show();

                            finish();
                            startActivity(getIntent());
                        }else{
                            Toast.makeText(getApplicationContext(),prescription.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                }

                @Override
                public void onFailure(Call<Prescription> call, Throwable t) {
                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                }
            });



        }
    }

    public void onAddPrescription(View view) {
        upload_frame.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void onCancelClicked(View view) {
        upload_frame.setVisibility(View.GONE);

    }
}