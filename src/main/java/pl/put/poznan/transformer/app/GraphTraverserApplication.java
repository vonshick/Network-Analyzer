package pl.put.poznan.transformer.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = {"pl.put.poznan.transformer.rest"})
public class GraphTraverserApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraphTraverserApplication.class, args);
    }
}

