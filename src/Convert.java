import java.io.File;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class Convert {
	
	public static void convertFile(String filename) throws FileNotFoundException { 
		 File myObj = new File("triplog.csv");
		 PrintWriter writer = new PrintWriter(myObj);
		File filePath = new File(filename);
		int i = -5;
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			br.readLine();
			br.readLine();
			br.readLine();
			String line;
			writer.println("Time,Latitude,Longitute");
			while ((line = br.readLine()) != null) {
				String[] latAndLon = line.split("\"");
				if (line.indexOf("\"") != -1) {
					String str1 = latAndLon[1];
					String str2 = latAndLon[3];
					str1 = str1.replaceAll(" ","");
					str2 = str2.replaceAll(" ","");
					str1 = str1.replaceAll("	","");
					str2 = str2.replaceAll("	","");
					str1 = str1.replace("?","");
					str2 = str2.replace("?","");
					i = i + 5;
					writer.println(i + "," + str1 + "," + str2);
//					System.out.println(i + "," + str1 + "," + str2);
//					System.out.println(Double.parseDouble(latAndLon[1]) + "," + Double.parseDouble(latAndLon[3]));
				}
			}
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}

