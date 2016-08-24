package TestPack;

import java.util.*;

public class MessageManager {
	public static void parseTWSIncoming(Scanner inputStream){
		while(inputStream.hasNextLine()){
			String nextLine = inputStream.nextLine();
			if(nextLine.contains("TWS RESTART") || nextLine.contains("Handling incoming"))
				System.out.println(nextLine);
			else
				continue;
		}
	}
}
