package com.pedrogallardo.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CliApplication implements CommandLineRunner {

	private static final List<String> VALID_ACTIONS = Arrays.asList("analyze");

	public static void main(String[] args) {
		SpringApplication.run(CliApplication.class, args);
	}

	@Override
    public void run(String... args) throws IOException {
        if (args.length == 0 || !VALID_ACTIONS.contains(args[0])) {
            System.out.println("usage:\n\r	analyze --depth <n> --verbose (optional) \"{phrase}\"");
            return;
        }

        String action = args[0];
        String[] actionArgs = Arrays.copyOfRange(args, 1, args.length);

        if (action.equals("analyze")) {
            analyze(actionArgs);
        }
    }

	private void analyze(String... args) throws IOException {
        int depth = 1;
        boolean verbose = false;
        String phrase = null;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "--depth":
                    if (i + 1 < args.length) {
                        try {
                            depth = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid depth value.");
                            return;
                        }
                    }
                    break;
                case "--verbose":
                    verbose = true;
                    break;
                default:
                    phrase = arg;
                    break;
            }
        }

        if (phrase == null) {
            System.err.println("Phrase is required.");
            return;
        }

        System.out.println("Phrase: " + phrase);
        System.out.println("Depth: " + depth);
        System.out.println("Verbose: " + verbose);

        File jsonFile = new File("dicts/data.json");

        ObjectMapper objectMapper = new ObjectMapper();
        
		Data jsonData = objectMapper.readValue(jsonFile, Data.class);

		List<Item> data = jsonData.getData();

    }
}
