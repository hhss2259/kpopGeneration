package kpop.kpopGeneration.controller;


import kpop.kpopGeneration.dto.DefaultResponse;
import kpop.kpopGeneration.service.UploadService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
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
    public DefaultResponse uploadLocal(@RequestParam("file") MultipartFile file) throws IOException {
        String url = uploadService.uploadLocal(file);
        return DefaultResponse.res(20001, "업로드 성공", url);
    }

    @PostMapping("/api/temp/delete")
    public DefaultResponse deleteLocal(@RequestBody DeleteDto dto) throws IOException {
        uploadService.deleteLocal(dto.getSrc());
        return DefaultResponse.res(20001, "삭제 성공", dto.getSrc().substring(dto.getSrc().indexOf("/images/")));
    }


    @GetMapping("/api/temp/savedImages")
    public DefaultResponse getSavedImages(@RequestParam String id) throws IOException {
        Long postId = Long.parseLong(id);
        List<String> srcs = uploadService.findLocalSavedImages(postId);
        return DefaultResponse.res(20001, "업로드 성공", srcs);
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
