package com.example.prefix_url.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@SupportedAnnotationTypes({
    "org.springframework.web.bind.annotation.RequestMapping",
    "org.springframework.web.bind.annotation.GetMapping",
    "com.example.prefix_url.config.ApiVersion" // 커스텀 애노테이션
})
@SupportedSourceVersion(SourceVersion.RELEASE_21) // 사용하는 Java 버전에 맞게 설정
public class EndpointAnnotationProcessor extends AbstractProcessor {

    // 기본 전역 prefix (클래스나 메서드에 @ApiVersion이 없을 경우)
    private static final String DEFAULT_GLOBAL_PREFIX = "/v1";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<String> endpointMappings = new ArrayList<>();

        // @RestController가 붙은 모든 클래스를 처리
        for (Element element : roundEnv.getElementsAnnotatedWith(RestController.class)) {
            if (!(element instanceof TypeElement)) {
                continue;
            }
            TypeElement controller = (TypeElement) element;

            // 클래스 레벨 @ApiVersion 처리: 있으면 그 값을, 없으면 기본 전역 prefix 사용
            String classPrefix = DEFAULT_GLOBAL_PREFIX;
            ApiVersion classApiVersion = controller.getAnnotation(ApiVersion.class);
            if (classApiVersion != null && !classApiVersion.value().isEmpty()) {
                classPrefix = "/" + classApiVersion.value();
            }

            // 컨트롤러 내의 메서드들을 순회
            for (Element enclosed : controller.getEnclosedElements()) {
                if (enclosed.getKind() != ElementKind.METHOD) {
                    continue;
                }
                ExecutableElement method = (ExecutableElement) enclosed;

                // 메서드 레벨 @ApiVersion이 있으면 우선 적용
                String methodPrefix = classPrefix;
                ApiVersion methodApiVersion = method.getAnnotation(ApiVersion.class);
                if (methodApiVersion != null && !methodApiVersion.value().isEmpty()) {
                    methodPrefix = "/" + methodApiVersion.value();
                }

                // @GetMapping 및 @RequestMapping 애노테이션의 경로를 수집
                List<String> paths = new ArrayList<>();
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping != null) {
                    paths.addAll(Arrays.asList(getMapping.value()));
                }
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                if (requestMapping != null) {
                    paths.addAll(Arrays.asList(requestMapping.value()));
                }

                // 각 경로에 전역 prefix(및 버전)가 적용된 최종 URL 산출
                for (String path : paths) {
                    // 기본적으로 앞쪽에 methodPrefix를 붙입니다.
                    String finalPath = methodPrefix + path;
                    endpointMappings.add(finalPath);
                }
            }
        }

        // 산출된 엔드포인트 목록을 리소스 파일로 기록
        writeEndpointsFile(endpointMappings);
        return true;
    }

    private void writeEndpointsFile(List<String> endpoints) {
        try {
            // CLASS_OUTPUT 위치에 endpoints.txt 파일 생성
            FileObject file = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "endpoints.txt");
            try (Writer writer = file.openWriter()) {
                String content = endpoints.stream().collect(Collectors.joining("\n"));
                writer.write(content);
            }
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Endpoint mappings generated in endpoints.txt");
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Error writing endpoint mappings: " + e.getMessage());
        }
    }
}