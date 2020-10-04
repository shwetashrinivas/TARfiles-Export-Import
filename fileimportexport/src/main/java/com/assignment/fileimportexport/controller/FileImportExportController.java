package com.assignment.fileimportexport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;

import com.assignment.fileimportexport.exceptions.FileAlreadyExistsException;
import com.assignment.fileimportexport.exceptions.FileNotFoundException;
import com.assignment.fileimportexport.model.TarFile;
import com.assignment.fileimportexport.service.FileService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tar")
public class FileImportExportController {

	private FileService fileService;

	@Autowired
	public FileImportExportController(FileService fileService) {
		this.fileService = fileService;
	}

	@PostMapping("/import")
	public ResponseEntity<?> importFile(@RequestParam("tarfile") MultipartFile tarFile) {
		try {
			TarFile tarFileOne = new TarFile();
			tarFileOne.setFile(new Binary(BsonBinarySubType.BINARY, tarFile.getBytes()));
			String fileName = StringUtils.cleanPath(tarFile.getOriginalFilename());
			tarFileOne.setFileName(fileName);
			return new ResponseEntity<>(fileService.importTarFile(tarFileOne), HttpStatus.CREATED);
		} catch (FileAlreadyExistsException tarFileAlreadyExistsException) {
			return new ResponseEntity<>(tarFileAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception exception) {
			return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@GetMapping("/export/{FileTitle:.+}")
	public ResponseEntity<?> exportFile(@PathVariable String FileTitle) {
		try {
			TarFile tarFile = fileService.exportTarFile(FileTitle);
			return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/tar+gzip"))
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + tarFile.getFileName() + "\"")
					.body(new ByteArrayResource(tarFile.getFile().getData()));
		} catch (FileNotFoundException fnfe) {
			return new ResponseEntity<>(fnfe.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/export")
	public ResponseEntity<?> listAllTarFiles() {
		try {
			return new ResponseEntity<>(fileService.exportAllTarFiles(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

}
