package com.serwertetowy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HostserwerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostserwerApplication.class, args);
	}

//	@GetMapping("/greet")
//	public GreetResponse greet(){
//		return new GreetResponse("Joe Mama");
//	}
//
//	record GreetResponse(String greet){}

}
