package github.com.rexfilius.tea;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "Tea API",
                description = "API documentation for a blog app",
                version = "v1.0",
                contact = @Contact(
                        name = "Ify Osakwe",
                        email = "ifeakachukwuosakwe@gmail.com"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Tea (GitHub)",
                url = "github-url-here"
        )
)
@SpringBootApplication
public class TeaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeaApplication.class, args);
        System.out.println("=======Tea application started========");
    }

//    @Autowired
//    private RoleRepository roleRepository;

//    @Override
//    public void run(String... args) throws Exception {
//        Role adminRole = new Role();
//        adminRole.setName("ROLE_ADMIN");
//        roleRepository.save(adminRole);
//
//        Role userRole = new Role();
//        userRole.setName("ROLE_USER");
//        roleRepository.save(userRole);
//    }
}
