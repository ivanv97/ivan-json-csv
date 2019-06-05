package json.utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
				totalSales = in.nextInt();
				System.out.print("Sales period: ");
				salesPeriod = in.nextInt();
				System.out.print("Experience multiplier: ");
				experienceMultiplier = in.nextFloat();
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
			employeesData = (JSONArray) jsonParser.parse(new FileReader(filePath));
			System.out.println("Adding to an existing file...");
			employeesData.add(employee);
		} catch (FileNotFoundException e) {
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

	public static void readJsonDataFile(String filePath) {
		try {
			JSONArray employeeData = (JSONArray) jsonParser.parse(new FileReader(filePath));
			System.out.println(employeeData);
		} catch (FileNotFoundException e) {
			System.out.println("File was not found! Try different path!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void writeReportDefinition(String filePath) {
		System.out.println("Welcome to JSON report definition file writing functionality!");

		int topPerformersTreshold = 0;
		boolean useExperienceMultiplier = false;
		int periodLimit = 0;

		boolean inputTaken = false;

		while (!inputTaken) {
			try {
				System.out.print("Top performers treshold: ");
				topPerformersTreshold = in.nextInt();
				System.out.print("Use experience multiplier (true/false): ");
				useExperienceMultiplier = in.nextBoolean();
				System.out.print("Period limit: ");
				periodLimit = in.nextInt();
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

	public static void readReportDefinition(String filePath) {
		try {
			JSONObject reportDefinition = (JSONObject) jsonParser.parse(new FileReader(filePath));
			System.out.println(reportDefinition);
		} catch (FileNotFoundException e) {
			System.out.println("File was not found! Try different path!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
