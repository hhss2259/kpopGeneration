package kpop.kpopGeneration.config;

import kpop.kpopGeneration.interceptor.ModelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig  implements WebMvcConfigurer {
    private final ModelInterceptor modelInterceptor;

    /**
     * 템플릿 엔진에서 현재 사용자의 정보를 사용할 수 있도록 항상 MemberViewDto을 제공하는 interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(modelInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**");
    }
}
