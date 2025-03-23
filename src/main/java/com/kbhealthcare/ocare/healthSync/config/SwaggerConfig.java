package com.kbhealthcare.ocare.healthSync.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI healthSyncApi() {
        /*
          Swagger 정보 설정
         */
        Info kbHealthApi = new Info()
                .title("KB 헬스케어 백엔드 과제 API 명세서")
                .description("KB 헬스케어 백엔드 과제 API 명세서입니다.");

        ExternalDocumentation notion = new ExternalDocumentation()
                .description("KB 헬스케어 백엔드 과제 설계 문서")
                .url("https://woonsik.notion.site/KB-1bbefd12221180f6b31acaaa7614ab30?pvs=4");

        return new OpenAPI()
                .info(kbHealthApi)
                .externalDocs(notion)
                ;
    }
}