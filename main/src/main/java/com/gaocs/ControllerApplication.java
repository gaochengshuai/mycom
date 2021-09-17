package com.gaocs;

import com.gaocs.utils.Autosend;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class ControllerApplication {

	public static void main(String[] args) {
		if(args.length < 2){
			System.out.println("参数不足");
		}
		Autosend autosend = new Autosend();
		autosend.start(args);
//		SpringApplication.run(ControllerApplication.class, args);
	}

}
