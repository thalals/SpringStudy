package com.example.prefix_url.config;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class WebMvcHandlerConfig implements WebMvcRegistrations {

    private static final String DEFAULT_VERSION_PREFIX = "/v1";

    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return new ApiVersionRequestMappingHandlerMapping();
    }

    private static class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

        @Override
        protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

            RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
            if (mappingInfo == null) {
                return null;
            }

            // 매핑 정보에 포함된 패턴을 확인하여 swagger 관련 경로인 경우 버전 접두사 적용을 건너뜁니다.
            final Set<String> patterns = getPatternHandlerPatternList(mappingInfo);
            final boolean isSwaggerMapping = patterns.stream()
                .anyMatch(path -> path.contains("swagger") || path.contains("api-docs"));

            if (isSwaggerMapping) {
                return mappingInfo;
            }

            // 커스텀 @ApiVersion 애노테이션을 이용하여 버전 정보를 추출 (메서드 우선, 없으면 클래스 레벨)
            ApiVersion apiVersion = AnnotatedElementUtils.findMergedAnnotation(method, ApiVersion.class);
            if (apiVersion == null) {
                apiVersion = AnnotatedElementUtils.findMergedAnnotation(handlerType, ApiVersion.class);
            }

            final String versionPrefix = (apiVersion != null && !apiVersion.value().isEmpty())
                ? "/" + apiVersion.value() : DEFAULT_VERSION_PREFIX;

            RequestMappingInfo versionMapping = RequestMappingInfo.paths(versionPrefix).build();
            return versionMapping.combine(mappingInfo);

        }

        private Set<String> getPatternHandlerPatternList(final RequestMappingInfo mappingInfo) {
            if (Objects.nonNull(mappingInfo.getPathPatternsCondition())
                && !mappingInfo.getPathPatternsCondition().getPatternValues().isEmpty()) {

                return mappingInfo.getPathPatternsCondition().getPatternValues();
            }
            if (Objects.nonNull(mappingInfo.getPatternsCondition())) {
                return mappingInfo.getPatternsCondition().getPatterns();
            }

            return Collections.emptySet();
        }

    }
}
