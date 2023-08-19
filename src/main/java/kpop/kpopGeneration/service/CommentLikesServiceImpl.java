package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.LikesViewDto;
import kpop.kpopGeneration.entity.*;
import kpop.kpopGeneration.exception.*;
import kpop.kpopGeneration.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentLikesServiceImpl implements CommentLikesService{

    private final CommentLikesRepository commentLikesRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 좋아요 상태보기
     */
    @Override
    public LikesViewDto getCommentLikes(Long id) {
        // DB에서 해당 포스트를 가지고 온다
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotExistedPostException());
        LikesViewDto likesViewDto =  new LikesViewDto(comment.getLikes(), false);
        return likesViewDto;
    }


    @Override
    public LikesViewDto getCommentLikes(Long id, String username) {
        // DB에서 해당 포스트를 가지고 온다
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotExistedPostException());
        // DB에서 해당 멤버를 가지고 온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());

        Optional<CommentLikes> byMember = commentLikesRepository.findByMemberAndComment(member, comment);
        LikesViewDto likesViewDto = null;
        if(byMember.isEmpty()) { // 아직 좋아요를 누르지 않았다면, isLiked 필드를 false로 설정한다
            likesViewDto = new LikesViewDto(comment.getLikes(), false);
        }else{ // 이미 좋아요를 눌렀다면, isLiked 필드를 true로 설정한다
            likesViewDto = new LikesViewDto(comment.getLikes(), true);
        }
        return likesViewDto;
    }

    /**
     * 게시글 좋아요 누르기
     */
    @Transactional
    @Override
    public LikesViewDto increaseLikes(Long id, String username) {
        // DB에서 해당 포스트를 가지고 온다
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotExistedPostException());
        // DB에서 해당 멤버를 가지고 온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());
        // DB에서 PostLikes 정보를 조회해온다
        Optional<CommentLikes> byMember = commentLikesRepository.findByMemberAndComment(member, comment);
        if(byMember.isPresent()){ // 이미 멤버가 좋아요를 누른 전적이 있으면 더 이상 좋아요를 누를 수 없다
            throw new DuplicateException("이미 좋아요를 누른 Member입니다");
        }

        // 포스트의 좋아요 갯수를 증가시킨다
        comment.increaseLikes();
        // PostLikes에 해당 memebr와 post를 저장한다
        commentLikesRepository.save(new CommentLikes(member, comment));
        return new LikesViewDto(comment.getLikes(), true);
    }

    /**
     * 게시글 좋아요 취소하기
     */
    @Transactional
    @Override
    public LikesViewDto decreaseLikes(Long id, String username) {
        // DB에서 해당 포스트를 가지고 온다
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new NotExistedPostException());
        // DB에서 해당 멤버를 가지고 온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());
        // DB에서 PostLikes 정보를 조회해온다
        CommentLikes commentLikes = commentLikesRepository.findByMemberAndComment(member, comment).orElseThrow(()->new NotExistedCommentException());

        // 포스트의 좋아요 갯수를 감소시킨다
        comment.decreaseLikes();
        // 해당 PostLikes를 삭제한다
        commentLikesRepository.delete(commentLikes);
        return new LikesViewDto(comment.getLikes(), false);
    }


}
