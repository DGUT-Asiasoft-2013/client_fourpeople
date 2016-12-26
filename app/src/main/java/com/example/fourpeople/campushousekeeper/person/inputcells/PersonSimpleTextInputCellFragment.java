package com.example.fourpeople.campushousekeeper.person.inputcells;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.fragment.inputcells.BaseInputCellFragment;

public class PersonSimpleTextInputCellFragment extends BaseInputCellFragment {
    TextView label;
    EditText edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.person_fragment_inputcell_simpletext, container);
        label = (TextView) view.findViewById(R.id.label);
        edit = (EditText)view.findViewById(R.id.edit);

        return view;
    }

    public void setLabelText(String labelText)
    {
        label.setText(labelText);
    }

    public void setHintText(String HintText)
    {
        edit.setHint(HintText);
    }

    public String getText()
    {
        return edit.getText().toString();
    }

    public TextView getLabel() {
        return label;
    }

    public void setLabel(TextView label) {
        this.label = label;
    }

    public EditText getEdit() {
        return edit;
    }

    public void setEdit(EditText edit) {
        this.edit = edit;
    }

    public void setText(String text) {
        this.edit.setText(text);
    }

    public void setIsPassword(boolean isPassword)
    {
        if(isPassword)
        {
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        }
        else
        {
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }
    }

}