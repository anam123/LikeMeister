package com.example.anambhatia.likemeister;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by AnamBhatia on 28/11/16.
 */

public class CustomListAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int id;
    private List<String> items ;

    //Custom list for UI Improvements
    public CustomListAdapter(Context context, int textViewResourceId , List<String> list )
    {
        super(context, textViewResourceId, list);
        mContext = context;
        id = textViewResourceId;
        items = list ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent)
    {
        View mView = v ;
        if(mView == null){
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = vi.inflate(id, null);
        }
        TextView text = (TextView) mView.findViewById(R.id.textView);
        //change the font
        final Typeface typeFace1=Typeface.createFromAsset(getContext().getAssets(),"YanoneKaffeesatz-Regular.otf");
        text.setTypeface(typeFace1);

        if(items.get(position) != null )
        {
            //change background and text colour
            text.setTextColor(Color.WHITE);
            text.setText(items.get(position));
            text.setBackgroundColor(Color.parseColor("#0084ff"));
            int color = Color.parseColor("#0084ff");
            text.setBackgroundColor( color );

        }

        return mView;
    }

}