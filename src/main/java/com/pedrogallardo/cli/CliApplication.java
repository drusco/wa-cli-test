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
import java.util.HashMap;
import java.util.ArrayList;

@SpringBootApplication
public class CliApplication implements CommandLineRunner {

	private Data data;
	private static final List<String> VALID_ACTIONS = Arrays.asList("analyze");

	public static void main(String[] args) {
		SpringApplication.run(CliApplication.class, args);
	}

	public void setData(Data data) {
		this.data = data;
	}

	public CliApplication() throws IOException {
		File jsonFile = new File("dicts/data.json");
		ObjectMapper objectMapper = new ObjectMapper();
		Data jsonData = objectMapper.readValue(jsonFile, Data.class);

		this.setData(jsonData);
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
		long startTime = System.currentTimeMillis();
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

		if (depth < 1) {
			System.err.println("Depth should not be less than 1.");
            return;
		}

        if (phrase == null) {
            System.err.println("Phrase is required.");
            return;
        }

		long paramsTime = System.currentTimeMillis() - startTime;
		long resultStartTime = System.currentTimeMillis();

		List<Item> data = this.data.getData();

		Map<String, Integer> results = traverseData(data, depth, phrase);

		long resultTime = System.currentTimeMillis() - resultStartTime;

		System.out.println("");

		if (results.isEmpty()) {
			System.out.print("0");
		} else {
			for (Map.Entry<String, Integer> entry : results.entrySet()) {
				System.out.print(entry.getKey() + " = " + entry.getValue() + "; ");
			}
		}

		if (verbose) {
			System.out.println("\n\r");
			System.out.printf("%-40s %s%n", "Tempo de carregamento dos parâmetros", paramsTime + "ms");
			System.out.printf("%-40s %s%n", "Tempo de verificação da frase", resultTime + "ms");
		}

		System.out.println("\n\r");

    }

	private Map<String, Integer> traverseData(List<Item> items, int depth, String phrase) {
		List<String> path = new ArrayList<>();
		Map<String, Integer> results = new HashMap<>();

		return traverseData(items, path, results, depth, phrase);
	}

	private Map<String, Integer> traverseData(List<Item> items, List<String> path, Map<String, Integer> results, int depth, String phrase) {

		int pathSize = path.size();
		String phraseLowercase = phrase.toLowerCase();

		for (Item item : items) {
			String name = item.getName();
			List<Item> children = item.getItems();

			if (pathSize >= depth && phraseLowercase.contains(name.toLowerCase())) {

				String pathName = path.get(depth-1);
				results.put(pathName, results.getOrDefault(pathName, 0) + 1);

			}

			if (children != null && !children.isEmpty()) {
				List<String> currentPath = new ArrayList<>(path);
				currentPath.add(name);
				traverseData(children, currentPath, results, depth, phrase);
			}
		}

		return results;
	}


}
