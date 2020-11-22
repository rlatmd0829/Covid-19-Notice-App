package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpRequest;
import cz.msebera.android.httpclient.HttpResponse;

public class Login extends AppCompatActivity {

    private AlertDialog dialog;
    String userName;
    public static String userID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        LottieAnimationView lottie = (LottieAnimationView) findViewById(R.id.animation_view);
        lottie.playAnimation();
        lottie.loop(true);



        Toast.makeText(this.getApplicationContext(),userName, Toast.LENGTH_SHORT);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registerIntent);
            }
        });
        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginButton = (Button)findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();

                Response.Listener<String> reponseLister = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                dialog = builder.setMessage("로그인에 성공했습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Login.this, Main.class);
                                                intent.putExtra("userID",userID);
                                                Login.this.startActivity(intent);
                                            }
                                        })
                                        .create();
                                dialog.show();


                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                dialog = builder.setMessage("계정을 다시 확인하세요.")
                                        .setPositiveButton("다시 시도", null)
                                        .create();
                                dialog.show();
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest(userID, userPassword, reponseLister);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
            }
        });



        }


        @Override
    protected void onStop(){
        super.onStop();
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
        }

    }
    /* php 전에것
     public static final int sub = 1001;
    private EditText login_id, login_pw;
    private String sId, sPw;

    * login_id = (EditText) findViewById(R.id.login_id);
        login_pw = (EditText) findViewById(R.id.login_pw);

        Button button1 = (Button) findViewById(R.id.login_btn);

        button1.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){

                final AsyncHttpClient client = new AsyncHttpClient();
                Log.d("testres", "REQUEST WAITING");
                sId = login_id.getText().toString();
                sPw = login_pw.getText().toString();

                final RequestParams params = new RequestParams();
                params.put("userID", sId);
                params.put("userPassword", sPw);
                final String url = "http://172.30.1.38:8080/testing/getData.jsp";

                client.post(url,params, new TextHttpResponseHandler() {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        Log.d("testres", responseString);
                        if(responseString.trim().equals("true")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                            builder.setMessage("로그인 성공");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                    final AsyncHttpClient client = new AsyncHttpClient();
                                    Log.d("testres", "REQUEST WAITING");
                                    sId = login_id.getText().toString();

                                    final RequestParams params = new RequestParams();
                                    params.put("userID", sId);
                                    final String url = "http://172.30.1.38:8080/testing/getName.jsp";

                                    client.post(url,params, new TextHttpResponseHandler(){

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                        }

                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                            Intent intent = new Intent(getApplicationContext(), Main.class);
                                            intent.putExtra("num1",responseString.trim());
                                            startActivityForResult(intent, sub);
                                        }
                                    });



                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();



                        }else{

                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);

                            builder.setMessage("ID와 PASS를 확인해주세요.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int which) {

                                }
                            });

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();


                        }

                    }
                });

            }
        });
    * */




