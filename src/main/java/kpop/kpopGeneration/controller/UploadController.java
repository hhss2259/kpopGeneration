package kpop.kpopGeneration.controller;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3AccelerateUnsupported;
import kpop.kpopGeneration.file.S3FileStore;
import kpop.kpopGeneration.file.UploadFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final S3FileStore s3FileStore;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

//    @PostMapping("/temp/upload")
//    public ResponseEntity<UploadFile> uploadTempFile(@RequestParam("file") MultipartFile file) throws IOException{
//        UploadFile upload = s3FileStore.tempUpload(file, 10L);
//        return ResponseEntity.ok(upload);
//    }

    @PostMapping("/upload")
    public ResponseEntity<UploadFile> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        UploadFile upload = s3FileStore.upload(file, 10L);

        return ResponseEntity.ok(upload);
    }

    @PostMapping("/test/upload")
    public ResponseEntity<String> uploadFileTest(@RequestParam("file") MultipartFile file) {
        try {
            String fileName="upload/" +file.getOriginalFilename();
            String fileUrl= "https://" + bucket + "/upload/" +fileName;

            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(),metadata);

            return ResponseEntity.ok(fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }





}
