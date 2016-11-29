package com.example.anambhatia.likemeister;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by AnamBhatia on 20/10/16.
 */

public class Friend extends ListFragment {

    public static final String URL = ("http://likemeister-145918.appspot.com/Predictor");
    String access;
    ProgressBar d;
    MenuItem nav_item2;
    MenuItem nav_item1;
    MenuItem nav_item3;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        d=(ProgressBar)view.findViewById(R.id.loadingPanel1);


        //use the access token and pass it as an input to the async task.
        new Friend.ServletPostAsyncTask().execute(new Pair<Context, String>(getContext(), access));

    }

    public static class CheckNetwork {


        private static final String TAG = Stats.CheckNetwork.class.getSimpleName();



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
            //disable all menu bar items when background tasks happen
            NavigationView navigationView= (NavigationView) getActivity().findViewById(R.id.nav_view);
            Menu menuNav=navigationView.getMenu();
            nav_item2 = menuNav.findItem(R.id.menuItem3);
            nav_item2.setEnabled(false);
            nav_item3 = menuNav.findItem(R.id.menuItem2);
            nav_item3.setEnabled(false);
            nav_item1 = menuNav.findItem(R.id.menuItem1);
            nav_item1.setEnabled(false);

        }

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            context = params[0].first;
            String name = params[0].second;


            if(Stats.CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
            {


                try {
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
                    return e.getMessage();
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
            //extract data and convert json into suitable format.
            if (result.equals("A")) {

                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                d.setVisibility(View.GONE);
                nav_item2.setEnabled(true);
                nav_item1.setEnabled(true);
                nav_item3.setEnabled(true);

            }
            else {
                String[] output = result.split("___anambhatia___");
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
//
                    JSONArray jsonArray1 = new JSONArray(output[1]);
                    List<Integer> list1 = new ArrayList<Integer>();

                    for (int j = 0; j < jsonArray1.length(); j++) {
                        list1.add(jsonArray1.getInt(j));
                    }

                    List<String> list2 = new ArrayList<String>();
                    for (int j = 0; j < list.size(); j++) {
                        list2.add(list.get(j) + " : " + list1.get(j).toString() + " likes");
                    }

//
//                JSONArray jsonArray2 = new JSONArray(output[2]);
//                List<String> list2 = new ArrayList<String>();
//
//                for (int j = 0; j < jsonArray2.length(); j++) {
//                    list2.add(jsonArray2.getString(j));
//                }


                    ListView lv = getListView();
                    //ListAdapter adapter = new SimpleAdapter(Stats.this, list2, R.layout.listing, new String[]{"name", "rating", "review"}, new int[]{R.id.likes, R.id.comments, R.id.status});
                    //using a custom list for a better ui.
                    CustomListAdapter adapter = new CustomListAdapter(
                            getContext(),
                            R.layout.custom_list,
                            list2);
                    lv.setAdapter(adapter);
                    nav_item2.setEnabled(true);
                    nav_item1.setEnabled(true);
                    nav_item3.setEnabled(true);
                    d.setVisibility(View.GONE);


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

        //extract access token from bundle
        access = getArguments().getString("token");
        View v = inflater.inflate(R.layout.friend, container, false);
        return v;
    }
}
