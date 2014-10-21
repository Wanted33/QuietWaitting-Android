package com.example.quietwaiting_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListActivity extends Activity {
	String[] services;
	Intent intent_for_info_services;
    private JSONParser jsonParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        new Thread(new Runnable() {
            @Override
            public void run() {
                services = getServices();
            }
        }).start();

    }
    
    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
            List<String> objects) {
          super(context, textViewResourceId, objects);
          for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
          }
        }

        @Override
        public long getItemId(int position) {
          String item = getItem(position);
          return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
          return true;
        }

    }

    protected String[] getServices() {
        jsonParser = new JSONParser();


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        //params.add(new BasicNameValuePair("tag", "1"));
        String url_link = "http://10.16.160.224/api/services/all.json";
        //String url_link = "http://graph.facebook.com/bgolub";
        String method = "GET";
        JSONArray json = jsonParser.getJSONFromUrl(url_link, method, params);
        String[] allServices = new String[json.length()];

        //Try to get Json Result
        try {
            if (json.length() > 0) {

                for (int i=0;i< json.length(); i++)
                {
                    JSONObject service = json.getJSONObject(i);
                    allServices[i] = String.valueOf(service.getString("name"));
                    Log.d("Nom :", String.valueOf(service.getString("name")));
                }
                services = allServices;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final ListView listView  = (ListView) findViewById(R.id.list_services);

                        final ArrayList<String> list = new ArrayList<String>();
                        if(services==null)
                        {
                            services[0] = "Aucun service";
                        }else {
                            for (int i = 0; i < services.length; ++i) {
                                list.add(services[i]);
                            }
                        }
                        final StableArrayAdapter adapter = new StableArrayAdapter(getApplicationContext(),
                                android.R.layout.simple_list_item_1, list);
                        listView.setAdapter(adapter);
                        intent_for_info_services = new Intent(getApplicationContext(),InfoServicesActivity.class);
                        listView.setOnItemClickListener(new OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {
                                String name_service = services[(int) id];
                                intent_for_info_services.putExtra("name_service",services[(int) id]);
                                intent_for_info_services.putExtra("id_service",(int)id);
                                startActivity(intent_for_info_services);

                            }
                        });

                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allServices;
    }
}
