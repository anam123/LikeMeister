package com.example.anambhatia.likemeister;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by AnamBhatia on 20/10/16.
 */

public class Predictor extends Fragment {


    TextView attach;
    EditText stat;
    EditText tag;
    CheckBox pp;
    RadioButton img;
    RadioButton vid;
    RadioButton link;
    Button pred;
    String access;
    TextView result;
    ProgressBar b;
    TextView ee;
    Button back;
    RadioGroup rg;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


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
        protected void onPostExecute(String res) {

            List<String> posts = new ArrayList<String>();
            List<Integer> likesperpost = new ArrayList<Integer>();
            String[] output= res.split("___");

            int allposts=Integer.parseInt(output[6]);
            int alllikes=Integer.parseInt(output[4]);


            try {
                JSONArray jsonArray = new JSONArray(output[3]);


                for (int j = 0; j < jsonArray.length(); j++) {
                    likesperpost.add(jsonArray.getInt(j));
                }
            }
            catch (JSONException e){

                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(output[2]);


                for (int j = 0; j < jsonArray.length(); j++) {
                    posts.add(jsonArray.getString(j));
                }
            }
            catch (JSONException e1)
            {
                e1.printStackTrace();
            }




          //  LinkedHashMap<String,Integer> likes=new LinkedHashMap<>();
            for(int i=0;i<posts.size();i++){

              //  likes.put(posts.get(i),likesperpost.get(i));
                Log.d("hi: ",posts.get(i)+" "+likesperpost.get(i));

            }

            int linklikes;
            int imagelikes;
            int videolikes;
            int statuslikes;
            ArrayList<Integer> links=new ArrayList<>();
            ArrayList<Integer> images=new ArrayList<>();
            ArrayList<Integer> videos=new ArrayList<>();
            ArrayList<Integer> status=new ArrayList<>();

            int totallinks=0;
            int totalphotos=0;
            int totalvideos=0;
            int totalstatus=0;


            for(int i=0;i<posts.size();i++)
            {
                String a;

                if(posts.get(i).contains(":")==true) {
                    a = posts.get(i).split(":")[1];
                }
                else
                {
                    a=posts.get(i);
                }
                int like=likesperpost.get(i);

                System.out.println("kk: "+a);
                System.out.println("k1k: "+like);
                if(a.equals("link") || posts.get(i).equals("link"))
                {
                    links.add(like);
                    totallinks=totallinks+like;
                }
                else if(a.equals("status") || posts.get(i).equals("status"))
                {
                    status.add(like);
                    totalstatus=totalstatus+like;
                }
                else if(a.equals("photo") || posts.get(i).equals("photo"))
                {
                    images.add(like);
                    totalphotos=totalphotos+like;
                }
                else if(a.equals("video") || posts.get(i).equals("video"))
                {
                    videos.add(like);
                    totalvideos=totalvideos+like;
                }
            }


            System.out.println(totallinks+" "+totalphotos+" "+totalstatus+" "+totalvideos);
            System.out.println(links.size()+" "+images.size()+" "+status.size()+" "+videos.size());
            linklikes=totallinks/links.size();
           imagelikes=totalphotos/images.size();
            videolikes=totalvideos/videos.size();
            statuslikes=totalstatus/status.size();

            System.out.println("Average likes on links: "+ linklikes);
            System.out.println("Average likes on status: "+ statuslikes);
            System.out.println("Average likes on videos: "+ videolikes);
            System.out.println("Average likes on photos: "+ imagelikes);



            result.setText(output[6].toString()+ " Likes!");


            b.setVisibility(View.GONE);
            result.setVisibility(View.VISIBLE);
            back.setVisibility(View.VISIBLE);
            ee.setVisibility(View.VISIBLE);



        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.predictor, container, false);
        attach=((TextView) v.findViewById(R.id.textView7));
        stat=((EditText) v.findViewById(R.id.status));
        tag=((EditText) v.findViewById(R.id.tags));
        pp=((CheckBox) v.findViewById(R.id.checkBox7));
        rg=((RadioGroup) v.findViewById(R.id.group));
        img=((RadioButton) v.findViewById(R.id.radioButton10));
        vid=((RadioButton) v.findViewById(R.id.radioButton12));
        link=((RadioButton) v.findViewById(R.id.radioButton11));
        pred=((Button) v.findViewById(R.id.button2));
        pred.setOnClickListener(btnListener);

        //Typeface typeFace=Typeface.createFromAsset(getAssets(),"BEBAS___.ttf");
       final Typeface typeFace1=Typeface.createFromAsset(getActivity().getAssets(),"YanoneKaffeesatz-Regular.otf");
        attach.setTypeface(typeFace1);
        stat.setTypeface(typeFace1);
        tag.setTypeface(typeFace1);
        pp.setTypeface(typeFace1);
        img.setTypeface(typeFace1);
        vid.setTypeface(typeFace1);
        link.setTypeface(typeFace1);
        pred.setTypeface(typeFace1);

        access = getArguments().getString("token");
        return v;
    }

    private View.OnClickListener btnListener = new View.OnClickListener()
    {

        Dialog dialog;
        public void onClick(View v) {


                String status = stat.getText().toString();
                final int tags = Integer.parseInt(tag.getText().toString());
                final Boolean profile = pp.isChecked();
                final Boolean image = img.isChecked();
                final Boolean video = vid.isChecked();
                final Boolean lnk = link.isChecked();

                dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alertdialog);


                b = (ProgressBar) dialog.findViewById(R.id.pb);

                result = (TextView) dialog.findViewById(R.id.textView27);
                back = ((Button) dialog.findViewById(R.id.button4));
                ee = (TextView) dialog.findViewById(R.id.textView28);
                final Typeface typeFace1 = Typeface.createFromAsset(getActivity().getAssets(), "YanoneKaffeesatz-Regular.otf");
                result.setTypeface(typeFace1);
                back.setTypeface(typeFace1);
                ee.setTypeface(typeFace1);

                new Predictor.ServletPostAsyncTask().execute(new Pair<Context, String>(getContext(), access));


                back.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        stat.setText("");
                        tag.setText("");
                        if (profile == true) {
                            pp.toggle();
                        }
                        if (image == true) {

                            rg.clearCheck();                        }
                        if (video == true) {
                            rg.clearCheck();
                        }
                        if (lnk == true) {
                            rg.clearCheck();
                        }


                    }
                });

                dialog.show();

            }


    };

}