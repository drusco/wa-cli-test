package com.pedrogallardo.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CliApplicationTests {

	private CliApplication cliApp;
	private List<Item> jsonData;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	private final String jsonString = """
        {
            "data": [
                {
                    "name": "Item1",
                    "items": [
                        {
                            "name": "Sub1",
                            "items": [
								{
									"name": "Deep1",
									"items": []
								},
								{
									"name": "Deep2",
									"items": []
								}
							]
                        }
                    ]
                },
                {
                    "name": "Item2",
                    "items": [
                        {
                            "name": "Sub2",
                            "items": [
								{
									"name": "Deep3",
									"items": []
								}
							]
                        }
                    ]
                }
            ]
        }
        """;

	@BeforeEach
    public void setUp() throws IOException {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));

		Data data = objectMapper.readValue(jsonString, Data.class);

		cliApp = new CliApplication();

		cliApp.setData(data);
		
	}

	@AfterEach
	public void cleanUp() {
		System.setOut(originalOut);
		System.setErr(originalErr);
	}

    @Test
    void testEmptyActions() throws IOException {
        String[] args = {};

        cliApp.run(args);

		String output = outContent.toString();
        
        assertTrue(output.contains("usage:\n\r	analyze --depth <n> --verbose (optional) \"{phrase}\""));
    }

    @Test
    void testAnalyzeAction() throws IOException {
        String[] args = {"analyze", "--depth", "2", "Deep3"};

        cliApp.run(args);

		String output = outContent.toString();

		assertEquals("Sub2 = 1;", output.trim(), "Output is Sub2 = 1;");
    }

    @Test
    void testLargePhrase() throws IOException {
        String longPhrase = "a".repeat(5000) + "Deep3 Deep1.";

        String[] args = {"analyze", "--depth", "2", longPhrase};

        cliApp.run(args);

		String output = outContent.toString();

		assertTrue(output.contains("Sub1 = 1;"));
		assertTrue(output.contains("Sub2 = 1;"));
		assertEquals("Sub2 = 1; Sub1 = 1;", output.trim(), "Output is Sub2 = 1; Sub1 = 1;");
    }

    @Test
    void testNoResultsFound() throws IOException {
        String[] args = {"analyze", "--depth", "6", "teste"};

        cliApp.run(args);

		String output = outContent.toString();

		assertEquals("0", output.trim(), "Output is 0");
    }

    @Test
    void testDefaultDepthAndVerbose() throws IOException {
        String[] args = {"analyze", "--verbose", "Sub1"};

        cliApp.run(args);

		String output = outContent.toString();

		assertTrue(output.contains("Tempo de carregamento dos parâmetros"));
		assertTrue(output.contains("Tempo de verificação da frase"));
		assertTrue(output.contains("Item1 = 1;"));
    }

	@Test
    void testDepthIsInvalid() throws IOException {
        String[] args = {"analyze", "--depth", "?", "teste"};

        cliApp.run(args);

		String errorOutput = errContent.toString();

		assertTrue(errorOutput.contains("Invalid depth value."));
    }

	@Test
    void testDepthIsLessThanOne() throws IOException {
        String[] args = {"analyze", "--depth", "-1", "teste"};

        cliApp.run(args);

		String errorOutput = errContent.toString();

		assertTrue(errorOutput.contains("Depth should not be less than 1."));
    }

	@Test
    void testUndefinedPhrase() throws IOException {
        String[] args = {"analyze"};

        cliApp.run(args);

		String errorOutput = errContent.toString();

		assertTrue(errorOutput.contains("Phrase is required."));
    }
}
