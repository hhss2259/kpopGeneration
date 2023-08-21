package kpop.kpopGeneration.file;

import jdk.jfr.StackTrace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class S3FileStoreTest {

//    @Test
//    @DisplayName("현재 파일 경로 찾기")
//    void path(){
//        String path = this.getClass().getResource("/post.html").getPath();
//        System.out.println("path = " + path);
//
//    }
}