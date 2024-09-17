package com.pedrogallardo.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class CliApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CliApplication.class, args);
	}

	@Override
    public void run(String... args) throws IOException {
        if (args.length > 0) {
            String phrase = args[args.length - 1];
            System.out.println("Phrase: " + phrase);
            
            File jsonFile = new File("dicts/data.json");
            
            // read JSON
            ObjectMapper objectMapper = new ObjectMapper();
            Map<?, ?> jsonData = objectMapper.readValue(jsonFile, Map.class);
            
            System.out.println("JSON: " + jsonData);
        } else {
            System.out.println("phrase is not defined");
        }
    }
}
