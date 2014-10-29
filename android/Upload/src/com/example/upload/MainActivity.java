package com.example.upload;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	static JSONObject json = null;

	// JSON element ids from repsonse of php script:
	private static final String JSON_TAG_SUCCESS = "success";
	private static final String JSON_TAG_MESSAGE = "message";

	TextView messageText;
	Button uploadButton;
	ProgressDialog pDialog = null;
	
	private JSONParser jsonParser;	
	private static final String ACTIVITY_TAG = "UPLOAD";
	private static final String UPLOAD_URL = "http://192.168.56.1/upload/savetofile.php";

	/********** File Path *************/
	final String uploadFilePath = Environment.getExternalStorageDirectory()
			.getAbsolutePath() + "/";
	final String uploadFileName = "service_lifecycle.png";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		jsonParser = new JSONParser();		
		initView();
		initUploadEvent();
	}

	private void initView() {
		// TODO Auto-generated method stub
		uploadButton = (Button) findViewById(R.id.uploadButton);
		messageText = (TextView) findViewById(R.id.messageText);

		messageText.setText("Uploading file path : " + uploadFilePath
				+ uploadFileName + "'");		
	}

	private void initUploadEvent() {
		// TODO Auto-generated method stub
		uploadButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new UploadFileFromURL().execute(uploadFilePath, uploadFileName);

			}
		});		
	}

	class UploadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(MainActivity.this);
			pDialog.setMessage("Upload....");
			pDialog.setIndeterminate(false);// 取消進度條
			pDialog.setCancelable(true);// 開啟取消
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			int success;
			String filePath = args[0];
			String fileName = args[1];
			File sourceFile = new File(filePath + fileName);

			if (!sourceFile.isFile()) {
				String strMessage = "Source File not exist :" + filePath
						+ fileName;
				Log.e("uploadFile", strMessage);
				return strMessage;
			}

			JSONObject json = jsonParser.makeHttpRequest(UPLOAD_URL, sourceFile,fileName);
					
			try {
				Log.d(ACTIVITY_TAG, "Upload attempt : " + json.toString());
				success = json.getInt(JSON_TAG_SUCCESS);
				if (success == 1) {
					Log.d(ACTIVITY_TAG,
							"Upload Successful : " + json.toString());

					return json.getString(JSON_TAG_MESSAGE);
				} else {
					Log.d(ACTIVITY_TAG,
							"Upload Failure : "
									+ json.getString(JSON_TAG_MESSAGE));
					return json.getString(JSON_TAG_MESSAGE);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return "Source File exist :" + filePath + fileName;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (result != null) {
				Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}

		}

	}
}