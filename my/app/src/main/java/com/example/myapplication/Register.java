package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Register extends AppCompatActivity {  //php 회원가입창
    private String userID;
    private String userPassword;
    private String userName;
    private AlertDialog dialog;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final EditText nameText = (EditText)findViewById(R.id.nameText);

        final Button validateButton = (Button)findViewById(R.id.validateButton);
        validateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userID = idText.getText().toString();
                if(validate)
                {
                    return;
                }
                if(userID.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("아이디는 빈 칸일 수 없습니다.")
                        .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                idText.setEnabled(false);
                                validate = true;
                                idText.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                validateButton.setBackgroundColor(getResources().getColor(R.color.colorGray));
                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("이미 존재 하는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(validateRequest);
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userName = nameText.getText().toString();

                if(!validate)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("먼저 충복 체크를 해주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                if(userID.equals("")||userPassword.equals("")||userName.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("빈 칸 없이 입력해주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        })
                                        .create();
                                dialog.show();


                            }
                            else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                dialog = builder.setMessage("회원 등록에 실패했습니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        }catch (Exception e){

                        }
                    }
                };
                RegisterRequest registerRequset = new RegisterRequest(userID, userPassword, userName, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(registerRequset);
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
/*
* public static final int sub = 1001;  // jsp를 이용햇던것
    private EditText login_id, login_pw,name;
    private Button id_button;
    private String sId, sPw,sNw;
* super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        id_button = (Button) findViewById(R.id.id_button);
        login_id = (EditText) findViewById(R.id.login_id);
        login_pw = (EditText) findViewById(R.id.login_pw);
        name = (EditText) findViewById(R.id.name);
        final Button button1 = (Button) findViewById(R.id.join_btn);
        Button button2 = (Button) findViewById(R.id.id_button);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sId = login_id.getText().toString();
                sPw = login_pw.getText().toString();

                if(sId.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                    builder.setMessage("아이디는 빈 칸일 수 없습니다.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }else{
                    final AsyncHttpClient client = new AsyncHttpClient();
                    Log.d("testres", "REQUEST WAITING");
                    sId = login_id.getText().toString();
                    sPw = login_pw.getText().toString();


                    final RequestParams params = new RequestParams();
                    params.put("userID", sId);
                    params.put("userPassword", sPw);
                    final String url = "http://172.30.1.38:8080/testing/getId.jsp";

                    client.post(url,params, new TextHttpResponseHandler(){

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            if(responseString.trim().equals("false")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                                builder.setMessage("중복체크 성공");
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                                login_id.setEnabled(false);
                                login_id.setBackgroundColor(getResources().getColor(R.color.colorGray));
                                id_button.setBackgroundColor(getResources().getColor(R.color.colorGray));

                                button1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View v){
                                        sPw = login_pw.getText().toString();
                                        sNw = name.getText().toString();
                                        if(sPw.equals("") || sNw.equals("")){
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                                            builder.setMessage("빈칸을 입력해주세요.");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                }
                                            });

                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                        else{
                                            AsyncHttpClient client = new AsyncHttpClient();
                                            Log.d("testres", "REQUEST WAITING");
                                            sId = login_id.getText().toString();
                                            sPw = login_pw.getText().toString();
                                            sNw = name.getText().toString();

                                            RequestParams params = new RequestParams();
                                            params.put("userID", sId);
                                            params.put("userPassword", sPw);
                                            params.put("userName",sNw);
                                            client.post("http://172.30.1.38:8080/testing/userjoinAction.jsp",params, new TextHttpResponseHandler() {
                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                                }

                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                                                    builder.setMessage("회원가입 성공");
                                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int which) {
                                                            Intent intent = new Intent(getApplicationContext(), Login.class);
                                                            startActivityForResult(intent,sub);

                                                        }


                                                    });
                                                    Log.d("testres", responseString);
                                                    AlertDialog alertDialog = builder.create();
                                                    alertDialog.show();


                                                }
                                            });
                                        }




                                    }
                                });



                            }else{

                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                                builder.setMessage("이미 존재하는 아이디입니다.");
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

            }
        });

        button1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                builder.setMessage("중복체크를 해주세요");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
* */
