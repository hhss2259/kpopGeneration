package kpop.kpopGeneration.file;

import lombok.Data;

@Data
public class S3UploadFile {
    /**
     * 같은 파일이름을 업로드하는 파일 이름이 충돌할 수도 있다.
     * 따라서 서버에서 파일을 저장할 때, 파일명이 겹치지 않도록 별도의 파일명을 사용해야한다
     */

    private String originalFilename;
    private String storeFileName;
    private String s3Url;


    public S3UploadFile(String originalFilename, String storeFileName,String s3Url) {
        this.originalFilename = originalFilename;
        this.storeFileName = storeFileName;
        this.s3Url = s3Url;
    }

}
