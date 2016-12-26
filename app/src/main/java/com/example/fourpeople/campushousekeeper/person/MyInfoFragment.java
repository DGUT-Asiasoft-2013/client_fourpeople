package com.example.fourpeople.campushousekeeper.person;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.example.fourpeople.campushousekeeper.R;

public class MyInfoFragment extends Fragment {
	View view;
	InfoItemCellFragment fragName;
	InfoItemCellFragment fragStudentId;
	InfoItemCellFragment fragSex;
	InfoItemCellFragment fragAddress;
	InfoItemCellFragment fragTel;
	InfoItemCellFragment fragEmail;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.person_fragment_my_info, null);

			fragName = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_name);
			fragStudentId = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_student_id);
			fragSex = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_sex);
			fragAddress = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_address);
			fragTel = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_tel);
			fragEmail = (InfoItemCellFragment) getFragmentManager().findFragmentById(R.id.info_email);

			view.findViewById(R.id.btn_update_info).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					goModify();
				}
			});
		}
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		fragName.setItemName("昵称：");
		fragName.setItemInfo("");
		fragStudentId.setItemName("学号：");
		fragStudentId.setItemInfo("");
		fragSex.setItemName("性别：");
		fragSex.setItemInfo("");
		fragAddress.setItemName("地址：");
		fragAddress.setItemInfo("");
		fragTel.setItemName("电话：");
		fragTel.setItemInfo("");
		fragEmail.setItemName("邮箱：");
		fragEmail.setItemInfo("");
	}

	// 去修改资料Fragment
	public static interface OnGoModifyListener { void onGoModify(); }

	OnGoModifyListener onGoModifyListener;

	public void setOnGoModifyListener(OnGoModifyListener onGoModifyListener) {
		this.onGoModifyListener = onGoModifyListener;
	}

	void goModify() {
		if (onGoModifyListener != null)
			onGoModifyListener.onGoModify();
	}
}
