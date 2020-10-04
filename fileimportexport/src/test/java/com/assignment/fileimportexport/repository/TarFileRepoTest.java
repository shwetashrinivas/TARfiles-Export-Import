package com.assignment.fileimportexport.repository;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.assignment.fileimportexport.model.TarFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TarFileRepoTest {
	
    @Autowired
    private FileRepository tarFileRepo;
    private TarFile tarFile;

    @BeforeEach
    void setUp() {
        tarFile = new TarFile();
        tarFile.setFileId("101");
        tarFile.setFileName("xyz.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Some content inside file!!".getBytes()));
    }


    @AfterEach
    void tearDown() {
        this.tarFileRepo.deleteAll();
        tarFile = null;
    }

    @Test
    public void testTarFileToImportThenReturnTarFile() {
        tarFileRepo.save(tarFile);
        TarFile savedFile = tarFileRepo.findById(tarFile.getFileId()).get();
        assertEquals("101", savedFile.getFileId());
    }

    @Test
    public void testExportAllThenReturnListOfAllTarFiles() {
    	TarFile tarFileOne = new TarFile();
        tarFileOne.setFileId("102");
        tarFileOne.setFileName("abc.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Some other content in file!!".getBytes()));
        tarFileRepo.save(tarFile);
        tarFileRepo.save(tarFileOne);
        List<TarFile> tarFileList = tarFileRepo.findAll();
        assertEquals("102", tarFileList.get(1).getFileId());
    }

    @Test
    public void testFileNameThenReturnTarFile() {
        tarFileRepo.save(tarFile);
        TarFile retrievedTarFile = tarFileRepo.findByFileName(tarFile.getFileName());
        assertEquals(tarFile.getFileId(), retrievedTarFile.getFileId());
    }

}