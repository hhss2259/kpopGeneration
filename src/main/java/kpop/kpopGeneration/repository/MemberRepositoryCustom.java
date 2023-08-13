package kpop.kpopGeneration.repository;

import kpop.kpopGeneration.entity.Post;
import org.springframework.data.domain.Page;

public interface MemberRepositoryCustom {
    int findCntByUsername(String username);
    int findCntByNickname(String nickname);
    void deleteAllMember();

}
