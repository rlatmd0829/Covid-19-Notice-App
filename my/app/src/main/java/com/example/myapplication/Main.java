package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity {
    public static final int sub = 1001;
    private EditText login_id, login_pw;
    private String sId, sPw;
    private String userID;
    String userName;
    TextView textView;


    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new BackgroundTask().execute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);



        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");



        noticeListView = (ListView)findViewById(R.id.noticeListView);
        noticeList = new ArrayList<Notice>();
        noticeList.add(new Notice("  개발시작", "사거리 유지 어플", "2020-04-01"));
        noticeList.add(new Notice("  로그인, 회원가입 구현", "사거리 유지 어플", "2020-04-08"));
        noticeList.add(new Notice("  확진자 동선 파악", "사거리 유지 어플", "2020-04-15"));
        noticeList.add(new Notice("  코로나 2.5단계", "사거리 유지 어플", "2020-08-30"));
        noticeList.add(new Notice("  코로나 2단계로 완화", "사거리 유지 어플", "2020-09-27"));
        noticeList.add(new Notice("  코로나 1단계로 완화", "사거리 유지 어플", "2020-10-12"));
        adapter = new NoticeListAdapter(getApplicationContext(), noticeList);
        noticeListView.setAdapter(adapter);



    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute(){
            try {
                target = "https://rlatmd0829.cafe24.com/UserName.php?userID=" + URLEncoder.encode(Login.userID, "UTF-8");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");



                    JSONObject object = jsonArray.getJSONObject(0);
                    userName = object.getString("userName");



                textView = (TextView) findViewById(R.id.textName);
                textView.setText(String.format(Locale.KOREAN,"%s님 환영합니다.",userName));


            } catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    public void monclick(View v) {
        switch (v.getId()) {
            case R.id.mapButton:
                Intent intent1 = new Intent(getApplicationContext(), MapView.class);
                intent1.putExtra("userName",userName);
                startActivityForResult(intent1, sub);
                break;
            case R.id.homepageButton1:
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://uhs-covid-19.neocities.org"));
                startActivityForResult(intent2, sub);
                break;
            case R.id.homepageButton2:
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://uhs10.neocities.org"));
                startActivityForResult(intent3, sub);
                break;
        }


    }
}