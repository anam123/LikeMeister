package com.example.anambhatia.likemeister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import javax.net.ssl.HttpsURLConnection;


public class Stats extends ListFragment {

    TextView likes;
    TextView posts;
    TextView a,b,c;
    ProgressBar d;
    String access;
    public static final String URL = ("http://likemeister-145918.appspot.com/Predictor");
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        a=(TextView)view.findViewById(R.id.textView6);
        b=(TextView)view.findViewById(R.id.textView9);
        //  view.findViewById(R.id.textView11).setVisibility(View.VISIBLE);
        c=(TextView)view.findViewById(R.id.textView13);
        d=(ProgressBar)view.findViewById(R.id.loadingPanel);
         posts = (TextView) view.findViewById(R.id.textView8);
        likes = (TextView) view.findViewById(R.id.textView11);
        System.out.println("a:"+access);
        new Stats.ServletPostAsyncTask().execute(new Pair<Context, String>(getContext(), access));


    }

    private class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private Context context;

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            context = params[0].first;
            String name = params[0].second;

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
            String[] output= result.split("___");
            for(int i=0;i<output.length;i++)
            {
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

                likes.setText(output[4].toString());
                posts.setText(output[6].toString());
                ListView lv = getListView();
                //ListAdapter adapter = new SimpleAdapter(Stats.this, list2, R.layout.listing, new String[]{"name", "rating", "review"}, new int[]{R.id.likes, R.id.comments, R.id.status});
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        getContext(),
                        android.R.layout.simple_list_item_1,
                        list2 );
                lv.setAdapter(adapter);
                a.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                //  view.findViewById(R.id.textView11).setVisibility(View.VISIBLE);
                c.setVisibility(View.VISIBLE);
                d.setVisibility(View.GONE);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
//

                        Snackbar.make(view, "Likes On Post: "+ list3.get(position).toString(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
            });

            } catch (JSONException e) {
                e.printStackTrace();
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