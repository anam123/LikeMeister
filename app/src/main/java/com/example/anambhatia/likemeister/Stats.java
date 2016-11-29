package com.example.anambhatia.likemeister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;


public class Stats extends ListFragment {

    TextView likes;
    TextView posts;
    TextView a,b,c,y,z,x,w;
    ProgressBar d;
    String access;
    MenuItem nav_item2;
    MenuItem nav_item1;
    MenuItem nav_item3;

    //the location of the server/database
    public static final String URL = ("http://likemeister-145918.appspot.com/Predictor");
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        a=(TextView)view.findViewById(R.id.textView9);
        b=(TextView)view.findViewById(R.id.textView11);
        c=(TextView)view.findViewById(R.id.textView30);
        //  view.findViewById(R.id.textView11).setVisibility(View.VISIBLE);
        d=(ProgressBar)view.findViewById(R.id.loadingPanel);
         posts = (TextView) view.findViewById(R.id.textView22);
        likes = (TextView) view.findViewById(R.id.textView13);
        y = (TextView) view.findViewById(R.id.textView24);
        z = (TextView) view.findViewById(R.id.textView25);
        x = (TextView) view.findViewById(R.id.textView26);
        w = (TextView) view.findViewById(R.id.textView29);
        //change font
        final Typeface typeFace1=Typeface.createFromAsset(getActivity().getAssets(),"YanoneKaffeesatz-Regular.otf");
        a.setTypeface(typeFace1);
        b.setTypeface(typeFace1);
        c.setTypeface(typeFace1);
        x.setTypeface(typeFace1);
        y.setTypeface(typeFace1);
        z.setTypeface(typeFace1);
        w.setTypeface(typeFace1);
        posts.setTypeface(typeFace1);
        likes.setTypeface(typeFace1);


        System.out.println("a:"+access);
        //use the access token and pass it as an input to the async task.
        new Stats.ServletPostAsyncTask().execute(new Pair<Context, String>(getContext(), access));


    }

    public static class CheckNetwork {


        private static final String TAG = CheckNetwork.class.getSimpleName();



        public static boolean isInternetAvailable(Context context)
        {
            NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

            if (info == null)
            {
                Log.d(TAG,"no internet connection");
                return false;
            }
            else
            {
                if(info.isConnected())
                {
                    Log.d(TAG," internet connection available...");
                    return true;
                }
                else
                {
                    Log.d(TAG," internet connection");
                    return true;
                }

            }
        }
    }


    private class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private Context context;

        @Override
        protected void onPreExecute() {

            //disable all the menu bar items while data being loaded.
            NavigationView navigationView= (NavigationView) getActivity().findViewById(R.id.nav_view);
            Menu menuNav=navigationView.getMenu();
            nav_item2 = menuNav.findItem(R.id.menuItem2);
            nav_item2.setEnabled(false);
            nav_item3 = menuNav.findItem(R.id.menuItem3);
            nav_item3.setEnabled(false);
            nav_item1 = menuNav.findItem(R.id.menuItem1);
            nav_item1.setEnabled(false);

        }

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            context = params[0].first;
            String name = params[0].second;

            if(CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
            {

                try {

                    // drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    //getActivity().getActionBar().hide
                    // Set up the request
                    java.net.URL url = new URL("http://likemeister-145918.appspot.com/Predictor");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoInput(true);
                    connection.setDoOutput(true);

                    // Build name data request params
                    Map<String, String> nameValuePairs = new HashMap<>();
                    nameValuePairs.put("name", name);
                    String postParams = buildPostDataString(nameValuePairs);

                    // Execute HTTP Post
                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    writer.write(postParams);
                    writer.flush();
                    writer.close();
                    outputStream.close();
                    connection.connect();

                    // Read response
                    int responseCode = connection.getResponseCode();
                    StringBuilder response = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {
                        return response.toString();
                    }
                    return "Error: " + responseCode + " " + connection.getResponseMessage();
                } catch (IOException e) {
                    return "A";
                } catch (Exception e) {
                    Log.d("d", e.getMessage());
                    return "A";
                }
            }

            else
            {

                return "A";

            }

        }


        private String buildPostDataString(Map<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first) {
                    first = false;
                } else {
                    result.append("&");
                }

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {

            if (result.equals("A")) {

                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                d.setVisibility(View.GONE);
                nav_item2.setEnabled(true);
                nav_item1.setEnabled(true);
                nav_item3.setEnabled(true);

            } else {
                String[] output = result.split("___anambhatia___");
                //Extract data from the server. Convert the json format into suitable format.
                for (int i = 0; i < output.length; i++) {
                    System.out.println(output.length);
                    System.out.println(output[i]);

                }
                try {
                    JSONArray jsonArray = new JSONArray(output[0]);
                    List<String> list = new ArrayList<String>();

                    for (int j = 0; j < jsonArray.length(); j++) {
                        list.add(jsonArray.getString(j));
                    }

                    JSONArray jsonArray1 = new JSONArray(output[1]);
                    List<Integer> list1 = new ArrayList<Integer>();

                    for (int j = 0; j < jsonArray1.length(); j++) {
                        list1.add(jsonArray1.getInt(j));
                    }

                    JSONArray jsonArray2 = new JSONArray(output[2]);
                    List<String> list2 = new ArrayList<String>();

                    for (int j = 0; j < jsonArray2.length(); j++) {

                        list2.add(jsonArray2.getString(j));
                    }

                    JSONArray jsonArray3 = new JSONArray(output[3]);
                    final List<Integer> list3 = new ArrayList<Integer>();

                    for (int j = 0; j < jsonArray3.length(); j++) {
                        list3.add(jsonArray3.getInt(j));
                    }

                    //Display retreived data in textviews/listviews etc.

                    likes.setText(output[4].toString());
                    posts.setText(output[6].toString());
                    x.setText(output[7].toString());
                    w.setText(output[5].toString());
                    ListView lv = getListView();
                    //ListAdapter adapter = new SimpleAdapter(Stats.this, list2, R.layout.listing, new String[]{"name", "rating", "review"}, new int[]{R.id.likes, R.id.comments, R.id.status});

                    //using a custom list for a better ui.
                    CustomListAdapter adapter = new CustomListAdapter(

                            getContext(),
                            R.layout.custom_list,
                            list2);
                    lv.setAdapter(adapter);
                    a.setVisibility(View.VISIBLE);
                    b.setVisibility(View.VISIBLE);
                    //  view.findViewById(R.id.textView11).setVisibility(View.VISIBLE);
                    c.setVisibility(View.VISIBLE);
                    x.setVisibility(View.VISIBLE);
                    y.setVisibility(View.VISIBLE);
                    z.setVisibility(View.VISIBLE);
                    w.setVisibility(View.VISIBLE);

                    //enabling menu bar items after background tasks completed.
                    nav_item2.setEnabled(true);
                    nav_item1.setEnabled(true);
                    nav_item3.setEnabled(true);
                    d.setVisibility(View.GONE);

                    //Setting an onclicklistener on our list
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
//

                            LinearLayout ll = (LinearLayout) view; // get the parent layout view
                            final TextView tv = (TextView) ll.findViewById(R.id.textView);
                            final String original = tv.getText().toString();
                            tv.setBackgroundColor(Color.DKGRAY);
                            //change text on the list item for 2000 milliseconds
                            tv.setText("Likes On Post: " + list3.get(position).toString());
                            tv.postDelayed(new Runnable() {
                                public void run() {
                                    tv.setText(original);
                                    tv.setBackgroundColor(Color.parseColor("#0084ff"));

                                }
                            }, 2000);
                            //Snackbar.make(view, "Likes On Post: "+ list3.get(position).toString(), Snackbar.LENGTH_LONG)
                            // .setAction("Action", null).show();
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    d.setVisibility(View.GONE);
                    nav_item2.setEnabled(true);
                    nav_item1.setEnabled(true);
                    nav_item3.setEnabled(true);

                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        access = getArguments().getString("token");
        View v = inflater.inflate(R.layout.stats, container, false);
        return v;
    }
}