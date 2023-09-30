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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FormatterController {

	private static final String PREFIX = "        v_sql := v_sql || ";
	private static final String CHR10 = "chr(10);";
	private static final String EMPTY_STRING = "\'\'; ";
	private static final String SCHEMA_PROCEDURE = "SCHEMA.PROCEDURE ";
	private static final String PATH = "C:\\Users\\user\\dir\\";
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
		sb.append("CREATE OR REPLACE PROCEDURE ").append(SCHEMA_PROCEDURE).append("\n");
		sb.append("AS").append("\n");
		sb.append("\n");
		sb.append("    BEGIN").append("\n");
		sb.append("\n");
		sb.append("    DECLARE v_sql LONG;").append("\n");
		sb.append("\n");
		sb.append("    BEGIN").append("\n");
		sb.append("\n");

		// inner procedure here
		lines.forEach((line) -> {
			sb.append(PREFIX).append("\'").append(line).append("\'").append("\n");
			sb.append(PREFIX).append(CHR10).append("\n");
			sb.append(PREFIX).append(EMPTY_STRING).append("\n");
		});

		sb.append("\n");
		sb.append("    -- dbms_output.put_line(v_sql);").append("\n");
		sb.append("    EXECUTE IMMEDIATE v_sql;").append("\n");
		sb.append("\n");
		sb.append("    END").append("\n");
		sb.append("\n");
		sb.append("END").append("\n");
		return sb.toString();
	}

	private static List<String> readFile(String dir) {
		Path path = Paths.get(dir);

		List<String> sqlString = null;
		try (BufferedReader rd = Files.newBufferedReader(path); Stream<String> lines = rd.lines()) {

			sqlString = lines.map(Function.identity()).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sqlString;
	}

}
