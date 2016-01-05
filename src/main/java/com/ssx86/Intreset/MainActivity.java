package com.ssx86.Intreset;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    private final String TAG = "AIRQUA";
    TextView pm25Text;
    TextView airquaText;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            pm25Text.setText(data.getString("pm2.5"));
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm25Text = (TextView) findViewById(R.id.pm25_value);

        findViewById(R.id.renew).setOnClickListener(new onRequestAirqua());
        findViewById(R.id.hotsearch).setOnClickListener(new onRequestHotsearch());

        doRequestAirqua();
    }

    private void doRequestAirqua() {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://zx.bjmemc.com.cn/web/Service.ashx?");

                    conn = (HttpURLConnection) url.openConnection();

                    conn.setConnectTimeout(15000);
                    conn.setRequestMethod("GET");

                    conn.setDoInput(true); //允许输入流，即允许下载
                    conn.setDoOutput(true); //允许输出流，即允许上传
                    conn.setUseCaches(false); //不使用缓冲
                    conn.setRequestMethod("GET"); //使用get请求
                    conn.connect();
                    InputStream is = conn.getInputStream();   //获取输入流，此时才真正建立链接
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader bufferReader = new BufferedReader(isr);
                    String inputLine = "";
                    String resultData = new String();
                    while ((inputLine = bufferReader.readLine()) != null) {
                        resultData += inputLine + "\n";
                    }
                    Log.i(TAG, resultData);

                    JSONTokener parser = new JSONTokener(resultData);
                    JSONObject root = (JSONObject) parser.nextValue();
                    JSONArray table = root.getJSONArray("Table");

                    String pm25 = "0";
                    for (int i = 0; i < table.length(); i++) {
                        JSONObject obj = (JSONObject) table.get(i);
                        Log.i(TAG, obj.toString());
                        String iaqi = obj.getString("IAQI");
                        String pollutant = obj.getString("Pollutant");
                        if (pollutant.equals("PM2.5")) {
                            pm25 = iaqi;
                        }
                    }
                    Bundle data = new Bundle();
                    data.putString("pm2.5", pm25);
                    Message msg = new Message();
                    msg.setData(data);
                    handler.sendMessage(msg);


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finally {
                    conn.disconnect();
                }
            }
        }.start();
    }

    private class onRequestAirqua implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            doRequestAirqua();
        }

    }

    private class onRequestHotsearch implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), HotsearchActivity.class);
            startActivity(intent);
        }
    }
}
