package app.start;

import java.util.Scanner;

import json.utilities.JSONUtil;

public class MainClass {

	public static void main(String[] args) {
		getUserInput(args);
	}

	public static void getUserInput(String[] args) {
		Scanner in = new Scanner(System.in);
		boolean loopContinues = true;
		System.out.println("Welcome to the employee management application!");

		while (loopContinues) {
			System.out.println("Type one of the following options and press Enter:");
			System.out.println("* Write to json file (employees) - |E|");
			System.out.println("* Read json file (employees) - |e|");
			System.out.println("* Write report definition - |D|");
			System.out.println("* Read report definition - |d|");
			System.out.println("* Generate report - |R|");
			System.out.println("* Quit - |Q|");
			System.out.print("Choose option: ");
			char choice = in.next().charAt(0);
			switch (choice) {
			case 'E':
				JSONUtil.writeToJsonDataFile(args[0]);
				break;
			case 'e':
				JSONUtil.readJsonDataFile(args[0]);
				break;
			case 'D':
				JSONUtil.writeReportDefinition(args[1]);
				break;
			case 'd':
				JSONUtil.readReportDefinition(args[1]);
				break;
			case 'R':
				JSONUtil.generateCSVReport(args[0], args[1], args[2]);
				break;
			case 'Q':
				loopContinues = false;
				break;
			default:
				break;
			}
		}
	}
}