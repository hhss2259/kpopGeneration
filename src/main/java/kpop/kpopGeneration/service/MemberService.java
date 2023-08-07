package kpop.kpopGeneration.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.DuplicateException;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.repository.MemberRepository;
import kpop.kpopGeneration.security.entity.MemberRole;
import kpop.kpopGeneration.security.entity.Role;
import kpop.kpopGeneration.security.entity.repository.MemberRoleRepository;
import kpop.kpopGeneration.security.entity.repository.RoleRepository;
import kpop.kpopGeneration.security.exception.NotExistedRoleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final RoleRepository roleRepository;

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
        // 비밀번호 암호화 
        String encodedPassword = passwordEncoder.encode(member.getPassword());
        
        // Member 저장
        Member savedMember = memberRepository.save(new Member(member.getUsername(), encodedPassword, member.getNickName()));
        
        // 기본 role은 "ROLE_USER" 입니다
        Role basicRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new NotExistedRoleException());
        
        //MemberRole을 저장합니다
        MemberRole savedRole = memberRoleRepository.save(new MemberRole(savedMember, basicRole));

        return savedMember.getId();
    }

    @Transactional
    public String updateMemberRole(String username, List<String> names){
        //  role을 업데이트할 멤버
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        Member member = byUsername.orElseThrow(() -> new NotExistedMemberException());

        //  MemberRole 테이블에서 해당 member의 기존 role을 모두 삭제
        memberRoleRepository.deleteAllByMember(member);


        // MemberRole 테이블에 해당 member의 새로운 role들을 추가
        List<Role> roles = roleRepository.findAllByName(names).get();
        roles.forEach(role -> {
            memberRoleRepository.save(new MemberRole(member, role));
        });

        return username;
    }

    @Transactional
    public void deleteAllMember(){
        memberRepository.deleteAll();
    }
}
