package com.example.mapdemo;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

public class PopupActivity extends DialogFragment {
	EditText title;
	EditText extra;
	String titl, info;
	
	Activity Ar = getActivity();
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	Log.i("POP!" , "fvawr");
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        
        builder.setView(inflater.inflate(R.layout.popup, null))
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int ide) {
                   Dialog f = (Dialog) dialog;
                   		
                   		
                   		
                	   	title = (EditText) f.findViewById(R.id.title_field);                	   	
                	   	extra = (EditText) f.findViewById(R.id.info_field);
                	   	
                	   	titl = title.getText().toString();
                	   	info = extra.getText().toString();
                	   	
                	   	
                	   	
                	   	dialog.dismiss();
                	    MainActivity callingActivity = (MainActivity) getActivity();
                        callingActivity.onUserSelectValue(titl, info);

                   }
               })
        	.setNegativeButton("wqcqwd", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int ide) {System.out.println("LOLLL");
         	   	dialog.dismiss();       	  
            }
        });
        
        System.out.println("Seven");
		Log.i("POPUP", "wfcqw");
        return builder.create();
    }
 
}
