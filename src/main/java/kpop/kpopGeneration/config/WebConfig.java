package kpop.kpopGeneration.config;

import kpop.kpopGeneration.interceptor.ModelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@RequiredArgsConstructor
public class WebConfig  implements WebMvcConfigurer {
    private final ModelInterceptor modelInterceptor;

    @Value("${custom.path.upload-images}")
    private String uploadImagePath;


    /**
     * 템플릿 엔진에서 현재 사용자의 정보를 사용할 수 있도록 항상 MemberViewDto을 제공하는 interceptor
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(modelInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:///" + uploadImagePath)
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
