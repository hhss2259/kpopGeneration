package kpop.kpopGeneration.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 멀티파트 파일을 Local에 저장한다
 */
@Component
public class FileStore {
    @Value("${custom.path.upload-images}")
    private String fileDir;

    // 파일의 저장 위치를 지정한다
    public String getLocalSavePath(String filename){
        return fileDir + filename;
    }
    
    // 브라우저에서 파일을 요청할 때 필요한 url을 제공한다
    public String getViewPath(String filename){  return "/images/"+filename;}


    public String storeFile(MultipartFile multipartFile)throws IOException{
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFilename(originalFilename);
        multipartFile.transferTo(new File(getLocalSavePath(storeFileName))); // 로컬 디렉토리에 파일을 저장한다
        return  getViewPath(storeFileName); // 이미지
    }


    public void deleteFile(String src) {
        int index = src.lastIndexOf("/")+1;
        String storeFileName = src.substring(index);

        Path filePath = Paths.get(getLocalSavePath(storeFileName));
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
