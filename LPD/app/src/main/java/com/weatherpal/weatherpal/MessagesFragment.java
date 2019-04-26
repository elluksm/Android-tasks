package com.weatherpal.weatherpal;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MessagesFragment extends Fragment {


    public MessagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
         showListItems();

        return rootView;
    }

    public void showListItems() {

        String[] nameArray = {"fghhh", "gjojf"};
        String[] infoArray = {"fghhh", "gjojf"};
        Integer image = R.drawable.rain;

        /*try{
            // Loop through the array elements
             for(int i=0;i<data.length();i++){
                // Get current json object
                JSONObject rep = data.getJSONObject(i);

                String name = rep.getString("name");
                nameArray  = arrayStringPush(name, nameArray);

                JSONObject owner = rep.getJSONObject("owner");
                String ownerName = owner.getString("login");
                infoArray = arrayStringPush(ownerName, infoArray);
            } */

            // ListView listView;

            CustomListAdapter whatever = new CustomListAdapter(getActivity(), nameArray, infoArray, image);
       //  ListView listView = (ListView) getView().findViewById(R.id.listviewID);
            // listView = (ListView) getView().findViewById(R.id.listviewID);
            // listView.setAdapter(whatever);
        /* } catch (JSONException e){
            e.printStackTrace();
        };*/

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
