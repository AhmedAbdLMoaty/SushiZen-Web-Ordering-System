package ism.lab02_ism;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ism.lab02_ism",
        "ism.lab02_ism.controller",
        "ism.lab02_ism.repository",
        "ism.lab02_ism.service",
        "ism.lab02_ism.util"
})
public class Lab02IsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(Lab02IsmApplication.class, args);
    }
}