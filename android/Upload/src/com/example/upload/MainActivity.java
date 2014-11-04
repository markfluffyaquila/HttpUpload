package com.example.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements UploadFileCallback {
	private static final String UPLOAD_URL = "http://192.168.0.101/upload/savetofile.php";

	private TextView messageText;
	private Button uploadButton;
	private ProgressDialog pDialog = null;

	/********** File Path *************/
	final String uploadFilePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/";
	final String uploadFileName = "service_lifecycle.png";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
		initUploadEvent();
	}

	private void initView() {
		// TODO Auto-generated method stub
		uploadButton = (Button) findViewById(R.id.uploadButton);
		messageText = (TextView) findViewById(R.id.messageText);

		messageText.setText(getResources().getString(
				R.string.main_activity_upload_text)
				+ uploadFilePath + uploadFileName + "'");
	}

	private void initUploadEvent() {
		// TODO Auto-generated method stub
		uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new UploadFileFromURL(MainActivity.this).execute(UPLOAD_URL,
						uploadFilePath, uploadFileName);
			}
		});
	}

	@Override
	public void onUploadFilePreExecute() {
		// TODO Auto-generated method stub
		pDialog = new ProgressDialog(MainActivity.this);
		pDialog.setMessage(getResources().getString(
				R.string.main_activity_uploading_progress_dialog_context));
		pDialog.setIndeterminate(false);// 取消進度條
		pDialog.setCancelable(true);// 開啟取消
		pDialog.setMax(100);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.show();
	}

	@Override
	public void onUploadFileProgressUpdate(int value) {
		// TODO Auto-generated method stub
		pDialog.setProgress(value);
	}

	@Override
	public void doUploadFilePostExecute(String result) {
		// TODO Auto-generated method stub
		pDialog.dismiss();
		if (result != null) {
			Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}

}