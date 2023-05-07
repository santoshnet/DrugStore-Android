package com.frontendsource.drugstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.api.clients.RestClient;
import com.frontendsource.drugstore.helper.PinEntryEditText;
import com.frontendsource.drugstore.helper.PinEntryView.PinEntryView;
import com.frontendsource.drugstore.model.User;
import com.frontendsource.drugstore.model.UserResult;
import com.frontendsource.drugstore.util.localstorage.LocalStorage;
import com.google.gson.Gson;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends BaseActivity {
    private PinEntryView pinEntryView;
    String otp;
    CountDownTimer cTimer = null;
    LinearLayout resendLayout,timerLayout;
    TextView timer,emailText;
    View progress;
    User user;
    Gson gson = new Gson();
    LocalStorage localStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        final PinEntryEditText pinEntry = findViewById(R.id.txt_pin_entry);
         resendLayout = findViewById(R.id.resend_otp_ll);
         timerLayout = findViewById(R.id.timer_ll);
         timer = findViewById(R.id.timer);
         emailText = findViewById(R.id.email_verify_text);
         progress = findViewById(R.id.progress_bar);
         localStorage = new LocalStorage(getApplicationContext());
         String userString = localStorage.getUserLogin();
         user = gson.fromJson(userString,User.class);

         if(user!=null){
             emailText.setText("Please verify your email using OTP send to your register Mobile Number   "+user.getMobile());
         }

        if (pinEntry != null) {
            pinEntry.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
                @Override
                public void onPinEntered(CharSequence str) {
                    otp = str.toString();
                }
            });
        }

    }

    public void onResendClicked(View view) {
        timerLayout.setVisibility(View.VISIBLE);
        resendLayout.setVisibility(View.GONE);

        startTimer();
        resendOTP();

    }

    private void resendOTP() {
        Call<UserResult>call = RestClient.getRestService(getApplicationContext()).resendVerification(user);
        call.enqueue(new Callback<UserResult>() {
            @Override
            public void onResponse(Call<UserResult> call, Response<UserResult> response) {

                if(response!=null){
//                    Log.d(TAG, "onResponse: Success");
                  UserResult userResult = response.body();
                  if(userResult!=null && userResult.getStatus()==200){
                      Toast.makeText(OTPActivity.this, "OTP Send to your register Phone Number Please check", Toast.LENGTH_LONG).show();

                  }else{
                      Toast.makeText(OTPActivity.this, userResult.getMessage(), Toast.LENGTH_LONG).show();

                  }


                }else{
                    Toast.makeText(OTPActivity.this, "Server not Responding. Please try after some time.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserResult> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
                Toast.makeText(OTPActivity.this, "Server not Responding. Please try after some time.", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(millisUntilFinished/1000+"");
            }
            public void onFinish() {
                timerLayout.setVisibility(View.GONE);
                resendLayout.setVisibility(View.VISIBLE);
            }
        };
        cTimer.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cancelTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelTimer();
    }

    private void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    public void onVerifyOTPClicked(View view) {
        progress.setVisibility(View.VISIBLE);
        if(otp.length()<6){
            Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
        }else{
            user.setOtp(otp);
            Call<UserResult>call = RestClient.getRestService(getApplicationContext()).userActivate(user);
            call.enqueue(new Callback<UserResult>() {
                @Override
                public void onResponse(Call<UserResult> call, Response<UserResult> response) {
                    if(response!=null){
//                        Log.d(TAG, "onResponse: Success");
                        UserResult userResult = response.body();
                        if(userResult!=null && userResult.getStatus()==200){
                            user.setVerified("1");
                            Gson gson = new Gson();
                            String userString = gson.toJson(user);
                            localStorage.createUserLoginSession(userString);
                            Toast.makeText(OTPActivity.this, "OTP Verified successfully.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }else{
                            Toast.makeText(OTPActivity.this, userResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(OTPActivity.this, "Please enter valid OTP.", Toast.LENGTH_SHORT).show();
                    }
                    progress.setVisibility(View.GONE);

                }

                @Override
                public void onFailure(Call<UserResult> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText(OTPActivity.this, "Server not Responding. Please try after some time.", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });
        }
    }
}