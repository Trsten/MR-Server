package com.uniza.mr;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures a JAX-RS endpoint.
 */
@ApplicationPath("/mrreport")
@OpenAPIDefinition(
        info = @Info(
                title = "Meeting reports server",
                version = "1.0",
                description = "Simple and easy meeting management."
        )
)

public class JAXRSConfiguration extends Application {

}
