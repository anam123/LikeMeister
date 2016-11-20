package com.example.anambhatia.likemeister;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class EmptyActivity extends Fragment {

//    TextView tv2;
//    TextView tv1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.empty, container, false);
//        tv1=((TextView) v.findViewById(R.id.textView7));
//        //Typeface typeFace=Typeface.createFromAsset(getAssets(),"BEBAS___.ttf");
//        final Typeface typeFace1=Typeface.createFromAsset(getActivity().getAssets(),"YanoneKaffeesatz-Regular.otf");
//        tv1.setTypeface(typeFace1);
        return v;
    }
}