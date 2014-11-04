package com.example.upload;

public interface UploadFileCallback {
	void onUploadFilePreExecute();

	void onUploadFileProgressUpdate(int value);

	void doUploadFilePostExecute(String result);
}
