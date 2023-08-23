package kpop.kpopGeneration.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadService {
    /**
     *  PostImages Entity를 DB에 저장
     */
    String saveImages(MultipartFile file) throws IOException;

    /**
     *  S3에 이미지 파일 업로드 
     */
    String uploadS3(MultipartFile file) throws IOException;

    /**
     *  S3에서 이미지 파일 삭제
     */
    void deleteS3(String src) throws IOException;

    /**
     *  Local에 이미지 파일 업로드
     */
    String uploadLocal(MultipartFile file) throws IOException;

    /**
     * Local에서 이미지 파일 삭제
     */
    void deleteLocal(String src) throws IOException;

    List<String> findLocalSavedImages(Long postId);
}
