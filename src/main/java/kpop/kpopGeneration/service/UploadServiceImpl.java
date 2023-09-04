package kpop.kpopGeneration.service;

import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostImage;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.file.FileStore;
import kpop.kpopGeneration.file.S3FileStore;
import kpop.kpopGeneration.file.S3UploadFile;
import kpop.kpopGeneration.repository.PostImageRepository;
import kpop.kpopGeneration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UploadServiceImpl implements UploadService{

    private final S3FileStore s3FileStore;
    private final FileStore fileStore;
    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    /**
     *  PostImages Entity를 DB에 저장
     */
    @Transactional
    @Override
    public String saveImages(MultipartFile file) throws IOException {
        S3UploadFile uploaded = s3FileStore.saveImages(file);
        return uploaded.getS3Url();
    }


    /**
     *  S3에 이미지 파일 업로드
     */
    @Transactional
    @Override
    public String uploadS3(MultipartFile file) throws IOException {
        S3UploadFile uploaded = s3FileStore.uploadS3(file);
        return uploaded.getS3Url();
    }

    /**
     *  S3에서 이미지 파일 삭제
     */
    @Transactional
    @Override
    public void deleteS3(String src) throws IOException {
        s3FileStore.deleteS3(src);
    }

    /**
     *  Local에 이미지 파일 업로드
     */
    @Transactional
    @Override
    public String uploadLocal(MultipartFile file) throws IOException {
        String src = fileStore.storeFile(file);
        return src;
    }
    /**
     * Local에서 이미지 파일 삭제
     */
    @Transactional
    @Override
    public void deleteLocal(String src) throws IOException {
        fileStore.deleteFile(src);
    }

    @Override
    public List<String> findLocalSavedImages(Long postId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new NotExistedPostException());
        Optional<PostImage> thumbNail = postImageRepository.findThumbNail(post);
        if(thumbNail.isEmpty()){
            return null;
        }

        List<String> srcs = new ArrayList<>();
        PostImage postImage = thumbNail.get();
        srcs.add(postImage.getSrc()); //무조건 썸네일을 첫 번째에 담아야 한다.

        List<PostImage> notThumbNail = postImageRepository.findNotThumbNail(post); //썸네일 이외의 이미지들을 찾아온다
        notThumbNail.forEach( pi ->{
            srcs.add(pi.getSrc());
        });
        return srcs;
    }


}
