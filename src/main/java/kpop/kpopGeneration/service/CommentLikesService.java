package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.LikesViewDto;

public interface CommentLikesService {

    /**
     * 댓글 좋아요 상태보기
     */
    LikesViewDto getCommentLikes(Long id);
    LikesViewDto getCommentLikes(Long id, String Username);
    /**
     * 댓글 좋아요 누르기
     */
    LikesViewDto increaseLikes(Long id, String username);

    /**
     * 댓글 좋아요 취소하기
     */
    LikesViewDto decreaseLikes(Long id, String username);

}
