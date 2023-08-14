package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.*;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostLikes;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.repository.PostLikesRepository;
import kpop.kpopGeneration.repository.PostRepository;
import kpop.kpopGeneration.repository.CommentRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Optional;

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
    private final PostLikesRepository postLikesRepository;

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
        Post post = new Post(postDto.getTitle(), postDto.getBody(), member, postDto.getCategory());
        Post savedPost = postRepository.save(post);

        // 멤버가 작성한 포스트 갯수를 증가시킨다
        member.increasePostCnt();
        return savedPost.getId();
    }

    /**
     * 게시글 목록 조회하기
     */
    @Override
    public PageCustomDto<PostTitleViewDto> findPostListByCategory(Category category, Pageable pageable) {
        Page<Post> postList = null;
        if (category == Category.ALL) { // 카테고리가 ALL인 경우 모든 포스트를 가지고 온다
            postList = postRepository.findALLPostList(pageable);
        }else{ // 지정된 카테고리가 있을 경우 카테고리별로 포스트를 조회해온다
            postList = postRepository.findPostListByCategory(category, pageable);
        }
        // 포스트 엔티티를 DTO로 변환한다
        Page<PostTitleViewDto> postTitleList = postList.map(
                post -> new PostTitleViewDto(
                        post.getId(), post.getCategory(), post.getTitle(),
                        post.getMember().getNickName(), post.getLastModifiedTime()
                )
        );
        // Page를 PageCustomDTO로 변환한 후 필요한 정보들을 추가적으로 담는다
        PageCustomDto<PostTitleViewDto> postViewDto = getPageCustom(postTitleList);
        postViewDto.setCurrent((int)(pageable.getPageNumber()+1));
        postViewDto.setCategory(category);
        return postViewDto;
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
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(id, commentPageable);
        PageCustomDto<CommentViewDto> commentView = getPageCustom(commentListByPost); // Page를 PageCustomDto로 변환한다
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
    public Long updatePost(Long id,  PostSaveDto postSaveDto) {
        // DB에서 Post를 찾아온다
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());
        /**
         * 변경 감지를 통해 post의 내용을 수정한다
         * 1. 제목
         * 2. 본문
         * 3. 카테고리
         */
        post.updatePost(postSaveDto);
        return post.getId();
    }

    /**
     * 게시글 삭제
     */
    @Override
    public Long deletePost(Long id, String username) {
        // DB에서 Post를 찾아온다
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());

        // DB에서 Member를 찾아온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());

        /**
         * deletedTrue = true();
         * deletedTime = LocalDateTime.now();
         */
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

}
