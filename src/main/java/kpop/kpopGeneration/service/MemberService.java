package kpop.kpopGeneration.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.Duplicate;
import kpop.kpopGeneration.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean checkDuplicateUsername(String username){
        int byUsername = memberRepository.findCntByUsername(username);
        if(byUsername > 0 ){
            throw new Duplicate();
        }
        return true;
    }

    @Transactional(readOnly = true)
    public boolean checkDuplicateNickname(String nickname){
        int byNickname = memberRepository.findCntByNickname(nickname);
        if(byNickname > 0 ){
            throw new Duplicate();
        }
        return true;
    }

    @Transactional
    public int save(Member member){
        Member save = memberRepository.save(member);
        return save.getId();
    }

    @Transactional
    public void deleteAllMember(){
        memberRepository.deleteAllMember();
    }
}
