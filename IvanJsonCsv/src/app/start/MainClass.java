package app.start;

import json.utilities.JSONUtil;

public class MainClass {

	public static void main(String[] args) {		
		//JSONUtil.writeReportDefinition("json-files/report-definition.json");
		JSONUtil.readReportDefinition("json-files/report-definition.json");
	}
}
