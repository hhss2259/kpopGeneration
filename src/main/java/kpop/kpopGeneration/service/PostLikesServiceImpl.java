package kpop.kpopGeneration.service;

import kpop.kpopGeneration.dto.PostLikesViewDto;
import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.entity.Post;
import kpop.kpopGeneration.entity.PostLikes;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotExistedPostException;
import kpop.kpopGeneration.exception.NotExistedPostLikes;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.repository.PostLikesRepository;
import kpop.kpopGeneration.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostLikesServiceImpl implements PostLikesService{

    private final PostLikesRepository postLikesRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /**
     * 게시글 좋아요 상태보기
     */
    @Override
    public PostLikesViewDto getPostLikes(Long id, String username) {
        // DB에서 해당 포스트를 가지고 온다
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());
        // DB에서 해당 멤버를 가지고 온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());

        Optional<PostLikes> byMember = postLikesRepository.findByMemberAndPost(member, post);
        PostLikesViewDto postLikesViewDto = null;
        if(byMember.isEmpty()) { // 아직 좋아요를 누르지 않았다면, isLiked 필드를 false로 설정한다
            postLikesViewDto = new PostLikesViewDto(post.getLikes(), false);
        }else{ // 이미 좋아요를 눌렀다면, isLiked 필드를 true로 설정한다
            postLikesViewDto = new PostLikesViewDto(post.getLikes(), true);
        }
        return postLikesViewDto;
    }

    /**
     * 게시글 좋아요 누르기
     */
    @Transactional
    @Override
    public PostLikesViewDto increaseLikes(Long id, String username) {
        // DB에서 해당 포스트를 가지고 온다
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());
        // DB에서 해당 멤버를 가지고 온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());
        // DB에서 PostLikes 정보를 조회해온다
        Optional<PostLikes> byMember = postLikesRepository.findByMemberAndPost(member, post);
        if(byMember.isPresent()){ // 이미 멤버가 좋아요를 누른 전적이 있으면 더 이상 좋아요를 누를 수 없다
            throw new DuplicateException("이미 좋아요를 누른 Member입니다");
        }

        // 포스트의 좋아요 갯수를 증가시킨다
        post.increaseLikes();
        // PostLikes에 해당 memebr와 post를 저장한다
        postLikesRepository.save(new PostLikes(member, post));
        return new PostLikesViewDto(post.getLikes(), true);
    }

    /**
     * 게시글 좋아요 취소하기
     */
    @Transactional
    @Override
    public PostLikesViewDto decreaseLikes(Long id, String username) {
        // DB에서 해당 포스트를 가지고 온다
        Post post = postRepository.findPostById(id).orElseThrow(() -> new NotExistedPostException());
        // DB에서 해당 멤버를 가지고 온다
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new NotExistedMemberException());
        // DB에서 PostLikes 정보를 조회해온다
        PostLikes postLikes = postLikesRepository.findByMemberAndPost(member, post).orElseThrow(() -> new NotExistedPostLikes());

        // 포스트의 좋아요 갯수를 감소시킨다
        post.decreaseLikes();
        // 해당 PostLikes를 삭제한다
        postLikesRepository.delete(postLikes);
        return new PostLikesViewDto(post.getLikes(), false);
    }


}
