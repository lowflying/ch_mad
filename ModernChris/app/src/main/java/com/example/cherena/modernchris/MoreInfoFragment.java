package com.example.cherena.modernchris;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

/**
 * Created by 10364498 on 9/9/2017.
 */
public class MoreInfoFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        alertBuilder.setView(inflater.inflate((R.layout.moma_info), null));

        alertBuilder.setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent =
                        new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://moma.org"));
                startActivity(intent);
            }
        });
        alertBuilder.setNegativeButton("Not Now", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dI, int i){
                dI.cancel();
            }
        });
        return alertBuilder.create();
    }

    @Override
    public void onStart(){
        super.onStart();
        Button posBtn = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_POSITIVE);
        Button negBtn = ((AlertDialog) getDialog()).getButton(DialogInterface.BUTTON_NEGATIVE);

    }

}
