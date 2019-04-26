package com.pd2.pd2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GithubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.github);

        showRepositories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_maps){
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showRepositories() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url ="https://api.github.com/users/elluksm/repos";

        // Request a string response from the provided URL.
        JsonArrayRequest jsonArrayRequest  = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        showList(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GithubActivity.this, "Mēģini vēlreiz! Kaut kas nogāja greizi.." + error, Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonArrayRequest);

        return;
    }


    public void showList(JSONArray data) {

        String[] nameArray = {};
        String[] infoArray = {};
        Integer image = R.drawable.folder;

        try{
            // Loop through the array elements
            for(int i=0;i<data.length();i++){
                // Get current json object
                JSONObject rep = data.getJSONObject(i);

                String name = rep.getString("name");
                nameArray  = arrayStringPush(name, nameArray);

                JSONObject owner = rep.getJSONObject("owner");
               String ownerName = owner.getString("login");
                infoArray = arrayStringPush(ownerName, infoArray);
            }

            ListView listView;

            CustomListAdapter whatever = new CustomListAdapter(this, nameArray, infoArray, image);
            listView = (ListView) findViewById(R.id.listviewID);
            listView.setAdapter(whatever);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return;
    }

    public static String[] arrayStringPush(String item, String[] oldArray) {
        int len = oldArray.length;
        String[] newArray = new String[len+1];
        System.arraycopy(oldArray, 0, newArray, 0, len);
        newArray[len] = item;
        return newArray;
    }
}
