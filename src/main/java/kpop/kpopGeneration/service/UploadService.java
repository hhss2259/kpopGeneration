package kpop.kpopGeneration.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {
    String upload(MultipartFile file)  throws IOException;

    String uploadTempFile(MultipartFile file)  throws IOException;

    void deleteTempFile(String src) throws IOException;

    String uploadLocal(MultipartFile file)  throws IOException;

    void deleteLocal(String src) throws IOException;
}
