package com.siwoo.azurefunctions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AzureFunctionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzureFunctionsApplication.class, args);
    }

    @RestController
    public static class MainController {

    }
}
