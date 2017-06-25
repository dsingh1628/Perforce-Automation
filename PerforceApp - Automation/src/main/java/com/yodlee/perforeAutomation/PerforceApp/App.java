package com.yodlee.perforeAutomation.PerforceApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.perforce.p4java.core.IChangelist;
import com.perforce.p4java.core.IChangelistSummary;
import com.perforce.p4java.core.file.DiffType;
import com.perforce.p4java.core.file.FileSpecBuilder;
import com.perforce.p4java.core.file.IFileSpec;
import com.perforce.p4java.impl.generic.core.Changelist;
import com.perforce.p4java.option.server.ChangelistOptions;
import com.perforce.p4java.option.server.GetChangelistsOptions;
import com.perforce.p4java.server.IServer;
import com.yodlee.perforceAutomation.Config.PerforceConfig;
import com.yodlee.perforceAutomation.utilities.ExcelWriter;
import com.yodlee.perforceAutomation.utilities.MailUtility;

/**
 * Hello world!
 *
 */
public class App {

	private static String DEPOT_SUFFIX = "#repo_path";

	public static void main(String[] args) {
		try {
			execute();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private static void execute() throws Exception {
		IServer server = PerforceConfig.getConnection();
		server.connect();
	
		ArrayList<String> ils_agents = getAgentList();

		String filePaths[] = new String[ils_agents.size()];
		for (int index = 0; index < ils_agents.size(); index++) {
			filePaths[index] = DEPOT_SUFFIX + ils_agents.get(index) + ".java";
		}
		HashMap<String, Integer> userList = getUserList();
		ArrayList<Result> resultList = getResult(server, filePaths, userList);
		// writeResult(resultList);
		ExcelWriter.writeExcelFile(resultList, "output.xlsx");
		// sending mail
	    MailUtility.SendMail("perforceAutomation",convertMapIntoTable(userList));

	}

	private static String convertMapIntoTable(HashMap<String, Integer> userList) {
		// TODO Auto-generated method stub

		String tableBody = "";
		String tableHeader = "<table style='border: 1px solid black; border-collapse: collapse;font-family:Times New Roman;'><tr><th style='border: 1px solid black;background-color: #0000FF; color: white;padding:5px;'>&nbsp;Developer&nbsp;</th>"
				+ "<th style='border: 1px solid black; padding:5px; background-color: #0000FF; color: white;'> &nbsp; Count &nbsp;</th>";

		Iterator<String> itr = userList.keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			tableBody += "<tr><td style='border: 1px solid black;'>" + key
					+ "</td><td style='border: 1px solid black; padding:2px;'>" + userList.get(key) + "</td></tr>";
		}

		return tableHeader + tableBody + "</table>";
	}

	/*
	 * private static void writeResult(ArrayList<Result> resultList) throws
	 * Exception { FileWriter fileWriter=new FileWriter(new File("output.txt"));
	 * BufferedWriter bufferFileWriter=new BufferedWriter(fileWriter);
	 * 
	 * for(Result result:resultList) { bufferFileWriter.newLine();
	 * bufferFileWriter.write(result.getAgentname()+" "+result.getUserName()+" "
	 * +" "+result.getDateString()); bufferFileWriter.newLine(); }
	 * bufferFileWriter.close();
	 * 
	 * }
	 */
	private static ArrayList<String> getAgentList() throws Exception {
		ArrayList<String> ils_agents = new ArrayList<String>();
		FileReader fileReader = new FileReader(new File("agentList.txt"));
		// System.out.println("getagentList");
		BufferedReader bufferFileReader = new BufferedReader(fileReader);
		String agentName = "";
		while ((agentName = bufferFileReader.readLine()) != null) {
			ils_agents.add(agentName);
		}
		bufferFileReader.close();
		return ils_agents;
	}

	private static ArrayList<Result> getResult(IServer server, String[] filePaths, HashMap<String, Integer> userList)
			throws Exception {
		ArrayList<Result> resultList = new ArrayList<>();
		GetChangelistsOptions getChangelistsOptions = new GetChangelistsOptions();
		getChangelistsOptions.setMaxMostRecent(50);

		for (String file : filePaths) {
			List<IFileSpec> fileList = server.getDepotFiles(FileSpecBuilder.makeFileSpecList(file), false);

			List<IChangelistSummary> changelistSummaries = server.getChangelists(fileList, getChangelistsOptions);
			
			Date today = new Date();
			long diffTime = 24 * 60 * 60 * 1000;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy :hh:mm");

			for (IChangelistSummary summary : changelistSummaries) {
				if (summary.getDescription().contains("PROD_CERTIFIED")
						&& (today.getTime() - summary.getDate().getTime() <= diffTime)
						&& userList.get(summary.getUsername()) != null) {
					
					Result result = new Result();
					result.setAgentname(file.substring(file.lastIndexOf("/") + 1));
					result.setUserName(summary.getUsername());
					result.setDateString(sdf.format(summary.getDate()));
					result.setComment(summary.getDescription());
					resultList.add(result);
					userList.put(summary.getUsername(), userList.get(summary.getUsername()) + 1);
					
				}

			}
		}

		printUserList(userList);

		return resultList;
	}

	private static void printUserList(HashMap<String, Integer> userList) {
		// TODO Auto-generated method stub
		Iterator<String> itr = userList.keySet().iterator();

		System.out.println("sizeOfuserList" + userList.size());
		while (itr.hasNext()) {
			String userName = itr.next();
			System.out.println(userName + "  " + userList.get(userName));
		}
	}

	private static HashMap<String, Integer> getUserList() throws Exception {

		HashMap<String, Integer> userList = new HashMap<String, Integer>();
		FileReader fileReader = new FileReader(new File("userAlias.txt"));
		BufferedReader bufferFileReader = new BufferedReader(fileReader);
		String userName = "";
		while ((userName = bufferFileReader.readLine()) != null) {
			userList.put(userName, 0);
		}
		bufferFileReader.close();
		return userList;
	}
}
