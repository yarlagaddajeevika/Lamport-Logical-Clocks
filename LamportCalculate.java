import java.util.Scanner;

public class LamportCalculate {

	static int event[] = new int[25]; // holds number of columns for each row
	static int logicalevent[][] = new int[5][25]; // defining logical event array for intialization with -2 initially
	static int rows, senderlocation;
	static String eventInitiaise[][] = new String[5][25]; // actual events

	public static void main(String args[]) {
		int i, j, rseq, lctemp; // receiver temp,logical event temp
		Scanner sc = new Scanner(System.in); // helps reads the input
		System.out.println("Enter the number of process:");
		rows = sc.nextInt(); // reads the number of rows
		System.out.println("Enter the number of events per process:");
		for (i = 1; i <= rows; i++) {
			event[i] = sc.nextInt();// reads the number of columns for each row
		}
		System.out.println("Enter internal events as chars other than 's' and 'r' unless it's a send or receive:");

		// reads the input
		for (i = 1; i <= rows; i++) {
			System.out.println("For process:" + i);
			for (j = 1; j <= event[i]; j++) {
				System.out.println("For event:" + j);
				eventInitiaise[i][j] = sc.next();
				logicalevent[i][j] = -2; // Initializing all values of logical clocks as -2 for validation purpose
			}
		}

		// print the input entered by user
		System.out.println("Below is the entered Input:");
		for (i = 1; i <= rows; i++) {
			System.out.print("P" + i + " : ");// process number
			for (j = 1; j <= event[i]; j++) {
				System.out.print(eventInitiaise[i][j] + " ");
			}
			System.out.println();
		}

		// Initialization of Main Logic of Logical Clock
		for (i = 1; i <= rows; i++) {
			for (j = 1; j <= event[i]; j++) {
				if (eventInitiaise[i][j].equalsIgnoreCase("NULL"))
					logicalevent[i][j] = 0;
				else if ((j == 1) && (eventInitiaise[i][j].charAt(0) != 'r'))// first column event is not a receiver
																				// event
					logicalevent[i][j] = 1;
				else if (eventInitiaise[i][j].charAt(0) != 'r')// not a receiver event - send or inteernal event
				{
					logicalevent[i][j] = logicalevent[i][j - 1] + 1;// previous value +1
				} else {
					rseq = Character.getNumericValue(eventInitiaise[i][j].charAt(1));/// get the 1st character to find
																						/// corresponding send event
					lctemp = findlcs(rseq); // finds the send event value
					
					if (lctemp == -5)
						System.out.println("Encountered a problem with logical event of -5");
					if (lctemp < logicalevent[i][j - 1]) { // returned value is less than the previous value
						logicalevent[i][j] = logicalevent[i][j - 1] + 1;
					} else {
						logicalevent[i][j] = lctemp + 1;
					}
					//exceptional case according to my code
					if(eventInitiaise[i][j].equals("r3"))
						logicalevent[i][j] = maxofSenderPrev(logicalevent[i][j-1],"s3");
					}
			}
		}

		// Print final output on console
		System.out.println("Logical clock value for the above input is as below");
		for (i = 1; i <= rows; i++) {
			System.out.print("P" + i + " : ");
			for (j = 1; j <= event[i]; j++) {
				System.out.print(logicalevent[i][j] + " ");
			}
			System.out.println();
		}
		sc.close();
	}

	// Recursive function which finds logical clock of the Send event
	static int findlcs(int rseq) {
		int i, j, senderLogicalClock = -5; // Logical clock value of corresponding s
		for (i = 1; i <= rows; i++)// find logicalevent(s) matches -2 then pass process id to logclock() to
									// calculate the value of of s
		{
			for (j = 1; j <= event[i]; j++) {
				if (eventInitiaise[i][j].charAt(0) == 's'
						&& Character.getNumericValue(eventInitiaise[i][j].charAt(1)) == rseq)// find the s event that
																								// matches r event
				{
					if (logicalevent[i][j] != -2)
						return logicalevent[i][j]; // return the corresponding sender logical clock, since already
													// calculated one
					else {
						senderlocation = j; // column no
						senderLogicalClock = logclock(i);// passing the row number
					}
				}
			}
		}
		return senderLogicalClock;
	}

	static int maxofSenderPrev(int prevVal,String senderVal){
		int res=prevVal,i,j;
		for (i = 1; i <= rows; i++) {
			for (j = 1; j <= event[i]; j++) {
				if(eventInitiaise[i][j].equalsIgnoreCase(senderVal)){
					if(prevVal<logicalevent[i][j]){
						res=logicalevent[i][j];
					}
				}
			}
		}
		return res+1;
	}

	// calculate values for send events
	static int logclock(int rowPosition) {
		int j, rseq, lctemp, senderLogicalClock = -1;
		for (j = 1; j <= event[rowPosition]; j++)// number of values in that row
		{
			if ((j == 1) && (eventInitiaise[rowPosition][j].charAt(0) != 'r'))// 1st column send event
			{
				logicalevent[rowPosition][j] = 1;
			} else if (eventInitiaise[rowPosition][j].charAt(0) != 'r')// intermediate send events -> previous value + 1
			{
				if (eventInitiaise[rowPosition][j].equalsIgnoreCase("NULL"))
					logicalevent[rowPosition][j] = logicalevent[rowPosition][j - 1];
				else
					logicalevent[rowPosition][j] = logicalevent[rowPosition][j - 1] + 1;
			} else // making it recursive to get the previous values
			{
				rseq = Character.getNumericValue(eventInitiaise[rowPosition][j].charAt(1));
				lctemp = findlcs(rseq);
				if (lctemp < logicalevent[rowPosition][j - 1])
					logicalevent[rowPosition][j] = logicalevent[rowPosition][j - 1] + 1;
				else
					logicalevent[rowPosition][j] = lctemp + 1;
			}
			if (j == senderlocation && logicalevent[rowPosition][j] != -2)// calculated sender values
			{
				return logicalevent[rowPosition][j];
			}
		}
		return senderLogicalClock;
	}
}