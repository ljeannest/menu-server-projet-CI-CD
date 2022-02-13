package com.cicdlectures.menuserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import picocli.CommandLine;
import picocli.CommandLine.Command;



@SpringBootApplication

@Command(
  name = "hello",
  description = "Says hello"
)
public class MenuServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(MenuServerApplication.class, args);
    //CommandLine.run(new MenuServerApplication(), args);
  }

  /*@Override
  public void run() {
    System.out.println("Hello World!");
    
  }*/
}
