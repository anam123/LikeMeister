package com.example.anambhatia.likemeister;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by AnamBhatia on 20/10/16.
 */

//Predictor class to predict number of likes on future posts.

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
    LoginButton share;
    RadioGroup rg;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    int tags;
    Boolean profile;
    Boolean image;
    Boolean video;
    Boolean lnk;

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


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

        //weight function to strip trivial words and calculate weight of every important word of a string where weight is the likes for a word.
        public ArrayList<String> weight(String input) {

            String[] temp = input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
            ArrayList<String> words = new ArrayList<String>(Arrays.asList(temp));
            ArrayList<String> trivial = new ArrayList<String>(Arrays.asList("i","a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"));
            Iterator<String> trivialIt = trivial.iterator();
            while(trivialIt.hasNext()){
                String remove = trivialIt.next();
                while(words.remove(remove)){
                }
            }

            return words;
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
                    //return e.getMessage();
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
        protected void onPostExecute(String res) {

            //Algorithm implemented below.


            if (result.equals("A")) {

                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                b.setEnabled(false);
                dialog.dismiss();

            } else {

                try {
                    //Data extract from server convert into usable form.
                    List<String> posts = new ArrayList<String>();
                    List<Integer> likesperpost = new ArrayList<Integer>();
                    List<Integer> tagsperpost = new ArrayList<Integer>();
                    String[] output = res.split("___anambhatia___");

                    double allposts = Integer.parseInt(output[6]);
                    double alllikes = Integer.parseInt(output[4]);


                    try {
                        JSONArray jsonArray = new JSONArray(output[3]);


                        for (int j = 0; j < jsonArray.length(); j++) {
                            likesperpost.add(jsonArray.getInt(j));
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    try {
                        JSONArray jsonArray = new JSONArray(output[9]);


                        for (int j = 0; j < jsonArray.length(); j++) {
                            tagsperpost.add(jsonArray.getInt(j));
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }


                    try {
                        JSONArray jsonArray = new JSONArray(output[2]);


                        for (int j = 0; j < jsonArray.length(); j++) {
                            posts.add(jsonArray.getString(j));
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                    //  LinkedHashMap<String,Integer> likes=new LinkedHashMap<>();
                    for (int i = 0; i < posts.size(); i++) {

                        //  likes.put(posts.get(i),likesperpost.get(i));
                        Log.d("hi: ", posts.get(i) + " " + likesperpost.get(i));

                    }


                    double averagelikes;
                    averagelikes = alllikes / allposts;
                    double photolikechange;
                    double linklikechange;
                    double videolikechange;
                    double statuslikechange;
                    double profilelikechange;
                    double linklikes;
                    double imagelikes;
                    double videolikes;
                    double statuslikes;
                    double increasepertagphoto;
                    double increasepertaglink;
                    double increasepertagstatus;
                    double increasepertagvideo;
                    double totaltags = 0;
                    double totallikestags = 0;
                    double likesperword;
                    int cnt = 0;


                    for (int i = 0; i < tagsperpost.size(); i++) {
                        totaltags = totaltags + tagsperpost.get(i);
                    }
                    if (totaltags == 0) {
                        increasepertagphoto = 0;
                        increasepertagvideo = 0;
                        increasepertaglink = 0;
                        increasepertagstatus = 0;
                    } else {
                        for (int i = 0; i < tagsperpost.size(); i++) {
                            if (tagsperpost.get(i) > 0) {
                                totallikestags = likesperpost.get(i) + totallikestags;
                                System.out.println("tags " + tagsperpost);
                                cnt = cnt + 1;
                            }
                        }
                        double averagefortags;
                        if (cnt == 0) {
                            averagefortags = 0;
                            increasepertaglink = 0;
                            increasepertagphoto = 0;
                            increasepertagstatus = 0;
                            increasepertagvideo = 0;
                        } else {
                            averagefortags = totallikestags / cnt;
                            increasepertaglink = (averagefortags - averagelikes) / totaltags;
                            increasepertagphoto = (averagefortags - averagelikes) / totaltags;
                            increasepertagstatus = (averagefortags - averagelikes) / totaltags;
                            increasepertagvideo = (averagefortags - averagelikes) / totaltags;
                        }

                    }
                    ArrayList<Double> links = new ArrayList<>();
                    ArrayList<Double> images = new ArrayList<>();
                    ArrayList<Double> videos = new ArrayList<>();
                    ArrayList<Double> status = new ArrayList<>();

                    double totallinks = 0;
                    double totalphotos = 0;
                    double totalvideos = 0;
                    double totalstatus = 0;
                    double profilelikes = 0;
                    double totalimportantwords = 0;


                    for (int i = 0; i < posts.size(); i++) {
                        String a;
                        String b;
                        ArrayList<String> b1 = new ArrayList<>();


                        //split between links,images,videos and status.
                        if (posts.get(i).contains(":") == true) {
                            b = posts.get(i).split(":")[0];
                            a = posts.get(i).split(":")[1];
                            b1 = weight(b);
                        } else {
                            a = posts.get(i);
                            b = "";
                        }

                        totalimportantwords = totalimportantwords + b1.size();


                        double like = likesperpost.get(i);

                        System.out.println("kk: " + a);
                        System.out.println("k1k: " + like);


                        if (a.equals("LINK") || posts.get(i).equals(" LINK")) {
                            links.add(like);
                            totallinks = totallinks + like;

                        } else if (a.equals("STATUS") || posts.get(i).equals(" STATUS")) {
                            status.add(like);
                            totalstatus = totalstatus + like;
                        } else if (a.equals("PHOTO") || posts.get(i).equals(" PHOTO")) {
                            images.add(like);
                            totalphotos = totalphotos + like;
                        } else if (a.equals("VIDEO") || posts.get(i).equals(" VIDEO")) {
                            videos.add(like);
                            totalvideos = totalvideos + like;
                        }
                    }


                    System.out.println(totallinks + " " + totalphotos + " " + totalstatus + " " + totalvideos);
                    System.out.println(links.size() + " " + images.size() + " " + status.size() + " " + videos.size());
                    likesperword = alllikes / totalimportantwords;
                    linklikes = totallinks / links.size();
                    imagelikes = totalphotos / images.size();
                    videolikes = totalvideos / videos.size();
                    statuslikes = totalstatus / status.size();
                    double sum1 = 0;
                    double sum2 = 0;
                    for (int i = 0; i < images.size() / 2; i++) {
                        sum1 = sum1 + images.get(i);
                    }
                    for (int i = images.size() / 2; i < images.size(); i++) {
                        sum2 = sum2 + images.get(i);
                    }
                    double sum3 = 0;
                    double sum4 = 0;
                    for (int i = 0; i < links.size() / 2; i++) {
                        sum3 = sum3 + links.get(i);
                    }
                    for (int i = links.size() / 2; i < links.size(); i++) {
                        sum4 = sum4 + links.get(i);
                    }
                    double sum5 = 0;
                    double sum6 = 0;
                    for (int i = 0; i < videos.size() / 2; i++) {
                        sum5 = sum5 + videos.get(i);
                    }
                    for (int i = videos.size() / 2; i < videos.size(); i++) {
                        sum6 = sum6 + videos.get(i);
                    }
                    double sum7 = 0;
                    double sum8 = 0;
                    for (int i = 0; i < status.size() / 2; i++) {
                        sum7 = sum7 + status.get(i);
                    }
                    for (int i = status.size() / 2; i < status.size(); i++) {
                        sum8 = sum8 + status.get(i);
                    }

                    photolikechange = (sum1 - sum2) / (sum1 + sum2);
                    linklikechange = (sum3 - sum4) / (sum4 + sum3);
                    videolikechange = (sum5 - sum6) / (sum5 + sum6);
                    statuslikechange = (sum7 - sum8) / (sum7 + sum8);


                    ArrayList<Double> sortedimage = new ArrayList<>();
                    sortedimage = images;
                    Collections.sort(sortedimage);
                    for (int i = sortedimage.size() - 1; i > sortedimage.size() - 2; i--) {
                        profilelikes = profilelikes + sortedimage.get(i);
                    }
                    profilelikes = profilelikes;

                    //Various variables used for calculating expected likes
                    System.out.println("Total likes: " + alllikes);
                    System.out.println("Total posts: " + allposts);
                    System.out.println("Average likes: " + averagelikes);
                    System.out.println("Average likes on links: " + linklikes);
                    System.out.println("Average likes on status: " + statuslikes);
                    System.out.println("Average likes on videos: " + videolikes);
                    System.out.println("Average likes on photos: " + imagelikes);
                    System.out.println("Average likes on profilephotos: " + profilelikes);
                    System.out.println("Average change on photos: " + photolikechange);
                    System.out.println("Average change on status: " + statuslikechange);
                    System.out.println("Average change on links: " + linklikechange);
                    System.out.println("Average change on videos: " + videolikechange);
                    System.out.println("Likes per word: " + likesperword);
                    System.out.println("Increase per tag for links: " + increasepertaglink);
                    double answer;

                    System.out.println(profile + " " + image + " " + video + " " + lnk + " ");
                    System.out.println(status1);

                    //the calculation step.
                    if (profile == false && image == true && video == false && lnk == false) {
                        Double finale = (1.6 * imagelikes + 1.6 * ((photolikechange + 1) * (imagelikes)) + 0.4 * averagelikes + 0.4 * likesperword * (weight(status1).size()) + increasepertagphoto * tags + statuslikes) / 6.0;
                        answer = Math.ceil(finale);
                    } else if (profile == false && image == false && video == true && lnk == false) {
                        Double finale = (1.6 * videolikes + 1.6 * ((videolikechange + 1) * (videolikes)) + 0.4 * averagelikes + 0.4 * likesperword * (weight(status1).size()) + increasepertagvideo * tags + statuslikes) / 6.0;
                        answer = Math.ceil(finale);

                    } else if (profile == false && image == false && video == false && lnk == true) {

                        Double finale = (1.4 * linklikes + 1.6 * ((linklikechange + 1) * (linklikes)) + 0.4 * averagelikes + 0.6 * likesperword * (weight(status1).size()) + 1.4*increasepertaglink * tags + 0.6*statuslikes) / 6.0;
                        answer = Math.ceil(finale);

                    } else if (profile == true && image == true && video == false && lnk == false) {
                        Double finale = (1.8 * profilelikes + 1.8 * ((photolikechange + 1) * (profilelikes)) + 0.4 * increasepertagstatus * tags + 0.2 * likesperword * (weight(status1).size())) / 4.0;
                        answer = Math.ceil(finale);

                    } else {

                        Double finale = (averagelikes + 0.6 * likesperword * (weight(status1).size()) + increasepertagstatus * tags + statuslikes + 1.4 * ((statuslikechange + 1) * (statuslikes))) / 5.0;
                        answer = Math.ceil(finale);

                    }


                    result.setText(Double.toString(answer) + " Likes!");


                    b.setVisibility(View.GONE);
                    result.setVisibility(View.VISIBLE);
                    back.setVisibility(View.VISIBLE);
                    ee.setVisibility(View.VISIBLE);


                }
                catch (Exception E)
                {
                    Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();
                    b.setVisibility(View.GONE);
                    dialog.dismiss();
                }
            }
        }

    }

    Dialog dialog;

    String status1;
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

        final Typeface typeFace1=Typeface.createFromAsset(getActivity().getAssets(),"YanoneKaffeesatz-Regular.otf");



        //Typeface typeFace=Typeface.createFromAsset(getAssets(),"BEBAS___.ttf");

        attach.setTypeface(typeFace1);
        stat.setTypeface(typeFace1);
        tag.setTypeface(typeFace1);
        pp.setTypeface(typeFace1);
        img.setTypeface(typeFace1);
        vid.setTypeface(typeFace1);
        link.setTypeface(typeFace1);
        pred.setTypeface(typeFace1);

       pp.setEnabled(false);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch(checkedId){
                    case R.id.radioButton10:
                        pp.setEnabled(true);
                        break;

                    case R.id.radioButton12:
                        pp.setEnabled(false);
                        if(pp.isChecked()==true) {
                            pp.toggle();
                        }
                        break;

                    case R.id.radioButton11:
                        pp.setEnabled(false);
                        if(pp.isChecked()==true) {
                            pp.toggle();
                        }
                        break;
                }
            }
        });

        access = getArguments().getString("token");
        return v;
    }


    private View.OnClickListener btnListener = new View.OnClickListener()
    {

        //A dialog box opened to display results.

        public void onClick(View v) {

            if (Stats.CheckNetwork.isInternetAvailable(getActivity())) //returns true if internet available
            {

                if (stat.getText().toString() == null) {
                    status1 = "";
                } else {
                    status1 = stat.getText().toString();
                }
                if (tag.getText().toString() == null || tag.getText() == null || tag.getText().length() == 0) {
                    tags = 0;
                } else {
                    tags = Integer.parseInt(tag.getText().toString());
                }
                profile = pp.isChecked();
                image = img.isChecked();
                video = vid.isChecked();
                lnk = link.isChecked();

                if (image == false && video == false && lnk == false && status1.equals("")) {
                    Snackbar.make(getView(), "Enter atleast a status or select an attachment.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (profile == true) {

                    }

                    dialog = new Dialog(getActivity());
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alertdialog);


                    b = (ProgressBar) dialog.findViewById(R.id.pb);

                    result = (TextView) dialog.findViewById(R.id.textView27);
                    back = ((Button) dialog.findViewById(R.id.button4));
                    //share=((Button) dialog.findViewById(R.id.button6));
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
                                pp.setEnabled(false);
                            }
                            if (image == true) {

                                rg.clearCheck();
                                pp.setEnabled(false);
                            }
                            if (video == true) {
                                rg.clearCheck();
                                pp.setEnabled(false);
                            }
                            if (lnk == true) {
                                rg.clearCheck();
                                pp.setEnabled(false);
                            }


                        }
                    });


                    dialog.show();
                }

            }
            else
            {

                Toast.makeText(getContext(), "ERROR", Toast.LENGTH_SHORT).show();

            }
        }




    };

}