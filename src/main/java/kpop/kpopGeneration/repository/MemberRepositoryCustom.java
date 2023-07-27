package kpop.kpopGeneration.repository;

public interface MemberRepositoryCustom {
    int findCntByUsername(String username);
    int findCntByNickname(String nickname);
    void deleteAllMember();
}
