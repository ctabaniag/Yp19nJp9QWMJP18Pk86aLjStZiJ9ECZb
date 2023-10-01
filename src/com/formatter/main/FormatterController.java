package com.formatter.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public class FormatterController {

	private static final String PREFIX = "        v_sql := v_sql || ";
	private static final String CHR10 = "chr(10)";
	private static final String SINGLE_QUOTE = "\'";
	private static final String SEMI_COLON = ";";
	private static final String NEXT_LINE = "\n";
	private static final String SCHEMA_PROCEDURE = "SCHEMA.PROCEDURE ";
	private static final String PATH = "C:\\Users\\user\\folder\\";
	private static final String SOURCE_FILE_NAME = "sample.sql";
	private static final String DESTINATION_FILE_NAME = "file.txt";

	public static void main(String[] args) {

		// read an sql file
		List<String> lines = readFile(PATH + SOURCE_FILE_NAME);
		
		// process
		String data = processString(lines);
		System.out.println("Data to write: \n" + data);

		// output a txt file
		createFile(data);
	}

	private static void createFile(String data) {
		Charset charset = Charset.forName("US-ASCII");
		Path file = Paths.get(PATH + DESTINATION_FILE_NAME);
		try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("-- Done --");
	}

	private static String processString(List<String> lines) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE OR REPLACE PROCEDURE ").append(SCHEMA_PROCEDURE).append(NEXT_LINE);
		sb.append("AS").append(NEXT_LINE);
		sb.append(NEXT_LINE);
		sb.append("    BEGIN").append(NEXT_LINE);
		sb.append(NEXT_LINE);
		sb.append("    DECLARE v_sql LONG;").append(NEXT_LINE);
		sb.append(NEXT_LINE);
		sb.append("    BEGIN").append(NEXT_LINE);
		sb.append(NEXT_LINE);

		// inner procedure here
		lines.forEach((line) -> {
			sb.append(PREFIX).append(SINGLE_QUOTE).append(line).append(SINGLE_QUOTE).append(SEMI_COLON).append(NEXT_LINE);
			sb.append(PREFIX).append(CHR10).append(SEMI_COLON).append(NEXT_LINE);
		});

		sb.append(NEXT_LINE);
		sb.append("    -- dbms_output.put_line(v_sql);").append(NEXT_LINE);
		sb.append("    EXECUTE IMMEDIATE v_sql;").append(NEXT_LINE);
		sb.append(NEXT_LINE);
		sb.append("    END").append(NEXT_LINE);
		sb.append(NEXT_LINE);
		sb.append("END").append(NEXT_LINE);
		return sb.toString();
	}

	private static List<String> readFile(String dir) {
		Path path = Paths.get(dir);
		
		Function<String, String> formatLines = (line) -> {
			return (StringUtils.isBlank(line)) ? StringUtils.EMPTY : line;
		};
		
		List<String> sqlString = null;
		try (BufferedReader rd = Files.newBufferedReader(path); Stream<String> lines = rd.lines()) {

			sqlString = lines.map(formatLines).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sqlString;
	}

}
