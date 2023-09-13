package org.ejprarediseases.vpdpbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        security = @SecurityRequirement(name = "bearerAuth"),
        info = @Info(
                title = "VP-Portal Backend",
                description = "The EJP VP-Portal Backend serves as the foundational engine powering " +
                        "the EJP VP-Portal ecosystem. It offers an array of sophisticated functionalities, " +
                        "including advanced disease search capabilities, comprehensive disease hierarchy retrieval, " +
                        "precision gene mapping, efficient search autocompletion, robust resource monitoring " +
                        "service, seamless resource notification service, and an array of other distinctive " +
                        "features. These capabilities collectively aim to ensure an unparalleled user experience " +
                        "within the VP-Portal environment.",
                version = "v0.1.0",
                license = @License(
                        name = "Apache License 2.0",
                        url = "https://github.com/ejp-rd-vp/vp-dp-backend/blob/develop/LICENSE"
                ),
                contact = @Contact(
                        name = "VP-Portal Team",
                        url = "https://vp.ejprarediseases.org/"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@SpringBootApplication
public class VpDpBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VpDpBackendApplication.class, args);
    }

}
