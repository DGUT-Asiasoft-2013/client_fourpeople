package com.example.fourpeople.campushousekeeper.person;



import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;

public class InfoItemCellFragment extends Fragment {
	
	TextView itemName;
	TextView itemInfo;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.person_fragment_info_item_cell, container);
		itemName = (TextView) view.findViewById(R.id.item_name);
		itemInfo = (TextView) view.findViewById(R.id.item_info);
		
		return view;
	}
	
	public void setItemName(String name) {
		itemName.setText(name);
	}
	
	public void setItemInfo(String info) {
		itemInfo.setText(info);
	}
	
	public String getItemTnfo() {
		return itemInfo.getText().toString();
	}

}
