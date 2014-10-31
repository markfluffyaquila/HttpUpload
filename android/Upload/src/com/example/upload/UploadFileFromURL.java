package com.example.upload;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class UploadFileFromURL extends AsyncTask<String, String, String> {
	UploadFileCallback callback;
	// constant value
	private static final String ACTIVITY_TAG = "UPLOAD";

	// JSON element ids from repsonse of php script:
	private static final String JSON_TAG_SUCCESS = "success";
	private static final String JSON_TAG_MESSAGE = "message";

	//
	private JSONParser jsonParser;

	public UploadFileFromURL(UploadFileCallback callback) {
		this.callback = callback;

		jsonParser = new JSONParser();
	}

	/**
	 * Before starting background thread Show Progress Dialog
	 * */
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		callback.onUploadFilePreExecute();
	}

	@Override
	protected String doInBackground(String... args) {
		// TODO Auto-generated method stub
		int success;
		String filePath = args[1];
		String fileName = args[2];
		File sourceFile = new File(filePath + fileName);

		if (!sourceFile.isFile()) {
			String strMessage = "Source File not exist :" + filePath + fileName;
			Log.e("uploadFile", strMessage);
			return strMessage;
		}

		JSONObject json = jsonParser.makeHttpRequest(args[0], sourceFile,
				fileName);
		if (json == null) {
			return "No data or connection failure";
		}
		try {
			Log.d(ACTIVITY_TAG, "Upload attempt : " + json.toString());
			success = json.getInt(JSON_TAG_SUCCESS);
			if (success == 1) {
				Log.d(ACTIVITY_TAG, "Upload Successful : " + json.toString());

				return json.getString(JSON_TAG_MESSAGE);
			} else {
				Log.d(ACTIVITY_TAG,
						"Upload Failure : " + json.getString(JSON_TAG_MESSAGE));
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
		callback.doUploadFilePostExecute(result);
	}

}
