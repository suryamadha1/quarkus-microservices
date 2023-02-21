package org.demo.quarkus.microservices.number;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;


@ApplicationPath("/")
@OpenAPIDefinition(
        info= @Info(
                title = "Number API",
                description = "Generates ISBN book numbers",
                version = "1.0",
                contact = @Contact(name="@suryamadhavan", url="https://www.google.com")),
        tags = {
                @Tag(name = "api", description = "Public API"),
                @Tag(name = "numbers", description = "Interested in numbers")
        }
        )

public class NumberMicroservice extends Application {
}
