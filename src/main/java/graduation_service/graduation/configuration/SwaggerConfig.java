package graduation_service.graduation.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("졸업요건 자가진단 서비스 API Document")
                .version("v1")
                .description("졸업요건 자가진단 서비스 API 명세입니다.");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}
