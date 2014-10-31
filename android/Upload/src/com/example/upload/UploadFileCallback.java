package com.example.upload;

public interface UploadFileCallback {
	void onUploadFilePreExecute();

	void doUploadFilePostExecute(String result);
}
