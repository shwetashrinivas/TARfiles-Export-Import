package com.assignment.fileimportexport.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.assignment.fileimportexport.exceptions.FileAlreadyExistsException;
import com.assignment.fileimportexport.exceptions.FileNotFoundException;
import com.assignment.fileimportexport.model.StoreTarFile;
import com.assignment.fileimportexport.model.TarFile;
import com.assignment.fileimportexport.repository.FileRepository;
import com.assignment.fileimportexport.service.FileService;

@Service
public class FileServiceImplementation implements FileService {

	private FileRepository filerepo;

	@Autowired
	public FileServiceImplementation(FileRepository filerepo) {
		this.filerepo = filerepo;
	}

	@Override
	public StoreTarFile importTarFile(TarFile tarFile) throws FileAlreadyExistsException {
		if (tarFile.getFileName().matches("([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.tar|.tar.xz)$")) 
			if (filerepo.findByFileName(tarFile.getFileName()) == null) {
				TarFile savedFile = filerepo.save(tarFile);
				StoreTarFile storeTarFile = new StoreTarFile();
				storeTarFile.setFileId(savedFile.getFileId());
				storeTarFile.setFileName(savedFile.getFileName());
				storeTarFile.setFileUri(ServletUriComponentsBuilder.fromCurrentContextPath().path("/tar/import/")
						.path(savedFile.getFileName()).toUriString());
				return storeTarFile;
			} else 
				throw new FileAlreadyExistsException("A TAR File with the same name already exists");
		return null;
	}

	@Override
	public TarFile exportTarFile(String fileName) throws FileNotFoundException {
		TarFile tarFile = filerepo.findByFileName(fileName);
		if (tarFile != null) {
			return tarFile;
		} else {
			throw new FileNotFoundException("This TAR file doesn't exist. Please check again!");
		}
	}

	@Override
	public List<StoreTarFile> exportAllTarFiles() {
		List<StoreTarFile> tarFileList = new ArrayList<>();
		List<TarFile> tarFiles = filerepo.findAll();
		for (TarFile tarFile : tarFiles) {
			StoreTarFile storeTarFile = new StoreTarFile();
			storeTarFile.setFileId(tarFile.getFileId());
			storeTarFile.setFileName(tarFile.getFileName());
			storeTarFile.setFileUri(ServletUriComponentsBuilder.fromCurrentContextPath().path("/tar/import/")
					.path(tarFile.getFileName()).toUriString());
			tarFileList.add(storeTarFile);
		}
		return tarFileList;
	}

}