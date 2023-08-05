package kpop.kpopGeneration.security.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.NotExistedCommentException;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> byUsername = memberRepository.findByUsername(username);
        Member member = byUsername.orElseThrow(() -> new NotExistedMemberException());

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(member.getRole()));
        MemberContext memberContext = new MemberContext(member, roles);

        return memberContext;
    }
}
