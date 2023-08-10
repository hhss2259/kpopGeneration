package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.exception.NotExistedCommentException;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.repository.PostRepository;
import kpop.kpopGeneration.repository.CommentRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 포스트에 댓글 달기
     */
    @Override
    @Transactional
    public Long saveComment(CommentSaveDto commentSaveDto, String username) {
        // 댓글이 달린 post 가져오기
        Optional<Post> optionalPost = postRepository.findPostById(commentSaveDto.getPostId());
        Post post = optionalPost.orElseThrow(() -> new NotExistedPostException());

        // 댓글을 작성한 member 가져오기
        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new NotExistedMemberException());


        Comment parentComment = null;
        // 댓글(댓글에 달린 댓글)이면 부모 댓글의 정보를 가지고 온다.
        if (commentSaveDto.getIsCommentForComment() == true && commentSaveDto.getParentCommentId() != null) {
            Optional<Comment> optionalComment = commentRepository.findById(commentSaveDto.getParentCommentId());
            parentComment = optionalComment.orElseThrow(() -> new NotExistedCommentException());
        }

        // 새로운 댓글을 만든다 => 이때 부모 댓글이 null 아니면, isCommentForComment 필드가 true가 되며 depth 필드는 부모 댓글의 depth+1이 된다
        Comment comment = new Comment(commentSaveDto.getTextBody(), post, parentComment, member);
        Comment savedComment = commentRepository.save(comment);

        // 게시글의 댓글 갯수를 1개 증가시킨다
        post.increaseCommentCnt();

        // 작성자의 댓글 갯수를 1개 증가시킨다
        member.increaseCommentCnt();

        return savedComment.getId();
    }

    /**
     * 댓글 목록 조회하기 (Ajax 사용 시)
     */
    @Override
    public Page<CommentViewDto> findCommentListByPost(Long postId, Pageable pageable) {
        Page<CommentViewDto> commentListByPost = commentRepository.findCommentListByPost(postId, pageable);
        return null;
    }
}
