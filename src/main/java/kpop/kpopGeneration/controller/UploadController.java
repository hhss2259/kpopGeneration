package kpop.kpopGeneration.controller;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3AccelerateUnsupported;
import com.amazonaws.services.s3.transfer.Upload;
import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.file.S3FileStore;
import kpop.kpopGeneration.file.UploadFile;
import kpop.kpopGeneration.service.UploadService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    public final UploadService uploadService;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    @PostMapping("/api/temp/upload")
    public DefaultResponse uploadTempFile(@RequestParam("file") MultipartFile file) throws IOException {
        String url = uploadService.uploadTempFile(file);
        return DefaultResponse.res(20001, "업로드 성공", url);
    }

    @PostMapping("/api/temp/delete")
    public DefaultResponse deleteTempFile(@RequestBody DeleteDto dto) throws IOException {
        uploadService.deleteTempFile(dto.getSrc());
        return DefaultResponse.res(20001, "삭제 성공", dto.getSrc());
    }

    @Data
    static class DeleteDto {
        String src;
    }

    @PostMapping("/api/upload")
    public DefaultResponse upload(@RequestBody List<String> src) throws IOException {
        src.forEach(s -> System.out.println(s));
        return DefaultResponse.res(20001, "삭제 성공","good");
    }



}
