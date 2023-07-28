package kpop.kpopGeneration.service;

import kpop.kpopGeneration.entity.Member;
import kpop.kpopGeneration.exception.NotExistedMemberException;
import kpop.kpopGeneration.exception.NotMatchedPasswordException;
import kpop.kpopGeneration.repository.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {

    private final LoginRepository loginRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(String username, String password) {

        Optional<Member> memberByUsername = loginRepository.findMemberByUsername(username);
        if(memberByUsername.isEmpty()){
            throw new NotExistedMemberException();
        }

        Member member = memberByUsername.get();
        if(!passwordEncoder.matches(member.getPassword(), password)){
            throw new NotMatchedPasswordException();
        }

        return member.getUsername();
    }


}
