package explorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        System.out.println("\nStarting...\n");

        SpringApplication.run(Main.class, args);

        System.out.println("\nTERMINATED\n");
    }
}
