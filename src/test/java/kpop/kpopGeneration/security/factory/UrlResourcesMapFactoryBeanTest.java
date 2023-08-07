package kpop.kpopGeneration.security.factory;

import kpop.kpopGeneration.security.service.SecurityResourceService;
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
class UrlResourcesMapFactoryBeanTest {

    @Autowired
    private SecurityResourceService securityResourceService;

    @Test
    @DisplayName("초기화된 ResourceRole Map을 제공하는 팩토리빈입니다")
    void initFactoryBean() throws Exception {

        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean(securityResourceService);
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> object = urlResourcesMapFactoryBean.getObject();

        Assertions.assertEquals(3, object.get(new AntPathRequestMatcher("/test/member")).size());
        Assertions.assertEquals(2, object.get(new AntPathRequestMatcher("/test/manager")).size());
        Assertions.assertEquals(1, object.get(new AntPathRequestMatcher("/test/admin")).size());

    }
}