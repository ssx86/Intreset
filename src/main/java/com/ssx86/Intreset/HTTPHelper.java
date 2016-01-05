package com.ssx86.Intreset;


import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by new2 on 2016/1/5.
 */
public class HTTPHelper {
    public static final String GetFromURL(String urlpath) {
        HttpURLConnection conn = null;
        String resultData = new String();
        try {
            URL url = new URL(urlpath);

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
            String inputLine ;

            while ((inputLine = bufferReader.readLine()) != null) {
                resultData += inputLine + "\n";
            }
            Log.i("HTTPHelper", resultData);




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            conn.disconnect();
        }

        return resultData;
    }
}
