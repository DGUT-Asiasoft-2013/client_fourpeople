package com.example.fourpeople.campushousekeeper.person;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.api.Server;
import com.example.fourpeople.campushousekeeper.person.inputcells.PersonPictureInputCellFragment;
import com.example.fourpeople.campushousekeeper.person.inputcells.PersonSimpleTextInputCellFragment;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyInfoFragment extends Fragment {
	
	View view;
	PersonSimpleTextInputCellFragment fragModifyName;
	Spinner spinnerModifySex;
	String spinnerStrSex;
	PersonSimpleTextInputCellFragment fragModifyAddress;
	PersonSimpleTextInputCellFragment fragModifyTel;
	PersonPictureInputCellFragment fragModifyAvatar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view==null) {
			view = inflater.inflate(R.layout.person_fragment_modify_info, null);
			
			fragModifyName = (PersonSimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.modify_name);
			spinnerModifySex = (Spinner) view.findViewById(R.id.modify_sex);
			fragModifyAddress = (PersonSimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.modify_address);
			fragModifyTel = (PersonSimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.modify_tel);
			fragModifyAvatar = (PersonPictureInputCellFragment) getFragmentManager().findFragmentById(R.id.modify_avatar);
			
			spinnerModifySex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
	            @Override
	            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
	            	spinnerStrSex = adapterView.getItemAtPosition(i).toString();
	            }

	            @Override
	            public void onNothingSelected(AdapterView<?> adapterView) {
	            	spinnerStrSex = adapterView.getItemAtPosition(0).toString();
	            }
	        });
			
			view.findViewById(R.id.btn_upload_modify).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				modifySubmit();
				}
			});
		}

		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		fragModifyName.setLabelText("输入新昵称");
		fragModifyName.setText(fragModifyName.getText());
		fragModifyAddress.setLabelText("输入新地址：");
		fragModifyAddress.setText(fragModifyAddress.getText());
		fragModifyTel.setLabelText("输入新电话：");
		fragModifyTel.setText(fragModifyTel.getText());
	}
	
	void modifySubmit() {
		String name = fragModifyName.getText();
		String sex = spinnerStrSex;
		String address = fragModifyAddress.getText();
		String tel = fragModifyTel.getText();

		OkHttpClient client = Server.getSharedClient();

		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("name", name)
				.addFormDataPart("sex", sex)
				.addFormDataPart("address", address)
				.addFormDataPart("tel", tel);

		if (fragModifyAvatar.getPngData()!=null) {
			requestBodyBuilder.addFormDataPart("avatar",
					"avatar",
					RequestBody
					.create(MediaType.parse("image/png"), fragModifyAvatar.getPngData()));
		}

		Request request = Server.requestBuildWithApi("modify")
                .method("post", null)
                .post(requestBodyBuilder.build())
                .build();

		final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("请稍候...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        client.newCall(request).enqueue(new Callback() {

			@Override
			public void onResponse(final Call arg0, final Response arg1) throws IOException {
				final String responseString = arg1.body().string();
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();

						try {
							ModifyInfoFragment.this.onResponse(arg0, responseString);
						} catch (Exception e) {
							e.printStackTrace();
							ModifyInfoFragment.this.onFailure(arg0, e);
						}

					}
				});

			}

			@Override
			public void onFailure(final Call arg0, final IOException arg1) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						progressDialog.dismiss();
						ModifyInfoFragment.this.onFailure(arg0, arg1);
					}
				});

			}
		});
	}

	void onResponse(Call arg0, String responseBody)
	{
		new AlertDialog.Builder(getActivity())
		.setTitle("修改成功")
		.setMessage(responseBody)
		.setPositiveButton("OK", null)
		.show();
	}

	void onFailure(Call arg0, Exception arg1)
	{
		new AlertDialog.Builder(getActivity())
		.setTitle("修改失败！")
		.setMessage(arg1.getLocalizedMessage())
		.setNegativeButton("OK", null)
		.show();
	}
}
