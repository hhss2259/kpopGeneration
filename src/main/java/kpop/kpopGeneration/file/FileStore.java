package kpop.kpopGeneration.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 멀티파트 파일을 서버에 저장하는 역할을 한다
 */

@Component
public class FileStore {

    private String fileDir="/Users/hs/Desktop/study/";

    /**
     * 파일의 저장 위치를 설정한다
     */
    public String getPullPath(String filename){
        return fileDir + filename;
    }


    /**
     * 멀티파트 파일이 여러 개 저장할 때 사용한다
     */
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException{
        List<UploadFile> storeFileResult = new ArrayList<>();

        for(MultipartFile multipartFile : multipartFiles){
            if(!multipartFile.isEmpty()){
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    /**
     * 한 개의 멀티파티 파일을 저장할 때 사용
     */
    public UploadFile storeFile(MultipartFile multipartFile)throws IOException{
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getPullPath(storeFileName)));
        return new UploadFile(originalFilename, storeFileName);
    }

    /**
     * UUID를 사용해 파일의 이름이 겹치지 않게 만들어준다
     * 원본 파일의 확장자를 그대로 유지하고 사용할 수 있게 해준다
     */
    private String createStoreFilename(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid+"."+ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
