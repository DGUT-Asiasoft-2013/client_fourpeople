package com.example.fourpeople.campushousekeeper.chat.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fourpeople.campushousekeeper.R;
import com.example.fourpeople.campushousekeeper.chat.ChatActivity;


public class DialogManager {

	private AlertDialog.Builder builder;
	private ImageView mIcon;
	private ImageView mVoice;
	private TextView mLable;
	private TextView speak;
	
	

	private Context mContext;

	private AlertDialog dialog;// ����ȡ��AlertDialog.Builder

	/**
	 * ���췽�� ����������
	 */
	public DialogManager(Context context) {
		this.mContext = context;
	}

	// ��ʾ¼���ĶԻ���
	@SuppressLint("NewApi")
	public void showRecordingDialog() {

		builder = new AlertDialog.Builder(mContext, R.style.Theme_AudioDialog);
		//�����Ի��򲼾�
		LayoutInflater inflater = LayoutInflater.from(mContext);
		View view = inflater.inflate(R.layout.dialog_recorder, null);

	    mIcon = (ImageView) view.findViewById(R.id.id_recorder_dialog_icon);
		mVoice = (ImageView) view.findViewById(R.id.id_recorder_dialog_voice);
		mLable = (TextView) view.findViewById(R.id.id_recorder_dialog_label);
		speak = (TextView) view.findViewById(R.id.textView1);
		speak.setText(ChatActivity.cScene);
		builder.setView(view);
		builder.create();
		dialog = builder.show();
	}

	public void recording() {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
			speak.setVisibility(View.VISIBLE);
			mVoice.setVisibility(View.VISIBLE);
			mLable.setVisibility(View.VISIBLE);
			mIcon.setVisibility(View.GONE);

			//mIcon.setImageResource(R.drawable.recorder);
			mLable.setText("��ָ�ϻ���ȡ������");
			speak.setText(ChatActivity.cScene);
			
		}
	}

	// ��ʾ��ȡ���ĶԻ���
	public void wantToCancel() {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
			speak.setVisibility(View.GONE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			mIcon.setVisibility(View.VISIBLE);
			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText("�ɿ���ָ��ȡ������");
		}
	}
	public void changeScence() {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
			speak.setVisibility(View.GONE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			mIcon.setVisibility(View.VISIBLE);
			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText("��ָ�󻬣�ѡ�񳡾�");
		}
	}
	
	public void changeToEdit() {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
			speak.setVisibility(View.GONE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			mIcon.setVisibility(View.VISIBLE);
			mIcon.setImageResource(R.drawable.cancel);
			mLable.setText("��ָ�һ�����������");
		}
	}

	// ��ʾʱ����̵ĶԻ���
	public void tooShort() {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
			speak.setVisibility(View.GONE);
			mVoice.setVisibility(View.GONE);
			mLable.setVisibility(View.VISIBLE);
			mIcon.setVisibility(View.VISIBLE);
			mIcon.setImageResource(R.drawable.voice_to_short);
			mLable.setText("¼��ʱ�����");
		}
	}

	
	// ��ʾȡ���ĶԻ���
	public void dimissDialog() {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
			dialog.dismiss();
			dialog = null;
		}
	}

	// ��ʾ������������ĶԻ���
	public void updateVoiceLevel(int level) {
		if (dialog != null && dialog.isShowing()) { // ��ʾ״̬
		// mIcon.setVisibility(View.VISIBLE);
		// mVoice.setVisibility(View.VISIBLE);
		// mLable.setVisibility(View.VISIBLE);

			// ����ͼƬ��id
			int resId = mContext.getResources().getIdentifier("v" + level,
					"drawable", mContext.getPackageName());
			mVoice.setImageResource(resId);
		}
	}
	
	

	

}