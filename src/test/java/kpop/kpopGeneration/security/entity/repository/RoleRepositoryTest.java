package kpop.kpopGeneration.security.entity.repository;

import kpop.kpopGeneration.security.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @DisplayName("기본 Role 확인하기")
    void basicRoleTest(){
        List<Role> all = roleRepository.findAll();
        Assertions.assertEquals(3, all.size());
        all.forEach((role)->System.out.println(role.getName()));
    }
}