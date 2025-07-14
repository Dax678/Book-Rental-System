package org.bookrental.bookrentalsystem;

import org.springframework.boot.SpringApplication;

public class TestBookRentalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookRentalSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
