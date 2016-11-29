package com.example.anambhatia.likemeister;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends Activity {

    public static final String URL = ("http://likemeister-145918.appspot.com/Predictor");

    TextView outputText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String access=intent.getStringExtra("number");
        System.out.println("a:"+access);
        outputText=(TextView)findViewById(R.id.textView);
        new ServletPostAsyncTask().execute(new Pair<Context, String>(this, access));
    }

    private class ServletPostAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
        private Context context;

        @Override
        protected String doInBackground(Pair<Context, String>... params) {
            context = params[0].first;
            String name = params[0].second;

            try {
                // Set up the request
                URL url = new URL("http://likemeister-145918.appspot.com/Predictor");
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
            String[] output= result.split("___anambhatia___");
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

                outputText.setText("Your Best Friend is:" +"\n"+ list.get(0)+"\n"+"with "+list1.get(0)+" likes!");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }
    }


}
