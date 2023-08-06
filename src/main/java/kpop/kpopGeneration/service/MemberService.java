package kpop.kpopGeneration.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.Retention;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;

    public boolean checkDuplicateUsername(String username){
        int cnt = memberRepository.findCntByUsername(username);
        if(cnt > 0 ){
            throw new DuplicateException();
        }
        return true;
    }

    public boolean checkDuplicateNickname(String nickname){
        int cnt = memberRepository.findCntByNickname(nickname);
        if(cnt > 0 ){
            throw new DuplicateException();
        }
        return true;
    }

    @Transactional
    public Long save(Member member){
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        Member save = memberRepository.save(new Member(member.getUsername(), encodedPassword, member.getNickName()));
//        MemberRole savedRole = memberRoleRepository.save(new MemberRole(save));
        return save.getId();
    }

    @Transactional
    public void deleteAllMember(){
        memberRepository.deleteAllMember();
    }
}
