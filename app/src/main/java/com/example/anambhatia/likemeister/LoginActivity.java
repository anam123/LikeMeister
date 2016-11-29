package com.example.anambhatia.likemeister;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * Created by AnamBhatia on 11/10/16.
 */

//Launcher Activity
public class LoginActivity extends Activity{

    private TextView info;
     TextView font;
    TextView f1;
    private LoginButton loginButton;
    String accesstoken;
    public Button btn;
    private CallbackManager callbackManager;
    ImageView image;


    //on pressing back button, quit app.
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }


    //Use facebook sdk to login.
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.login_activity);
        image = (ImageView) findViewById(R.id.imageView2);

        info = (TextView)findViewById(R.id.info);
        loginButton = (LoginButton)findViewById(R.id.login_button);
        //ask the user for required permissions
        loginButton.setReadPermissions(Arrays.asList("user_likes", "user_friends", "user_status", "user_posts", "public_profile","read_custom_friendlists","read_insights"));
        font=(TextView)findViewById(R.id.textView3);
        f1=(TextView)findViewById(R.id.textView4);
        //change font
        Typeface typeFace=Typeface.createFromAsset(getAssets(),"BEBAS___.ttf");
        Typeface typeFace1=Typeface.createFromAsset(getAssets(),"YanoneKaffeesatz-Regular.otf");
        font.setTypeface(typeFace);
        f1.setTypeface(typeFace1);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                //take access token and bundle it to pass it to the other fragments.
                accesstoken=loginResult.getAccessToken().getToken();
                System.out.println("access: "+loginResult.getAccessToken().getToken());
                System.out.println(AccessToken.getCurrentAccessToken().getDeclinedPermissions().toString());
                System.out.println(AccessToken.getCurrentAccessToken().getPermissions().toString());

                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                intent.putExtra("number",accesstoken);
                startActivity(intent);

            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException e) {

                info.setText("Login attempt failed.");

            }





        });



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }





}
