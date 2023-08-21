package kpop.kpopGeneration.controller;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3AccelerateUnsupported;
import com.amazonaws.services.s3.transfer.Upload;
import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.file.S3FileStore;
import kpop.kpopGeneration.file.UploadFile;
import kpop.kpopGeneration.service.UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    public final UploadService uploadService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @PostMapping("/upload")
    public DefaultResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = uploadService.upload(file);
        return DefaultResponse.res(20001, "업로드 성공", url);
    }


}
