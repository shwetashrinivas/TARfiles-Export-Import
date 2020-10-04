package com.assignment.fileimportexport.service;

import java.util.List;

import com.assignment.fileimportexport.exceptions.FileAlreadyExistsException;
import com.assignment.fileimportexport.exceptions.FileNotFoundException;
import com.assignment.fileimportexport.model.StoreTarFile;
import com.assignment.fileimportexport.model.TarFile;

public interface FileService {
	
	public StoreTarFile importTarFile(TarFile filedetails) throws FileAlreadyExistsException;

	public TarFile exportTarFile(String fileName) throws FileNotFoundException;

	public List<StoreTarFile> exportAllTarFiles();
 
}
