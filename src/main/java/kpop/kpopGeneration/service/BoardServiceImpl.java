package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.*;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.repository.BoardRepository;
import kpop.kpopGeneration.repository.CommentRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.View;
import java.util.Optional;

/**
 * 게시판의 CRUD 기능을 구현하고 있는 클래스입니다
 */
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    /**
     * 게시글 저장
     */
    @Override
    @Transactional
    public Long savePost(PostSaveDto postDto, String username) {
        Optional<Member> optional = memberRepository.findByUsername(username);
        if (optional.isEmpty()) {
            throw new NotExistedMemberException();
        }
        Member member = optional.get();

        Post post = new Post(postDto.getTitle(), postDto.getBody(), member, postDto.getCategory());
        Post savedPost = boardRepository.save(post);

        member.increasePostCnt();
        return savedPost.getId();
    }

    /**
     * 게시글 목록 조회하기
     */
    @Override
    public PageCustomDto<PostTitleViewDto> findPostListByCategory(Category category, Pageable pageable) {
        Page<Post> postList = null;
        if (category == Category.ALL) {
            postList = boardRepository.findALLPostList(pageable);
        }else{
            postList = boardRepository.findPostListByCategory(category, pageable);
        }

        Page<PostTitleViewDto> postTitleList = postList.map(
                post -> new PostTitleViewDto(
                        post.getId(), post.getCategory(), post.getTitle(),
                        post.getMember().getNickName(), post.getLastModifiedTime()
                )
        );
        PageCustomDto<PostTitleViewDto> postViewDto = getPageCustom_post(postTitleList);

        return postViewDto;
    }


    /**
     * 게시글 자세히 보기 + 댓글 목록 가져오기
     */
    @Override
    public PostDetailDto findPostById(Long id, Pageable commentPageable) {
        // post 가지고 오기
        Optional<Post> postById = boardRepository.findPostById(id);
        Post post = postById.orElseThrow(()->new NotExistedPostException());

        //  포스트에 달린 댓글들 가져오기
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(id, commentPageable);
        PageCustomDto<CommentViewDto> commentView = getPageCustom(commentListByPost);

        // 포스트 정보와 댓글의 정보를 결합하여 Dto로 리턴하기
        PostDetailDto postDetailDto = new PostDetailDto(post, commentView);
        return postDetailDto;
    }
    /**
     * 게시글 좋아요 누르기
     */

    /**
     * 게시글 수정
     */

    /**
     * 게시글 삭제
     */



    private PageCustomDto<CommentViewDto> getPageCustom(Page<CommentViewDto> list){
        PageCustomDto<CommentViewDto> dto = new PageCustomDto<>();

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

    private PageCustomDto<PostTitleViewDto> getPageCustom_post(Page<PostTitleViewDto> list){
        PageCustomDto<PostTitleViewDto> dto = new PageCustomDto<>();

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
