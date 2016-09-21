package com.gospay.sdk.ui.payment.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gospay.sdk.R;

/**
 * Created by Bertalt on 18.05.2016.
 */
public class DescriptionBrowserFragment extends DialogFragment {

    private TextView tvClose;
    private Button btnNext;
    private String mUrl;

    public static final String TAG = "DescriptionBrowserDialog";


    public DescriptionBrowserFragment() {
    }

    private final static String ARG_URL = "arg_url";

    public static DescriptionBrowserFragment newInstance(String url){

        Bundle args = new Bundle();
        args.putString(ARG_URL, url);

        DescriptionBrowserFragment fragment = new DescriptionBrowserFragment();
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();

        if(args != null){
            mUrl = args.getString(ARG_URL);
        }

    }

    private static DescriptionBrowserFragment sInstance;


    public static DescriptionBrowserFragment getInstance() {

        if (sInstance != null) return sInstance;

        sInstance = new DescriptionBrowserFragment();
        return sInstance;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_trouble_browser, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(view);

        builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });


        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mUrl));
                startActivity(browserIntent);
                dismiss();
            }
        });

        return builder.create();
    }

}