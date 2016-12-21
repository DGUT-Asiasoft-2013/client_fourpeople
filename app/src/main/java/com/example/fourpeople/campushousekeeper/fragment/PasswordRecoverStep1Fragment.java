package com.example.fourpeople.campushousekeeper.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.SimpleTextInputCellFragment;

public class PasswordRecoverStep1Fragment extends Fragment {
    SimpleTextInputCellFragment fragEmail;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view==null)
        {
            view = inflater.inflate(R.layout.fragment_password_recover_step1, null);

            fragEmail = (SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_email);

            view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    goNext();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        fragEmail.setLabelText("输入邮箱");
        fragEmail.setHintText("请输入邮箱");
    }

    public String getEmail()
    {
        return fragEmail.getText();
    }

    public static interface OnGoNextListener{ void onGoNext(); }

    OnGoNextListener onGoNextListener;

    public void setOnGoNextListener(OnGoNextListener onGoNextListener)
    {
        this.onGoNextListener = onGoNextListener;
    }

    void goNext()
    {
        if (onGoNextListener!=null)
            onGoNextListener.onGoNext();
    }

}
