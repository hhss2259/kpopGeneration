package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.CommentSaveDto;
import kpop.kpopGeneration.dto.CommentViewDto;
import kpop.kpopGeneration.entity.Comment;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.exception.NotExistedCommentException;
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

import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Long saveComment(CommentSaveDto commentSaveDto, String username) {
        Optional<Post> optionalPost = boardRepository.findPostById(commentSaveDto.getPostId());
        Post post = optionalPost.orElseThrow(() -> new NotExistedPostException());

        Optional<Member> optionalMember = memberRepository.findByUsername(username);
        Member member = optionalMember.orElseThrow(() -> new NotExistedMemberException());

        Comment parentComment = null;
        if (commentSaveDto.getIsCommentForComment() == true && commentSaveDto.getParentCommentId() != null) {
            Optional<Comment> optionalComment = commentRepository.findById(commentSaveDto.getParentCommentId());
            parentComment = optionalComment.orElseThrow(() -> new NotExistedCommentException());
        }

        Comment comment = new Comment(commentSaveDto.getTextBody(), post, parentComment, member);
        Comment savedComment = commentRepository.save(comment);
        post.increaseCommentCnt();
        member.increaseCommentCnt();

        return savedComment.getId();
    }

    @Override
    public Page<CommentViewDto> findCommentListByPost(Long postId, Pageable pageable) {
        commentRepository.findCommentListByPost(postId, pageable);



        return null;
    }
}
