package com.assignment.fileimportexport.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.assignment.fileimportexport.model.TarFile;

@Repository
public interface FileRepository extends MongoRepository<TarFile, String> {

	public TarFile findByFileName(String fileName);
	
	public boolean existsByFileName(String fileName);
}
