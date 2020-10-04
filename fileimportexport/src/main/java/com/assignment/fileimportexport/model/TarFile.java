package com.assignment.fileimportexport.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tarfiles_storage")
public class TarFile {
	
    @Id
    private String fileId;
    private String fileName;
    private Binary file;
    
    public TarFile() {}
    
	public TarFile(String fileId, String fileName, Binary file) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.file = file;
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

	public Binary getFile() {
		return file;
	}

	public void setFile(Binary file) {
		this.file = file;
	}
       
}
