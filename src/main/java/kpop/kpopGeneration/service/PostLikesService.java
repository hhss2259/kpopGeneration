package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.PostLikesViewDto;

public interface PostLikesService {

    /**
     * 게시글 좋아요 상태보기
     */

    PostLikesViewDto getPostLikes(Long id, String Username);
    /**
     * 게시글 좋아요 누르기
     */
    PostLikesViewDto increaseLikes(Long id, String username);

    /**
     * 게시글 좋아요 취소하기
     */
    PostLikesViewDto decreaseLikes(Long id, String username);

}
