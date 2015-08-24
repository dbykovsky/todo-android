package com.home.dbykovskyy.simpletodo.DialogFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.home.dbykovskyy.simpletodo.ItemPriority;

/**
 * Created by dbykovskyy on 8/22/15.
 */
public class MyAlertDialogFragment extends DialogFragment {

    public MyAlertDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public DialogInterface.OnClickListener positiveClickListener;
    public DialogInterface.OnClickListener cancelClickListener;
    public DialogInterface.OnClickListener onChoiceClickListener;

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener){
        this.positiveClickListener=positiveListener;
    }

    public void setCancelClickListener(DialogInterface.OnClickListener cancelListener){
        this.cancelClickListener = cancelListener;
    }

    public void setOnChoiceClickListener(DialogInterface.OnClickListener onChoiceListener){
        this.onChoiceClickListener=onChoiceListener;
    }

    public static MyAlertDialogFragment newInstance(String title) {
        MyAlertDialogFragment frag = new MyAlertDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setSingleChoiceItems(ItemPriority.priority, -1, onChoiceClickListener);
        alertDialogBuilder.setPositiveButton("OK", positiveClickListener);
        alertDialogBuilder.setNegativeButton("Cancel", cancelClickListener);
        return alertDialogBuilder.create();
    }

}
