package com.pitchmanagement.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Truong Xuan Giang",
                        email = "giangtx.2003@gmail.com",
                        url = "url"
                ),
                description = "Ứng dụng quản lý và cho thuê sân bóng!",
                title = "Quản lý sân bóng",
                version = "v1"
        ),
        servers = {
                @Server (
                        description = "DEV",
                        url = "http://localhost:8080"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Jwt auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
