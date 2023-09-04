package kpop.kpopGeneration.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional
public class S3FileStore {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private  String bucket;
    final String tempFolder = "temp/";

    public S3UploadFile saveImages(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename(); //파일의 원래 이름
        String storeFilename = createStoreFilename(originalFilename); // 파일의 저장 이름


        //실제 s3에 파일을 저장하는 과정
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, storeFilename, file.getInputStream(), metadata);

        String s3Url = amazonS3Client.getUrl(bucket, storeFilename).toString();
        S3UploadFile s3UploadFile = new S3UploadFile(originalFilename, storeFilename, s3Url);

        return s3UploadFile;
    }

    public S3UploadFile uploadS3(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename(); //파일의 원래 이름
        String storeFilename = tempFolder+createStoreFilename(originalFilename); // 파일의 저장 이름

        //실제 s3에 파일을 저장하는 과정
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        amazonS3Client.putObject(bucket, storeFilename, file.getInputStream(), metadata);

        String s3Url = amazonS3Client.getUrl(bucket, storeFilename).toString();
        S3UploadFile s3UploadFile = new S3UploadFile(originalFilename, storeFilename, s3Url);

        return s3UploadFile;
    }

    public void deleteS3(String src) throws IOException {

        int index = src.lastIndexOf("temp/");
        String key = src.substring(index);

        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucket, key);
        amazonS3Client.deleteObject(deleteObjectRequest);

    }





    /**
     *  파일 이름을 uuid로 바꾸어 겹치지 않도록 해준다
     */
    private String createStoreFilename(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid+"."+ext;
    }

    /**
     * 파일 이름을 uuid로 변경 시  확장자를 유지할 수 있도록, 확장자를 따로 추출해준다.
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
