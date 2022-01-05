import java.io.*;
import java.util.StringTokenizer;
import java.util.Arrays;

public class LamportVerify {
	static int event[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 }, rows = 0; // initialise the matrix with 0
	static int logicalevent[][] = new int[15][25]; // store temporary event values
	static String finalArray[][]; // final output event values
	static int senderCount, receiveCount = 0, incorrectInputFlag = 0, lcountr,maxCol; // send event,receive event incrementers
	static int internalEvent = 97; // characters for internal event, 97 refers to 'a'

	public static void main(String[] args) throws IOException {
		// read the file to get the input
		FileInputStream filestream = new FileInputStream("verify.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(filestream));
		String strLine; // to detect the end of file
		int i, j; // loop incrementers
		StringTokenizer st; // read a value from file
		// Read File Line By Line
		while ((strLine = br.readLine()) != null) { // Print the content on the console
			st = new StringTokenizer(strLine);
			if (Arrays.asList("P1", "P2", "P3","P4","P5", ":").contains(st.nextToken()))
				st.nextToken();
			while (st.hasMoreTokens()) {
				logicalevent[rows][event[rows]] = Integer.parseInt(st.nextToken());
				event[rows]++;
			}
			rows++;
		}
		System.out.println();
		System.out.println("Given Logical Values to verify:");
		for (i = 0; i < rows; i++) {
			for (j = 0; j < event[i]; j++)
				System.out.print(logicalevent[i][j] + " ");
			System.out.println();
		}
		br.close();// close the reader
		assignSenderReceiverValues(rows);
	}

	// get the receive events by finding the difference between the values
	// difference is huge - make it as a receiver event
	public static void assignSenderReceiverValues(int rows) {
		int rowValue, col;
		finalArray = new String[5][25];

		// search for logicalcount-1 for each logicalcount if it is not found print
		// error and exit
		for (rowValue = 0; rowValue < rows; rowValue++) {
			for (col = 0; col < (event[col]); col++) {
				lcountr = logicalevent[rowValue][col];
				incorrectInputFlag = searchValueOfReceiver(lcountr);
				if (incorrectInputFlag == 1) // incorrect input
					break;
			}
			if (incorrectInputFlag == 1) // incorrect input
				break;
		}

		// count total number of receiver and assign it to sender event
		for (rowValue = 0; rowValue < rows; rowValue++) {
			for (col = 0; col < event[rowValue]; col++) {
				if (col == 0) // 1st column
				{
					if (logicalevent[rowValue][col] == 1) {
						continue;
					} else {
						receiveCount++; // increment receiver count
					}
				} else if (logicalevent[rowValue][col] != logicalevent[rowValue][col - 1] + 1) // got the receive evnt
				{
					receiveCount++; // position of the receiver event
				}
			}
		}
		senderCount = receiveCount + 1; // position of sender event

		// Updating the values in the final output array
		for (rowValue = 0; rowValue < rows; rowValue++) {
			for (col = 0; col < event[rowValue]; col++) {
				if (col == 0) // 1st column
				{
					// calculate the 1st internal or send events
					if (logicalevent[rowValue][col] == 1) {
						if (rowValue == 0 ) {
							finalArray[rowValue][col] = Character.toString(internalEvent);
							if((logicalevent[rowValue][col+1] - logicalevent[rowValue][col] !=1 )){
							internalEvent = internalEvent + 2;
							}else{
								internalEvent = internalEvent + 1;
							}
						} else {
							finalArray[rowValue][col] = Character.toString(internalEvent - 1);
							maxCol = event[rowValue];
							if((finalArray[rowValue-1][maxCol-1]).equals(finalArray[rowValue][col])){
								finalArray[rowValue][col] = Character.toString(internalEvent);
							}
							internalEvent = internalEvent + 1;
						}
					} else {
						searchSender(rowValue, (logicalevent[rowValue][col] - 1));
						finalArray[rowValue][col] = "r" + (senderCount - 1);
					}
				} else if (logicalevent[rowValue][col] == 0) { // NULL case
					finalArray[rowValue][col] = "NULL";
				} else if (logicalevent[rowValue][col] - 1 != logicalevent[rowValue][col - 1]) // receive event is hit
				{
					searchSender(rowValue, (logicalevent[rowValue][col] - 1));
					finalArray[rowValue][col] = "r" + (senderCount - 1);
				} else if (logicalevent[rowValue][col] - 1 == logicalevent[rowValue][col - 1]
						&& finalArray[rowValue][col] == null) { // internal event
					finalArray[rowValue][col] = Character.toString(internalEvent - 1);
					if((finalArray[rowValue][col-1]).equals(finalArray[rowValue][col])){
						finalArray[rowValue][col] = Character.toString(internalEvent);
					}
					internalEvent = internalEvent + 1;
				}
			}
		}

		// for printing the result
		if (incorrectInputFlag == 1) {
			System.out.println("Printing Output:");
			System.out.println("INCORRECT");
		} else {
			System.out.println();
			System.out.println("Printing Output");
			for (rowValue = 0; rowValue < rows; rowValue++) {
				for (col = 0; col < event[rowValue]; col++) {
					System.out.print(finalArray[rowValue][col] + " ");
				}
				System.out.println();
			}
		}
	}

	// logical values for receiver and detects if the given input is correct or not
	public static int searchValueOfReceiver(int receiveValue) {
		int rowValue, col;
		for (rowValue = 0; rowValue < rows; rowValue++) {
			for (col = 0; col < event[rowValue]; col++) {
				if (receiveValue == 1)
					return 0;
				else {
					if (logicalevent[rowValue][col] == receiveValue - 1)
						return 0;
				}
			}
		}
		return 1; // which means the input given is incorrect
	}

	// search for the sender event corresponding to the receive event
	public static void searchSender(int pr, int lc) {
		int i, j;
		for (i = 0; i < rows; i++) {
			if (i != pr) { // sender is not in the same process
				for (j = 0; j < event[i]; j++) {
					if (logicalevent[i][j] == lc && (senderCount - 2) != 0) {
						senderCount--;// decrease the count of sender event
						finalArray[i][j] = "s" + (senderCount - 1);
					}
				}
			}
		}
	}
}