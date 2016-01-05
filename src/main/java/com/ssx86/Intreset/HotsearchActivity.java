package com.ssx86.Intreset;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HotsearchActivity extends Activity {

    private ArrayList<HotsearchModel> modelArrayList;
    private HotsearchAdapter adapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotsearch);

        TextView textView = (TextView) findViewById(R.id.label);
        final ListView listView = (ListView) findViewById(R.id.listview);

        modelArrayList = new ArrayList<>();

        adapter = new HotsearchAdapter(this, modelArrayList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotsearchModel item = (HotsearchModel)adapter.getItem(position);
                String url = item.getUrl();
                WebViewActivity.url = url;
                Intent  intent = new  Intent(getApplicationContext(), WebViewActivity.class);
                startActivity(intent);
            }
        });

        new Thread() {
            @Override
            public void run() {
                modelArrayList.clear();
                String s = HTTPHelper.GetFromURL("http://top.baidu.com/clip?b=1&line=20");
                Log.i("BAIDU", s);
                Pattern pattern = Pattern.compile("BD_DATA=(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String data = matcher.group(1);
                    try {
                        JSONTokener parser = new JSONTokener(data);
                        JSONArray root = (JSONArray) parser.nextValue();
                        for (int i = 0; i < root.length(); i++) {
                            Log.i("BAIDU", root.get(i).toString());

                            JSONObject obj = (JSONObject)root.get(i);
                            String title = obj.getString("title");
                            String url = obj.getString("tit_url");
                            HotsearchModel model = new HotsearchModel(title, url, i);
                            modelArrayList.add(model);
                        }
                        handler.sendEmptyMessage(0);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();


        textView.setText(getString(R.string.hotsearch));

    }

}
