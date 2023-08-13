package kpop.kpopGeneration.file;

import lombok.Data;

@Data
public class UploadFile {
    /**
     * 같은 파일이름을 업로드하는 파일 이름이 충돌할 수도 있다.
     * 따라서 서버에서 파일을 저장할 때, 파일명이 겹치지 않도록 별도의 파일명을 사용해야한다
     */

    private String uploadFileName;
    private String storeFileName;

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }

}
