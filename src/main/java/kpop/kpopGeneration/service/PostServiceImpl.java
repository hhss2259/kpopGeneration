package kpop.kpopGeneration.service;

import antlr.collections.impl.IntRange;
import kpop.kpopGeneration.dto.*;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostImage;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.file.FileStore;
import kpop.kpopGeneration.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 게시판의 CRUD 기능을 구현하고 있는 클래스입니다
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostImageRepository postImageRepository;
    private final FileStore fileStore;

    /**
     * 게시글 저장
     */
    @Override
    @Transactional
    public Long savePost(PostSaveDto postDto, String username) {
        // DB에 저장되어 있는 멤버 정보를 가지고 온다
        Optional<Member> optional = memberRepository.findByUsername(username);
        Member member = optional.orElseThrow(() -> new NotExistedPostException());

        // 포스트를 저장한다
        Post savedPost = postRepository.save(new Post(postDto.getTitle(), postDto.getBody(), member, postDto.getCategory()));

        // 포스트의 이미지들을 저장한다.
        List<String> images = postDto.getImages();
        if (images != null) {
            for (int i = 0; i < images.size(); i++) {
                PostImage postImage = new PostImage(savedPost, images.get(i));
                if(i == 0){
                    postImage.changeThumbnail(true);
                }
                postImageRepository.save(postImage);
            }
        }

        // 멤버가 작성한 포스트 갯수를 증가시킨다
        member.increasePostCnt();

        return savedPost.getId();
    }
    @Override
    public PageCustomDto<PostTitleViewDto> findNewsListByCategory(Category category, Pageable pageable) {
        Page<Post> postList = postRepository.findPostListByCategory(category, pageable);
        // 포스트 엔티티를 DTO로 변환한다
        Page<PostTitleViewDto> postTitleList = postList.map(
                post -> {
                    PostImage thumbNail = postImageRepository.findThumbNail(post).orElse(null);
                    String thumbNailSrc = null;
                    if(thumbNail != null){
                        thumbNailSrc = thumbNail.getSrc();
                    }
                    return new PostTitleViewDto(
                        post.getId(), post.getCategory(), post.getTitle(),
                        post.getMember().getNickName(), post.getLastModifiedTime(),
                        post.getLikes(), post.getCommentCnt(),thumbNailSrc
                );
            }
        );
        // Page를 PageCustomDTO로 변환한 후 필요한 정보들을 추가적으로 담는다
        PageCustomDto<PostTitleViewDto> postViewDto = getPageCustom(postTitleList);
        postViewDto.setCurrent((int)(pageable.getPageNumber()+1));
        postViewDto.setCategory(category);
        return postViewDto;
    }

    /**
     * 게시글 목록 조회하기
     */
    @Override
    public PageCustomDto<PostTitleViewDto> findPostListByCategory(Category category, Pageable pageable) {
        Page<Post> postList = null;

        // 포스트 리스트를 가지고 온다
        if (category == Category.ALL) {
            postList = postRepository.findALLPostList(pageable);
        }else{
            postList = postRepository.findPostListByCategory(category, pageable);
        }
        // 포스트 엔티티를 DTO로 변환한다
        Page<PostTitleViewDto> postTitleList = postList.map(
                post -> new PostTitleViewDto(
                        post.getId(), post.getCategory(), post.getTitle(),
                        post.getMember().getNickName(), post.getLastModifiedTime(),
                        post.getLikes(), post.getCommentCnt()
                )
        );
        // Page를 PageCustomDTO로 변환한 후 필요한 정보들을 추가적으로 담는다
        PageCustomDto<PostTitleViewDto> postViewDto = getPageCustom(postTitleList);
        postViewDto.setCurrent((int)(pageable.getPageNumber()+1));
        postViewDto.setCategory(category);
        return postViewDto;
    }

    /**
     * 포스트 원작자 찾아오기
     */
    @Override
    public String findWriter(Long id) {
        return postRepository.findWriter(id);
    }

    /**
     * 포스트 업데이트 정보 가져오기
     */
    @Override
    public PostUpdateDto findPostUpdateDto(Long id) {
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());
        return new PostUpdateDto(post.getId(), post.getMember().getUsername(), post.getTitle(), post.getBody(), post.getCategory());
    }

    /**
     * 게시글 자세히 보기 + 댓글 목록 가져오기 + 작성자의 최신글 가져오기
     */
    @Override
    public PostDetailDto findPostById(Long id, Pageable commentPageable) {
        
        // DB에 저장된 post 가지고 오기
        Optional<Post> postById = postRepository.findPostById(id);
        Post post = postById.orElseThrow(()->new NotExistedPostException());

        //  포스트에 달린 댓글들 가져오기
        Page<Comment> pureList = commentRepository.findPureCommentListByPost(id, commentPageable);
        Page<PureCommentViewDto> commentList = getCommentViewDtoPage(pureList);
        PageCustomDto<PureCommentViewDto> commentView = getPageCustom(commentList); // Page를 PageCustomDto로 변환한다
        commentView.setCurrent((int)(commentPageable.getPageNumber()+1)); // PageCustomDto에 필요한 정보를 담는다

        // 작성자의 최신글 가져오기
        Page<RecentPostByMemberDto> recent = postRepository.findRecentPostListByMember(post.getMember(), PageRequest.of(0, 5), id);
        PageCustomDto<RecentPostByMemberDto> recentView = getPageCustom(recent); // Page를 PageCustomDto로 변환한다

        // 포스트 정보와 댓글의 정보를 결합하여 Dto로 리턴하기
        PostDetailDto postDetailDto = new PostDetailDto(post, commentView, recentView);

        return postDetailDto;
    }


    /**
     * 게시글 조회수 늘리기
     */
    @Transactional
    @Override
    public void increaseViews(Long id) {
        // DB에서 포스트를 찾아온다.
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());
        post.increaseViews();
    }

    /**
     * 게시글 수정
     */
    @Override
    @Transactional
    public Long updatePost(Long id,  PostSaveDto postSaveDto) {
        // DB에서 Post를 찾아온다
        Post savedPost = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());

         // 변경 감지를 통해 post의 내용을 수정한다(제목, 본문, 카테고리)
        savedPost.updatePost(postSaveDto);

        
        // 수정된 포스트에 이미지가 존재하지 않으면, 기존에 저장해두었던 모든 이미지 파일과 DB의 데이터를 삭제한다.
        List<String> newSrc = postSaveDto.getImages();
        List<PostImage> savedImages = postImageRepository.findAllPostImage(savedPost);
        if (newSrc == null || newSrc.size() == 0) {
            postImageRepository.deleteAllPostImage(savedPost); // DB에 해당 포스트의 모든 이미지 정보들을 삭제
            savedImages.forEach( savedInfo ->{
                fileStore.deleteFile(savedInfo.getSrc()); // 외부 디렉토리에서 해당 포스트의 이미지 파일들 모두 삭제
            });
            return savedPost.getId();
        }
        // 수정된 포스트에 이미지가 존재한다면
        // 1. 수정되기 전에 저장됐던 이미지 리스트와 수정된 후에 존재할 이미지 리스트를 비교한다
        // 2. 수정된 후에도 유지되어야 할 이미지들을 제외하고, 삭제되어야 할 이미지들을 삭제한다 
        // 3. 새롭게 등장한 이미지 데이터를 DB에 저장해야 된다(이미지 파일 자체는 외부 디렉토리에 먼저 저장되어 있는 상태)
        // 4. 썸네일을 변경해준다  1) 썸네일 유지되거나 2) 기존 썸네일이 새로운 썸내일로 바뀌거나 3) 아예 새로운 썸내일 생기거나

        Map<String, Boolean> map = new HashMap<>(); // 유지될 이미지들과 삭제될 이미지들을 비교
        List<String> survival = new ArrayList<>(); // 유지될 이미지들
        savedImages.forEach( savedInfo ->{
            map.put(savedInfo.getSrc(), false);
        });
        newSrc.forEach( image ->{
            map.put(image, true); // 중첩되는 이미지들(유지될 이미지들)의 value는 true로 바꾼다.
        });
        map.keySet().forEach( src ->{
            if(map.get(src) == true){ // 중첩되는 이미지들은 survival에 담는다
                survival.add(src);
            }else{
                postImageRepository.deletePostImageBySrc(src); // 중첩되지 않은 이미지들을 수정 과정에서 삭제된 이미지들이므로 삭제해주어야 한다.
                fileStore.deleteFile(src);
            }
        });

        List<String> savedSrc = new ArrayList<>();
        savedImages.forEach( savedImage ->{
            savedSrc.add(savedImage.getSrc());
        });
        newSrc.forEach( src ->{
            if(!savedSrc.contains(src)){ // 새롭게 추가된 이미지들  정보를 DB에 저장한다.
                postImageRepository.save( new PostImage(savedPost, src));
            }
        });

        Optional<PostImage> thumbNail = postImageRepository.findThumbNail(savedPost);
        if(thumbNail.isPresent()){
            PostImage oldThumbnail = thumbNail.get();
            if(!oldThumbnail.getSrc().equals(newSrc.get(0))){
                oldThumbnail.changeThumbnail(false);
                PostImage newThumbnail = postImageRepository.findPostImageBySrc(newSrc.get(0), savedPost).get();
                newThumbnail.changeThumbnail(true);
            }
        }else{
            PostImage newThumbnail = postImageRepository.findPostImageBySrc(newSrc.get(0),savedPost).get();
            newThumbnail.changeThumbnail(true);
        }
        return savedPost.getId();
    }

    /**
     * 게시글 삭제
     */
    @Override
    @Transactional
    public Long deletePost(Long id, String username) {
        // DB에서 Post를 찾아온다
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());

        // DB에서 Member를 찾아온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());

         //deletedTrue = true(),  deletedTime = LocalDateTime.now();
        post.deletePost();

        // 멤버가 작성한 포스트 갯수를 감소시킨다.
        member.decreasePostCnt();
        return post.getId();
    }



    /**
     *  Page 자체에 방대한 정보가 담겨 있으므로 Page 객체 전체를 return 하기 보다
     *  필요한 정보만을 추려서 PageCustomDto에 옮겨 담은 후, PageCustomDto를 return 한다
     */
    private <T> PageCustomDto<T> getPageCustom(Page<T> list){
        PageCustomDto<T> dto = new PageCustomDto<>();

        dto.setContent(list.getContent());
        dto.setSize(list.getSize());
        dto.setNumberOfElements(list.getNumberOfElements());
        dto.setTotalElements(list.getTotalElements());
        dto.setTotalPages(list.getTotalPages());
        dto.setHasNext(list.hasNext());
        dto.setHasPrevious(list.hasPrevious());
        dto.setIsFirst(list.isFirst());
        dto.setIsLast(list.isLast());
        dto.setNextPageable(list.nextPageable());
        dto.setPreviousPageable(list.previousPageable());
        return dto;
    }

    private Page<PureCommentViewDto> getCommentViewDtoPage(Page<Comment> pureCommentListByPost){
        List<Comment> content = pureCommentListByPost.getContent();
        List<PureCommentViewDto> pureCommentViewDtoList = new ArrayList<>();
        content.forEach((comment) ->{
            Long commentId = comment.getId();
            String nickname = comment.getMember().getNickName();
            Long memberId = comment.getMember().getId();
            String textBody = comment.getTextBody();
            Long likes = comment.getLikes();
            Long postId = comment.getParentPost().getId();
            Long parentCommentId = null;
            if(comment.getParentComment() != null){
                parentCommentId = comment.getParentComment().getId();
            }
            Boolean isCommentForComment = comment.getIsCommentForComment();
            LocalDateTime lastModifiedTime = comment.getLastModifiedTime();
            Integer depth = comment.getDepth();
            String parentNickname = null;

            if(comment.getParentComment() != null){
                parentNickname = comment.getParentComment().getMember().getNickName();
            }

            Boolean deletedTrue = false;
            if(comment.getDeletedTrue()  == true ){
                deletedTrue = true;
            }

            PureCommentViewDto pureCommentViewDto =
                    new PureCommentViewDto(commentId, nickname, memberId, textBody, likes, postId,parentCommentId,isCommentForComment, lastModifiedTime, depth, parentNickname, deletedTrue);
            pureCommentViewDtoList.add(pureCommentViewDto);
        });

       return new PageImpl<PureCommentViewDto>(pureCommentViewDtoList, pureCommentListByPost.getPageable(), pureCommentListByPost.getTotalElements());
    }

}
