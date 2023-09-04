package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.LikesViewDto;

public interface PostLikesService {

    /**
     * 게시글 좋아요 상태보기
     */
    LikesViewDto getPostLikes(Long id);
    LikesViewDto getPostLikes(Long id, String Username);
    /**
     * 게시글 좋아요 누르기
     */
    LikesViewDto increaseLikes(Long id, String username);

    /**
     * 게시글 좋아요 취소하기
     */
    LikesViewDto decreaseLikes(Long id, String username);

}
