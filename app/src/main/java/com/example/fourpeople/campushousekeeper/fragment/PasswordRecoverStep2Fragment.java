package com.example.fourpeople.campushousekeeper.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.SimpleTextInputCellFragment;

public class PasswordRecoverStep2Fragment extends Fragment {
    View view;
    SimpleTextInputCellFragment fragPassword;
    SimpleTextInputCellFragment fragPasswordRepeat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view==null)
            view = inflater.inflate(R.layout.fragment_password_recover_step2, null);

        fragPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password);
        fragPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_password_repeat);
        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onSubmitClicked();
            }
        });

        return view;
    }

    public String getPassword()
    {
        return fragPassword.getText();
    }

    public String getPasswordRepeat() {
        return fragPasswordRepeat.getText();
    }

    public static interface OnSubmitClickedListener
    {
        void onSubmitClicked();
    }

    @Override
    public void onResume() {
        super.onResume();
        fragPassword.setLabelText("密码");
        fragPassword.setHintText("请输入新密码");
        fragPassword.setIsPassword(true);
        fragPasswordRepeat.setLabelText("重复密码：");
        fragPasswordRepeat.setHintText("请再次输入密码：");
        fragPasswordRepeat.setIsPassword(true);
    }

    OnSubmitClickedListener onSubmitClickedListener;

    public void setOnSubmitClickedListener(OnSubmitClickedListener onSubmitClickedListener)
    {
        this.onSubmitClickedListener = onSubmitClickedListener;
    }

    void onSubmitClicked()
    {
        if (fragPassword.getText().equals(fragPasswordRepeat.getText()))
        {
            if (onSubmitClickedListener!=null)
                onSubmitClickedListener.onSubmitClicked();
        }
        else
        {
            new AlertDialog.Builder(getActivity())
                    .setMessage("重复密码不一致！")
                    .show();
        }
    }
}