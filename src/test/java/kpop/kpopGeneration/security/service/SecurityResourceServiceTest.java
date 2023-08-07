package kpop.kpopGeneration.security.service;

import kpop.kpopGeneration.security.entity.repository.ResourceRoleRepository;
import kpop.kpopGeneration.security.entity.repository.ResourceRoleRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class SecurityResourceServiceTest {

    @Autowired
    SecurityResourceService securityResourceService;
    @Autowired
    ResourceRoleRepository resourceRoleRepository;

    @Test
    @DisplayName("securityResouceService 동작 테스트")
    void testSecurityResourceService(){
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> resourceRoleList = securityResourceService.getResourceRoleList();
        assertTrue(resourceRoleList.containsKey(new AntPathRequestMatcher("/test/member")));
        assertTrue(resourceRoleList.containsKey(new AntPathRequestMatcher("/test/manager")));
        assertTrue(resourceRoleList.containsKey(new AntPathRequestMatcher("/test/admin")));

        assertEquals(3, resourceRoleList.get(new AntPathRequestMatcher("/test/member")).size());
        assertEquals(2, resourceRoleList.get(new AntPathRequestMatcher("/test/manager")).size());
        assertEquals(1, resourceRoleList.get(new AntPathRequestMatcher("/test/admin")).size());
    }

    @Test
    @DisplayName("ResouceRole이 아무것도 없을 때도 잘 작동하는가")
    void testNull(){
        resourceRoleRepository.deleteAll();

        assertEquals(0, resourceRoleRepository.findAllResourceRole().get().size());
        assertEquals(0, securityResourceService.getResourceRoleList().size());

    }
}