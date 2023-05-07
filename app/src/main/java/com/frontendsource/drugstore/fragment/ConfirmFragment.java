package com.frontendsource.drugstore.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.activity.BaseActivity;
import com.frontendsource.drugstore.activity.CartActivity;
import com.frontendsource.drugstore.activity.SuccessActivity;
import com.frontendsource.drugstore.adapter.CheckoutCartAdapter;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.model.Cart;
import com.frontendsource.drugstore.model.Order;
import com.frontendsource.drugstore.model.OrderItem;
import com.frontendsource.drugstore.model.OrdersResult;
import com.frontendsource.drugstore.model.PlaceOrder;
import com.frontendsource.drugstore.model.User;
import com.frontendsource.drugstore.util.FilePath;
import com.frontendsource.drugstore.util.FileUtils;
import com.frontendsource.drugstore.util.localstorage.LocalStorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;/**
 * Drug App
 * https://github.com/quintuslabs/GroceryStore
 * Created on 18-Feb-2019.
 * Created by : Santosh Kumar Dash:- http://santoshdash.epizy.com
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmFragment extends Fragment  {
    LocalStorage localStorage;
    List<Cart> cartList = new ArrayList<>();
    Gson gson;
    RecyclerView recyclerView;
    CheckoutCartAdapter adapter;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    TextView back, order;
    TextView total, shipping, totalAmount;
    Double _total, _shipping, _totalAmount;
    ProgressDialog progressDialog;
    List<Order> orderList = new ArrayList<>();
    List<OrderItem> orderItemList = new ArrayList<>();
    PlaceOrder confirmOrder;
    String orderNo, address;
    String id;
    OrderItem orderItem = new OrderItem();
    CardView uploadPrescription, arrangeCall,notRequired;
    String prescription_type="";
    User user;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, PICK_PDF_REQUEST = 3;

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_MULTIPLE_PERMISSION = 101;

    File prescription;

    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm, container, false);
        localStorage = new LocalStorage(getContext());
        recyclerView = view.findViewById(R.id.cart_rv);
        totalAmount = view.findViewById(R.id.total_amount);
        total = view.findViewById(R.id.total);
        shipping = view.findViewById(R.id.shipping_amount);
        back = view.findViewById(R.id.back);
        order = view.findViewById(R.id.place_order);
        uploadPrescription = view.findViewById(R.id.upload_card);
        arrangeCall = view.findViewById(R.id.arrange_call);
        notRequired = view.findViewById(R.id.not_required);

        progressDialog = new ProgressDialog(getContext());
        gson = new Gson();
        orderList = ((BaseActivity) getActivity()).getOrderList();
        orderList = ((BaseActivity) getActivity()).getOrderList();
        cartList = ((BaseActivity) getContext()).getCartList();
        user = gson.fromJson(localStorage.getUserLogin(), User.class);
        address = user.getAddress() + "," + user.getCity() + "," + user.getState() + "," + user.getZip();

        for (int i = 0; i < cartList.size(); i++) {

            orderItem = new OrderItem(cartList.get(i).getTitle(), cartList.get(i).getQuantity(), cartList.get(i).getAttribute(), cartList.get(i).getCurrency(), cartList.get(i).getImage(), cartList.get(i).getPrice(), cartList.get(i).getSubTotal());
            orderItemList.add(orderItem);
        }



        _total = ((BaseActivity) getActivity()).getTotalPrice();
        _shipping = 0.0;
        _totalAmount = _total + _shipping;
        total.setText(_total + "");
        shipping.setText(_shipping + "");
        totalAmount.setText(_totalAmount + "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CartActivity.class));
                getActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeUserOrder();
            }
        });


        uploadPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPrescription.setCardBackgroundColor(getContext().getResources().getColor(R.color.lineColor));
                notRequired.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                arrangeCall.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                prescription_type = "upload";

//               onUploadClicked();
            }
        });
        arrangeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrangeCall.setCardBackgroundColor(getContext().getResources().getColor(R.color.lineColor));
                notRequired.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                uploadPrescription.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                prescription_type = "arrange call";
            }
        });
        notRequired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notRequired.setCardBackgroundColor(getContext().getResources().getColor(R.color.lineColor));
                uploadPrescription.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                arrangeCall.setCardBackgroundColor(getContext().getResources().getColor(R.color.white));
                prescription_type = "Not Required";
            }
        });


        setUpCartRecyclerview();
        return view;
    }

    private void onUploadClicked() {
        final Dialog mDialog = new Dialog(getContext());
        mDialog.setCancelable(true);
        mDialog.setContentView(R.layout.choose_file_dialog);
        Window window = mDialog.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView gallery_image = mDialog.findViewById(R.id.gallery_photo);
        TextView take_photo = mDialog.findViewById(R.id.take_photo);
        TextView cancel = mDialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        gallery_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int camerapermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        CAMERA);
                int writePermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        WRITE_EXTERNAL_STORAGE);

                if (camerapermissionCheck != PackageManager.PERMISSION_GRANTED || writePermissionCheck != PackageManager.PERMISSION_GRANTED) {
                } else {
                    mDialog.dismiss();
                    String[] mimeTypes =
                            {"image/jpg", "image/png", "image/jpeg"};

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                        if (mimeTypes.length > 0) {
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                        }
                    } else {
                        String mimeTypesStr = "";
                        for (String mimeType : mimeTypes) {
                            mimeTypesStr += mimeType + "|";
                        }
                        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                    }
                    startActivityForResult(Intent.createChooser(intent, "ChooseFile"), SELECT_FILE);
                }
            }
        });
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int camerapermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        CAMERA);
                int writePermissionCheck = ContextCompat.checkSelfPermission(getContext(),
                        WRITE_EXTERNAL_STORAGE);

                if (camerapermissionCheck != PackageManager.PERMISSION_GRANTED || writePermissionCheck != PackageManager.PERMISSION_GRANTED) {
                } else {
                    mDialog.dismiss();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
            }
        });
        mDialog.show();
    }

    private void placeUserOrder() {
        if(prescription_type.length()==0){
            Toast.makeText(getContext(), "Select a prescription Option", Toast.LENGTH_LONG).show();
           return;
        }


        confirmOrder = new PlaceOrder(user.getToken(), user.getName(), user.getMobile(),user.getEmail(), user.getCity(),user.getState(),user.getZip(), address, orderItemList);
        progressDialog.setMessage("Confirming Order...");
        progressDialog.show();
        Log.d("Confirm Order==>", gson.toJson(confirmOrder));
        Call<OrdersResult> call = RestClient.getRestService(getContext()).confirmPlaceOrder(confirmOrder);
        call.enqueue(new Callback<OrdersResult>() {
            @Override
            public void onResponse(Call<OrdersResult> call, Response<OrdersResult> response) {
                Log.d("respose==>", response.body().getCode() + "");

                OrdersResult ordersResult = response.body();

                if (ordersResult!=null && ordersResult.getCode() == 200) {
                    localStorage.deleteCart();

                    if(prescription!=null){
                        uploadDocumentFile(prescription,ordersResult.getOrder_id());

                    }else{
                        showCustomDialog();
                        progressDialog.dismiss();
                    }

                }

            }

            @Override
            public void onFailure(Call<OrdersResult> call, Throwable t) {
                Log.d("Error respose==>", t.getMessage() + "");
                progressDialog.dismiss();
            }
        });


    }

    private void uploadDocumentFile(File prescription, String order_id) {
        progressDialog.setMessage("Confirming Order...");
        progressDialog.show();
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        prescription
                );
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part prescriptionFile =
                MultipartBody.Part.createFormData("prescription", prescription.getName(), requestFile);

        Call<OrdersResult>call = RestClient.getRestService(getContext()).uploadPrescription(order_id,prescriptionFile);
        call.enqueue(new Callback<OrdersResult>() {
            @Override
            public void onResponse(Call<OrdersResult> call, Response<OrdersResult> response) {
                Log.d("respose==>", response.body().getCode() + "");

                OrdersResult ordersResult = response.body();

                if (ordersResult.getCode() == 200) {
                    showCustomDialog();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<OrdersResult> call, Throwable t) {
                Log.d("Error respose==>", t.getMessage() + "");
                progressDialog.dismiss();
            }
        });

    }


    private void showCustomDialog() {

        startActivity(new Intent(getContext(), SuccessActivity.class));
        getActivity().finish();

      /*  // Create custom dialog object
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        // Include dialog.xml file
        dialog.setContentView(R.layout.success_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });
        // Set dialog title

        dialog.show();*/
    }

    private void setUpCartRecyclerview() {
        recyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        adapter = new CheckoutCartAdapter(cartList, getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Confirm");
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                Uri fileUri = data.getData();
                File file = new File(FileUtils.getPath(getContext(), fileUri));
                if (file != null) {
//                    uploadDocumentFile(file);
                    prescription = file;
                } else {
                    Toast.makeText(getContext(), "Please select image from another location", Toast.LENGTH_SHORT).show();
                }

            } else {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri uri = FilePath.getImageUri(getContext(), thumbnail);
                if (uri != null) {
                    File file = new File(FileUtils.getPath(getContext(), uri));
                    prescription = file;
//                    uploadDocumentFile(file);
                }
            }
        }
    }


}
