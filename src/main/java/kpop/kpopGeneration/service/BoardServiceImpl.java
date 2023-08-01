package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.Category;
import kpop.kpopGeneration.dto.PostDetailDto;
import kpop.kpopGeneration.dto.PostSaveDto;
import kpop.kpopGeneration.dto.PostTitleDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.repository.BoardRepository;
import kpop.kpopGeneration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Page<PostTitleDto> findPostListByCategory(Category category, Pageable pageable) {
        Page<Post> postList = null;
        if (category == Category.ALL) {
            postList = boardRepository.findALLPostList(pageable);
        }else{
            postList = boardRepository.findPostListByCategory(category, pageable);
        }

        Page<PostTitleDto> postTitleList = postList.map(
                (post) -> new PostTitleDto(
                        post.getId(), post.getCategory(), post.getTitle(),
                        post.getMember().getNickName(), post.getLastModifiedTime()
                )
        );

        return postTitleList;
    }


    /**
     * 게시글 자세히 보기
     */
    @Override
    public PostDetailDto findPostById(Long id) {
        Optional<Post> postById = boardRepository.findPostById(id);
        Post post = postById.orElseThrow(()->new NotExistedPostException());

//        PostDetailDto postDetailDto = new PostDetailDto(post.getTitle(), post.getBody(), )


        return null;
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

}
