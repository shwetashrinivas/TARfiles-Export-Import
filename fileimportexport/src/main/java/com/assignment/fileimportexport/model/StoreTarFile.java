package com.assignment.fileimportexport.model;

import org.springframework.stereotype.Component;

@Component
public class StoreTarFile {

    private String fileId;
	private String fileName;
    private String fileUri;
    
    public StoreTarFile(String fileId, String fileName, String fileUri) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.fileUri = fileUri;
	}

	public StoreTarFile() {
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

}
