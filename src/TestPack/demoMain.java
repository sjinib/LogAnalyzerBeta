package TestPack;

import java.io.*;
import java.util.*;

public class demoMain {
	public static void main(String argv[]){
		Scanner inputStream = null;
		try{
			inputStream = new Scanner(new FileInputStream("C:\\Jts33\\dyizsoxdm\\tws.log"));
			System.out.println("File read successful");			
			MessageManager.parseTWSIncoming(inputStream);
		} catch(FileNotFoundException ex){
			System.out.println("File not found");
		}
	}
}
