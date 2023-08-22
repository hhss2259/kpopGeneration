package kpop.kpopGeneration.service;

import kpop.kpopGeneration.file.S3FileStore;
import kpop.kpopGeneration.file.S3UploadFile;
import kpop.kpopGeneration.file.UploadFile;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UploadServiceImpl implements UploadService{

    private final S3FileStore s3FileStore;

    @Transactional
    @Override
    public String upload(MultipartFile file) throws IOException {
        S3UploadFile uploaded = s3FileStore.upload(file);
        return uploaded.getS3Url();
    }

    @Override
    public String uploadTempFile(MultipartFile file) throws IOException {
        S3UploadFile uploaded = s3FileStore.uploadTempFile(file);
        return uploaded.getS3Url();
    }

    @Override
    public void deleteTempFile(String src) throws IOException {
        s3FileStore.deleteTempFile(src);
    }


}
