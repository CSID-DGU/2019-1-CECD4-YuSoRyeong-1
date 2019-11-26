package com.hanium.glass;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CustomDialogFragment extends DialogFragment implements View.OnClickListener{
    private static final String TAG = "CustomDialogFragment";
    private static final String ARG_DIALOG_MAIN_MSG = "dialog_main_msg";

    private String mMainMsg;

    public static CustomDialogFragment newInstance(String mainMsg) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_DIALOG_MAIN_MSG, mainMsg);

        CustomDialogFragment fragment = new CustomDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMainMsg = getArguments().getString(ARG_DIALOG_MAIN_MSG);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        int dialogWidth = getResources().getDimensionPixelSize(android.R.dimen.dialog_min_width_major);
        int dialogHeight = ActionBar.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_custom_dialog, null);
        ((TextView)view.findViewById(R.id.dialog_confirm_msg)).setText(mMainMsg);
        view.findViewById(R.id.dialog_confirm_btn).setOnClickListener(this);

        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

/*
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_custom_dialog, container, false);
        ((TextView)view.findViewById(R.id.dialog_confirm_msg)).setText(mMainMsg);
        view.findViewById(R.id.dialog_confirm_btn).setOnClickListener(this);
        Dialog dialog = getDialog();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        return view;
    }
*/

    private void dismissDialog() {
        this.dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.dialog_confirm_btn:
                dismissDialog();
                break;
        }
    }
}