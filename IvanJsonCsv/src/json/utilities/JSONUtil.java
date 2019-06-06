package json.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings("unchecked")
public final class JSONUtil {
	private static Scanner in = new Scanner(System.in);
	private static JSONParser jsonParser = new JSONParser();

	private JSONUtil() {
	}

	/*
	 * Prompts the user to type in the attributes of an employee and adds that
	 * employee to already existing (or creates new) json file in the specified
	 * path that holds array of employees in it.
	 */
	public static void writeToJsonDataFile(String filePath) {
		System.out.println("Welcome to JSON data file writing functionality!");

		String name = "";
		int totalSales = 0;
		int salesPeriod = 0;
		float experienceMultiplier = 0;

		boolean inputTaken = false;

		System.out.print("Start by typing name of the employee first: ");
		name = in.nextLine();

		while (!inputTaken) {
			try {
				System.out.print("Total sales: ");
				totalSales = Integer.parseInt(in.nextLine());
				System.out.print("Sales period: ");
				salesPeriod = Integer.parseInt(in.nextLine());
				System.out.print("Experience multiplier: ");
				experienceMultiplier = Float.parseFloat(in.nextLine());
				inputTaken = true;
			} catch (Exception e) {
				System.out.println("Please try again with the correct values!");
			}
		}

		JSONArray employeesData = null;
		JSONObject employee = new JSONObject();
		employee.put("name", name);
		employee.put("totalSales", totalSales);
		employee.put("salesPeriod", salesPeriod);
		employee.put("experienceMultiplier", experienceMultiplier);

		try {
			// if the file already exists
			employeesData = (JSONArray) jsonParser.parse(new FileReader(filePath));
			System.out.println("Adding to an existing file...");
			employeesData.add(employee);
		} catch (FileNotFoundException e) {
			// if there is no such file
			System.out.println("Creating a new file...");
			employeesData = new JSONArray();
			employeesData.add(employee);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			try (FileWriter file = new FileWriter(filePath)) {
				file.write(employeesData.toJSONString());
				file.flush();
				System.out.println("Operation successful! File saved!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * Reads the json file containing all employees, prints and returns the json
	 * array contained in it
	 */
	public static JSONArray readJsonDataFile(String filePath) {
		JSONArray employeeData = null;
		try {
			employeeData = (JSONArray) jsonParser.parse(new FileReader(filePath));
			System.out.println(employeeData);
		} catch (FileNotFoundException e) {
			System.out.println("File was not found! Try different path!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return employeeData;
	}

	/*
	 * Gets user input and writes the report definition file in the specified
	 * path
	 */
	public static void writeReportDefinition(String filePath) {
		System.out.println("Welcome to JSON report definition file writing functionality!");

		int topPerformersTreshold = 0;
		boolean useExperienceMultiplier = false;
		int periodLimit = 0;

		boolean inputTaken = false;

		while (!inputTaken) {
			try {
				System.out.print("Top performers treshold: ");
				topPerformersTreshold = Integer.parseInt(in.nextLine());
				System.out.print("Use experience multiplier (true/false): ");
				useExperienceMultiplier = Boolean.parseBoolean(in.nextLine());
				System.out.print("Period limit: ");
				periodLimit = Integer.parseInt(in.nextLine());
				inputTaken = true;
			} catch (Exception e) {
				System.out.println("Please try again with the correct values!");
			}
		}

		JSONObject reportDefinition = new JSONObject();
		reportDefinition.put("topPerformersTreshold", topPerformersTreshold);
		reportDefinition.put("useExperienceMultiplier", useExperienceMultiplier);
		reportDefinition.put("periodLimit", periodLimit);

		try (FileWriter file = new FileWriter(filePath)) {
			file.write(reportDefinition.toJSONString());
			file.flush();
			System.out.println("Operation successful! File saved!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Read the report definition json file, prints it and returns it.
	 */
	public static JSONObject readReportDefinition(String filePath) {
		JSONObject reportDefinition = null;
		try {
			reportDefinition = (JSONObject) jsonParser.parse(new FileReader(filePath));
			System.out.println(reportDefinition);
		} catch (FileNotFoundException e) {
			System.out.println("File was not found! Try different path!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return reportDefinition;
	}

	/*
	 * Read the two json files and generates csv file containing employees that
	 * have sales period that is equal or less than the periodLimit property and
	 * have score that is within the top X percent of the results where X is
	 * defined in report definition
	 */
	public static void generateCSVReport(String targetFilePath, String employeeDataFilePath,
			String reportDefinitionFilePath) {
		// ArrayList that contains the calculated scores
		ArrayList<Float> scores = new ArrayList<Float>();

		JSONArray jsonEmployees = readJsonDataFile(employeeDataFilePath);
		JSONObject reportDefinition = readReportDefinition(reportDefinitionFilePath);

		// 2D ArrayList that will hold the eligible
		// employees to be included in the report
		ArrayList<ArrayList<String>> rows = new ArrayList<ArrayList<String>>();

		// Calculating the scores - if useExperienceMultiplier is set to true
		// the score is multiplied by it and adding it to the list
		for (int i = 0; i < jsonEmployees.size(); i++) {
			JSONObject currentEmployee = (JSONObject) jsonEmployees.get(i);
			float currentScoreNoMultiplier = Float.parseFloat(currentEmployee.get("totalSales").toString())
					/ Float.parseFloat(currentEmployee.get("salesPeriod").toString());
			float currentScore = (boolean) reportDefinition.get("useExperienceMultiplier") == true
					? currentScoreNoMultiplier
							* Float.parseFloat(currentEmployee.get("experienceMultiplier").toString())
					: currentScoreNoMultiplier;
			scores.add(currentScore);
		}

		// Sorting the scores in descending order
		// to find the border score depending on the percentage
		// treshold after which other
		// employees are discarded
		Collections.sort(scores, Collections.reverseOrder());
		float borderScore = scores.get(Math.round(
				scores.size() * Float.parseFloat(reportDefinition.get("topPerformersTreshold").toString()) / 100));

		//Adding the eligible employees to the list
		for (int i = 0; i < jsonEmployees.size(); i++) {
			JSONObject currentEmployee = (JSONObject) jsonEmployees.get(i);
			if (Integer.parseInt(currentEmployee.get("salesPeriod").toString()) <= Integer
					.parseInt(reportDefinition.get("periodLimit").toString())) {
				float currentScoreNoMultiplier = Float.parseFloat(currentEmployee.get("totalSales").toString())
						/ Float.parseFloat(currentEmployee.get("salesPeriod").toString());
				float currentScore = (boolean) reportDefinition.get("useExperienceMultiplier") == true
						? currentScoreNoMultiplier
								* Float.parseFloat(currentEmployee.get("experienceMultiplier").toString())
						: currentScoreNoMultiplier;
				if (currentScore >= borderScore) {
					rows.add(new ArrayList<String>(
							Arrays.asList(currentEmployee.get("name").toString(), Float.toString(currentScore))));
				}
			}
		}

		//Writing to the comma separated values file
		FileWriter csvWriter;
		try {
			csvWriter = new FileWriter(targetFilePath);
			csvWriter.append("Name");
			csvWriter.append(",");
			csvWriter.append("Score");
			csvWriter.append("\n");
			for (ArrayList<String> row : rows) {
				csvWriter.append(String.join(",", row));
				csvWriter.append("\n");
			}
			csvWriter.flush();
			csvWriter.close();
			System.out.println("File saved.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
