package com.assignment.fileimportexport.service;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.assignment.fileimportexport.exceptions.FileAlreadyExistsException;
import com.assignment.fileimportexport.exceptions.FileNotFoundException;
import com.assignment.fileimportexport.model.StoreTarFile;
import com.assignment.fileimportexport.model.TarFile;
import com.assignment.fileimportexport.repository.FileRepository;
import com.assignment.fileimportexport.service.impl.FileServiceImplementation;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TarFileServiceTest {
    @Mock
    private FileRepository tarFileRepo;

    @InjectMocks
    private FileServiceImplementation tarFileService;
    private TarFile tarFile;
    private List<TarFile> fileList = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        tarFile = new TarFile();
        tarFile.setFileId("101");
        tarFile.setFileName("abc.tar.xz");
        tarFile.setFile(new Binary(BsonBinarySubType.BINARY, "Some other content in file!!".getBytes()));
        fileList.add(tarFile);
        HttpServletRequest mockRequest = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }

    @Test
    void testTarFileThenImportTarFile() throws FileAlreadyExistsException{
        when(tarFileRepo.save(any())).thenReturn(tarFile);
        StoreTarFile savedTarFile = tarFileService.importTarFile(tarFile);
        assertEquals(tarFile.getFileId(), savedTarFile.getFileId());
        verify(tarFileRepo, times(1)).save(any());
    }

    @Test
    public void testSameFileNameToThrowException() {
        when(tarFileRepo.findByFileName(tarFile.getFileName())).thenReturn(tarFile);
        Assertions.assertThrows(FileAlreadyExistsException.class, () ->
                tarFileService.importTarFile(tarFile));
        verify(tarFileRepo, times(1)).findByFileName(tarFile.getFileName());
    }

    @Test
    void testExportAllThenReturnListOfAllTarFiles() {
        tarFileRepo.save(tarFile);
        //stubbing the mock to return specific data
        when(tarFileRepo.findAll()).thenReturn(fileList);
        List<StoreTarFile> tarFiles = tarFileService.exportAllTarFiles();
        assertEquals(fileList.get(0).getFileId(), tarFiles.get(0).getFileId());
        verify(tarFileRepo, times(1)).save(tarFile);
        verify(tarFileRepo, times(1)).findAll();
    }

    @Test
    public void givenFileNameThenReturnTarFile() throws FileNotFoundException {
        when(tarFileRepo.findByFileName(anyString())).thenReturn(tarFile);
        TarFile retrievedTarFile = tarFileService.exportTarFile(tarFile.getFileName());
        assertEquals(tarFile.getFileId(), retrievedTarFile.getFileId());
        verify(tarFileRepo, times(1)).findByFileName(anyString());

    }

    @Test
    void givenInvalidFileNameThenThrowException() {
        when(tarFileRepo.findByFileName(tarFile.getFileName())).thenReturn(null);
        Assertions.assertThrows(FileNotFoundException.class, () ->
                tarFileService.exportTarFile("abc.tar.xz"));
        verify(tarFileRepo, times(1)).findByFileName(tarFile.getFileName());
    }

}