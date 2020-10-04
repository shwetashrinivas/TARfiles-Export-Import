package com.assignment.fileimportexport.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.assignment.fileimportexport.exceptions.FileAlreadyExistsException;
import com.assignment.fileimportexport.exceptions.FileNotFoundException;
import com.assignment.fileimportexport.model.StoreTarFile;
import com.assignment.fileimportexport.model.TarFile;
import com.assignment.fileimportexport.service.impl.FileServiceImplementation;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TarFileControllerTest {
    private MockMvc mockMvc;

    @Mock
    private FileServiceImplementation tarFileService;

    @InjectMocks
    private FileImportExportController tarFileController;
    private TarFile tarFile;
    private StoreTarFile storeTarFile;
    private List<StoreTarFile> fileList;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(tarFileController).build();
        tarFile = new TarFile();
        tarFile.setFileId("101");
        tarFile.setFileName("abc.tar.xz");
        fileList = new ArrayList<>();
    }

    @Test
    void givenTarfileToImportThenReturnStatusIsCreated() throws Exception {
        MockMultipartFile file = new MockMultipartFile("tarfile","abc.tar.xz", MediaType.MULTIPART_FORM_DATA_VALUE,"demo content".getBytes());
        when(tarFileService.importTarFile(any())).thenReturn(storeTarFile);
        mockMvc.perform(
                multipart("/tar/import?FileTitle=abc")
                        .file(file))
                .andExpect(status().isCreated());
    }
    
    @Test
    void givenTarfileToImportThenReturnStatusIsConflict() throws Exception {
        MockMultipartFile file = new MockMultipartFile("tarfile", "abc.tar.xz", MediaType.MULTIPART_FORM_DATA_VALUE, "demo content".getBytes());
        when(tarFileService.importTarFile(any())).thenThrow(FileAlreadyExistsException.class);
        mockMvc.perform(
                multipart("/tar/import?FileTitle=abc")
                        .file(file))
                .andExpect(status().isConflict());
    }

    @Test
    void givenInvalidFileThenReturnStatusIsNotFound() throws Exception {
        when(tarFileService.exportTarFile(any())).thenThrow(FileNotFoundException.class);
        mockMvc.perform(
                get("/tar/export/demo.tar"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testExportAllThenReturnStatusIsOk() throws Exception {
        when(tarFileService.exportAllTarFiles()).thenReturn(fileList);
        mockMvc.perform(
                get("/tar/export"))
                .andExpect(status().isOk());
    }


}