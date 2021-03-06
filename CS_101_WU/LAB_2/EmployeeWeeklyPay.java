/* Jacob Howarth
 * CS-101 05L, Fall 2007
 * Professor Wu 10/17/07
 */
 
 /* Description: This program will calculate the net pay of an hourly employer using federal and state
  * tax deductions input by the user. The net pay is for one week of work done by the employee.
  */
  
  
import javax.swing.JOptionPane;
import java.util.Scanner;

public class EmployeeWeeklyPay {
	public static void main(String[] args) {
	
		String decision = "console";
		String console = "console";
		String windows = "windows";
		boolean whileFlag = true;
		
		while (whileFlag) { // while loop checks if the user input "console" or "windows" while handling all other bad input
			
			decision = JOptionPane.showInputDialog(null, "Would you like to use windows to enter " + 
							"input or the console? (please type console or windows): ", "Input Format",
							JOptionPane.QUESTION_MESSAGE); // window prompts user to decide which input is desired.
			
			if(decision.equalsIgnoreCase(console) || decision.equalsIgnoreCase(windows))
				whileFlag = !whileFlag; // if input is the string "console" or "windows" exit while loop.
			else
				JOptionPane.showMessageDialog(null, "You have entered incorrect input, please type \"console\" " +
													"or \"windows\" in the question box."); // if input is not "console" or "windows" print this
																									    // message and ask for reinput.
			}
		
		if (decision.equalsIgnoreCase(console)) // if the choice was the use of console I/O then call method inputScanner.
			inputScanner();
		else 
			inputJOptionPane();
			
  } // end main
  
  public static void inputScanner() { // function that gathers console input.
		
		String employeeFirstName = "";  /* -- Variable Table -- */
		String employeeLastName = "";	  /* employeeLastName = employees last name. */
		String numberOfHours, 			  /* employeeFirstName = employees first name. */
				 fedTax,			 			  /* fedTax = federal tax percentage as typed by user. */
				 stateTax,		 			  /* stateTax = state tax percentage as typed by user. */
				 hourlyPayRate; 			  /* hourlyPayRate = hourly wage of employee. */
		boolean checkInput = true; 	  /* numberOfHours = hours employee worked for current week. */
		double numOfHours = 0;			  /* checkInput = boolean flag variable for checking numbers and letters in input. */	
		double federalTaxes = 0;		  /* numOfHours = number of hours employee worked double conversion from numberOfHours string. */
		double stateTaxes = 0;			  /* federalTaxes = federal tax percentage double conversion from fedTax string. */
		double hourlyPay = 0;			  /* stateTaxes = state tax percentage double conversion from stateTax string. */
				 								  /* hourlyPay = hourly pay rate of employee double conversion from hourlyPayRate string. */
											
		Scanner inputName = new Scanner(System.in); // Scanner used to scan strings entered in console.
		
		while (checkInput) { // Enter while loop to check input, for example numbers in names and letters in numbers.
		
			System.out.print("Enter the name of the employee (First Name/Last Name): ");		
		   employeeFirstName = inputName.next(); // First name of employee
			employeeLastName = inputName.next(); // Last name of employee
		
			checkInput = digitCheck(employeeFirstName, employeeLastName); // Call digitCheck method to check the name for digits and special characters. Returns true if digit exists in employee name input and false if no digit.
			
		} // end while
		
		employeeFirstName = employeeFirstName; // statement that stores value of employeeFirstName from while into employeeFirstName string to prevent variable initialization compiler errors
		employeeLastName = employeeLastName; // statement that stores value of employeeLastName from while into employeeLastName string to prevent variable initialization compiler errors
		checkInput = !checkInput; // change checkInput to true to re-enter the next loop after being changed to false in the before loop.
		
		while (checkInput) { // while loop used to check the inputs for federal tax, hours worked, pay rate and state tax for numbers.
			
			String checkForLetters; // String used for checking the input for letters when passed to function checkLetter() when console is chosen input method.
			Scanner inputNumbers = new Scanner(System.in); // Scanner created to get input from console.
			 
			for (int j = 0; j <= 3; j++) { // for loop to check each input involving numbers for letters, this is used in order to print the appropriate error message when checkLetter() is called.
				boolean stopLoop = true; // boolean flag variable to exit loops when input is correct and not exit when input is incorrect from call method letterCheck() because it returns boolean.
				boolean outOfBound = true;  // boolean flag variable to check if federal tax percentage withhold and state tax withhold is greater than 1 or less than 0.
			
				if (j == 0) {
					while (stopLoop) { // while loops used to check if the input for number of hours worked, pay per hour, federal tax and state tax containe characters or special characters
						System.out.print("Enter number of hours worked for the week: ");
						numberOfHours = inputNumbers.next(); // prompt for input at console
						checkForLetters = numberOfHours;
						stopLoop = letterCheck(checkForLetters, j); // check number of hours input string for characters, if characters exist, letterCheck() returns true, if no characters letterCheck returns false. The returned value is stored in stopLoop boolean value and controls while loop.
							if (!stopLoop) // negate current value of stopLoop boolean flag i.e. if there were characters in input, don't parse otherwise parse numberOfHours into numOfHours as double.
								numOfHours = Double.parseDouble(numberOfHours);
					} // end while
				} // end if
				else if (j == 1) {
					while (stopLoop) { // same as first while loop above
						System.out.print("Enter pay rate per hour: ");
						hourlyPayRate = inputNumbers.next();
						checkForLetters = hourlyPayRate;
						stopLoop = letterCheck(checkForLetters, j);
							if(!stopLoop)
								hourlyPay = Double.parseDouble(hourlyPayRate);
					} // end while
				} // end if
				else if (j == 2) {
					while (stopLoop) {
						System.out.print("Enter Federal Tax Withholding Rate (in decimal): ");
						fedTax = inputNumbers.next();
						checkForLetters = fedTax;
						stopLoop = letterCheck(checkForLetters, j);
						
						if (!stopLoop) { // if stopLoop flag returns false for chars, check to see if it's input is greater than 1 or less than 0.
							federalTaxes = Double.parseDouble(fedTax);
							
							if (federalTaxes <= 0 || federalTaxes > 1) { // check the converted string double to see if it's bigger than 1 or less than 0.
								System.out.println("The Federal Tax Withholding Percentage must be greater than or equal to zero or less than 1, please re-enter.");
								stopLoop = !stopLoop; // throw error and change stopLoop to true to continue in the while loop and prompt for reinput.
							}
							else
								outOfBound = !outOfBound; // if no chars, or number is within 0 <= x < 1, then negate outOfBound to exit while loop. 
						} // end if	

					} // end while
				} // end else if
				else {
					while (stopLoop) {
						System.out.print("Enter State Tax Withholding Rate (in decimal): ");
						stateTax = inputNumbers.next();
						checkForLetters = stateTax;
						stopLoop = letterCheck(checkForLetters, j);
						
						if (!stopLoop) { // if stopLoop flag returns false for chars, check to see if it's input is greater than 1 or less than 0.
							stateTaxes = Double.parseDouble(stateTax);
							
							if (stateTaxes <= 0 || stateTaxes > 1) { // check the converted string double to see if it's bigger than 1 or less than 0.
								System.out.println("The State Tax Withholding Percentage must be greater than or equal to zero of less than 1, please re-enter");
								stopLoop = !stopLoop; // throw error and change stopLoop to true to continue in the while loop and prompt for reinput.
							}
							else
								outOfBound = !outOfBound; // if no chars, or number is within 0 <= x < 1, then negate outOfBound to exit while loop. 
						} // end if	

					} // end while
				} // end else
			
			} // end for
			
			checkInput = !checkInput; // turn check boolean flag to false to exit inputCheck loop if all input is verified correct.
			
			// Pass variables to method calculateNetPayConsole() that performs calculation of net pay for employee and outputs it to the console.
			calculateNetPayConsole(employeeFirstName, employeeLastName, numOfHours, hourlyPay, federalTaxes, stateTaxes);
			
		} // end checkInput while loop line 71 
	
  } // end inputScanner
  
  public static void inputJOptionPane() { // method for gathering all input using JOptionPane windows.
  
  		String employeeName = "";  	 /* -- Variable Table -- */
		String numberOfHours, 			 /* employeeName = employees name as typed by user. */
				 fedTax,			 			 /* fedTax = federal tax percentage as typed by user. */
				 stateTax,		 			 /* stateTax = state tax percentage as typed by user. */
				 hourlyPayRate; 			 /* hourlyPayRate = hourly wage of employee. */
		boolean checkInput = true;     /* numberOfHours = hours employee worked for current week. */
		double numOfHours = 0;			 /* numOfHours = hours employee worked for current week converted to double. */
		double federalTaxes = 0;		 /* federalTaxes = federal tax percentage converted to double. */
		double stateTaxes = 0;			 /* stateTaxes = state tax percentage converted to double. */
		double hourlyPay = 0;			 /* hourlyPay = employee pay per hour converted to double. */ 
												 /* checkInput = boolean flag variable for checking numbers and letters in input. */				 							
											
		while(checkInput) { // loop to check input of employee name for JOptionPane. Control boolean flag checkInput to exit loop only when input is correct.
			
			employeeName = JOptionPane.showInputDialog(null, "Enter the employee's name:", "Input Window", JOptionPane.QUESTION_MESSAGE);
			checkInput = digitCheckJOption(employeeName); // type in name of user in window and call digitCheck method for JOptionPane to check string for digits and special chars, method returns true for digits in input and false if no digits in input.
		
		} // end checkInput while
		
		employeeName = employeeName; // statement that stores value of employeeName from while into employeeName string to prevent variable initialization compiler errors
		checkInput = !checkInput; // switch checkInput boolean flag to true to enter checkInput loop for user input of numbers i.e. employee hourly pay, hours worked, etc.
		
		while (checkInput) { 
			
			String checkForLetters; // String used to check number inputs for letters.
		
			for (int j = 0; j <= 3; j++) { // loop four times for each input involving a number so the appropriate error messages for the input can be displayed.
				boolean stopLoop = true; // boolean flag variable to exit loops when input is correct and not exit when input is incorrect from call method letterCheckJOption() because it returns boolean.
				boolean outOfBound = true; // boolean flag variable to check if federal tax percentage withhold and state tax withhold is greater than 1 or less than 0.
			
				if (j == 0) {
					while (stopLoop) {
						numberOfHours = JOptionPane.showInputDialog(null, "Enter number of hours worked for the week:",
						"Input Window", JOptionPane.QUESTION_MESSAGE);
						checkForLetters = numberOfHours;
						stopLoop = letterCheckJOption(checkForLetters, j); // enter employees hours worked and call letterCheckJOption to check for chars and special chars, method returns true if a letter exists, false if no letter exists.
							if(!stopLoop) 
							numOfHours = Double.parseDouble(numberOfHours);
					} // end while stopLoop
				} // end if
				else if (j == 1) {
					while (stopLoop) {
						hourlyPayRate = JOptionPane.showInputDialog(null, "Enter pay rate per hour:",
						"Input Window", JOptionPane.QUESTION_MESSAGE);	
						checkForLetters = hourlyPayRate;
						stopLoop = letterCheckJOption(checkForLetters, j); // enter pay rate per hour and perform the same check.
							if(!stopLoop) 
								hourlyPay = Double.parseDouble(hourlyPayRate);
					} // end while stopLoop
				} // end else if
				else if (j == 2) {
					while (stopLoop && outOfBound) { // stopLoop boolean flag and outOfBound flag must be both true if error in Fed Tax input is incorrect, prompting for reinput until both are false, signifying correct input.
						fedTax = JOptionPane.showInputDialog(null, "Enter Federal Tax Withholding Rate (in decimal):", "Input Window",
						JOptionPane.QUESTION_MESSAGE);
						checkForLetters = fedTax;
						stopLoop = letterCheckJOption(checkForLetters, j); // enter Federal Tax Withhold % in decimal and check for chars, returns true for chars and false for non char input.
						
						if (!stopLoop) { // if stopLoop flag returns false for chars, check to see if it's input is greater than 1 or less than 0.
							federalTaxes = Double.parseDouble(fedTax);
							
							if (federalTaxes <= 0 || federalTaxes > 1) { // check the converted string double to see if it's bigger than 1 or less than 0.
								JOptionPane.showMessageDialog(null, "The Federal Tax Withholding Percentage must be a number between 0 and 1, please re-enter",
								"Error", JOptionPane.ERROR_MESSAGE);
								stopLoop = !stopLoop; // throw error and change stopLoop to true to continue in the while loop and prompt for reinput.
							}
							else
								outOfBound = !outOfBound; // if no chars, or number is within 0 <= x < 1, then negate outOfBound to exit while loop. 
						} // end if	

					} // end while
				} // end else if
				else {
					while (stopLoop && outOfBound) { // loop does the same as above with Federal Tax Withhold but this loop check State Tax Withhold.
						stateTax = JOptionPane.showInputDialog(null, "Enter State Tax Withholding Rate (in decimal):", "Input Window",
						JOptionPane.QUESTION_MESSAGE);
						checkForLetters = stateTax;
						stopLoop = letterCheckJOption(checkForLetters, j);
						
						if (!stopLoop) {
							stateTaxes = Double.parseDouble(stateTax);
							
							if (stateTaxes <= 0 || stateTaxes > 1) {
								JOptionPane.showMessageDialog(null, "The State Tax Withholding Percentage must be a number between 0 and 1, please re-enter",
								"Error", JOptionPane.ERROR_MESSAGE);
								stopLoop = !stopLoop;
							}
							else
								outOfBound = !outOfBound; 
						} // end if	
					} // end while
				} // end else if
				
			} // end for
			
			checkInput = !checkInput; // when all input is cleared, turn checkInput to false to exit loop at line 138
		
			// Pass variables to method calculateNetPayJOption that performs calculation of net pay for employee and displays it in a window.
			calculateNetPayJOption(employeeName, numOfHours, hourlyPay, federalTaxes, stateTaxes);
			
		} // end while at line 138

  } // end inputJOptionPane
  
  public static boolean digitCheck(String employeeFirstName, String employeeLastName) { // method that checks for digits and special charaters for input that requires only letters for console, method digitCheckJOption is the same, but used when windows is selected.
  
  		int asciiCharValue;				/* -- Variable Table -- */
  		char digitCheckFirstName;		/* digitCheckFirstName = Used to store a character from the string containing the employee's first name. */
  		char digitCheckLastName;		/* digitCheckLastName = Used to store a character from the string containing the employee's last name. */
		boolean isDigit = false;		/* asciiCharValue = holds ASCII value of the character at each stop in String array. */
		boolean stopLoop = true;		/* isDigit = boolean flag that is changed to true when digit is found, returned in the end */
												/* stopLoop = boolean flag that changes to false to exit for loop and prevent it from checking all characters in String array. */
												
		for (int i = 0; i <= employeeFirstName.length() - 1 && stopLoop; i++) { // Check each character in the first name of employee entered as a string.
				digitCheckFirstName = employeeFirstName.charAt(i); // Store character in index i of array and cast into int to get ASCII value.
				asciiCharValue = (int)digitCheckFirstName;
				
				if (asciiCharValue >= 33 && asciiCharValue <= 64 || asciiCharValue >= 91 && asciiCharValue <= 96 || asciiCharValue >= 123 && asciiCharValue <=126) { 
					System.out.println("The employees name must contain letters, please re-enter."); // if asciiCharValue is in these checked ranges prompt user that error is in input.
					isDigit = !isDigit; // switch isDigit boolean flag to true.
					stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
				} // end if
		
		} // end for
		
		for (int j = 0; j <= employeeLastName.length() - 1 && stopLoop; j++) { // Check each character in the last name of employee entered as a string.
				digitCheckLastName = employeeLastName.charAt(j); // Store character in index i of array and cast into int to get ASCII value.
				asciiCharValue = (int)digitCheckLastName;
				
				if (asciiCharValue >= 33 && asciiCharValue <= 64 || asciiCharValue >= 91 && asciiCharValue <= 96 || asciiCharValue >= 123 && asciiCharValue <=126) { 
					System.out.println("The employees name must contain letters, please re-enter."); // if asciiCharValue is in these checked ranges prompt user that error is in input.
					isDigit = !isDigit; // switch isDigit boolean flag to true.
					stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
				} // end if
		
		} // end for

		return isDigit; // return isDigit flag variable to checkInput boolean variable in inputScanner method.
		
  } // end digitCheck
  
  public static boolean digitCheckJOption(String employeeName) { // Method that checks for digits and special charaters for input that requires only letters. Called if JOptionPane was chosen for I/O and performs the same as digitCheck()
  
  		int asciiCharValue;				/* -- Variable Table -- */
  		char digitCheck;					/* digitCheck = Used to store a character from the String array. */
  		boolean isDigit = false;		/* asciiCharValue = holds ASCII value of the character at each stop in String array. */
		boolean stopLoop = true;		/* isDigit = boolean flag that is changed to true when digit is found, returned in the end */
												/* stopLoop = boolean flag that changes to false to exit for loop and prevent it from checking all characters in String array. */				
  				
												
		for (int i = 0; i <= employeeName.length() - 1 && stopLoop; i++) { // Check each character in String employeeName.
				digitCheck = employeeName.charAt(i);  // Store character in index i of array and cast into int to get ASCII value.
				asciiCharValue = (int)digitCheck;
				
				if (asciiCharValue >= 33 && asciiCharValue <= 64 || asciiCharValue >= 91 && asciiCharValue <= 96 || asciiCharValue >= 123 && asciiCharValue <=126) { 
					JOptionPane.showMessageDialog(null, "Employee names can only be letters, not numbers. Please re-enter", 
					"Error", JOptionPane.ERROR_MESSAGE); // if asciiCharValue is in these checked ranges display error window to user.
					isDigit = !isDigit; // switch isDigit boolean flag to true.
					stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
				} // end if
		
		} // end for
		
		return isDigit; 
		
  } // end digitCheckJOption

  
  public static boolean letterCheck(String checkForLetters, int j) {
  		
		int asciiCharValue;				/* -- Variable Table -- */
  		char letterCheck;					/* letterCheck = Used to store a character from the current position in the String array. */
  		boolean isLetter = false;		/* asciiCharValue = holds ASCII value of the character at each stop in String array. */
		boolean stopLoop = true;		/* isDigit = boolean flag that is changed to true when digit is found, returned in the end */
												/* stopLoop = boolean flag that changes to false to exit for loop and prevent it from checking all characters in String array. */
												
		for (int i = 0; i <= checkForLetters.length() - 1 && stopLoop; i++) { // Check each character in String employeeName.
				letterCheck = checkForLetters.charAt(i); // Store character in index i of array and cast into int to get ASCII value.
				asciiCharValue = (int)letterCheck;
				
				if (asciiCharValue >= 33 && asciiCharValue <= 45 || asciiCharValue == 47 || asciiCharValue >= 58 && asciiCharValue <= 126) { 
					if (j == 0) {
						System.out.println("The hours worked for the current week must be a number, please re-enter."); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
					}
					else if (j == 1) {
						System.out.println("The hourly pay rate must be a number, please re-enter."); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
					}
					else if (j == 2) {
						System.out.println("The Federal Tax Withholding Percentage must be a number, please re-enter."); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
					}
					else if (j == 3) {
						System.out.println("The State Tax Withholding Percentage must be a number, please re-enter."); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
 					}
					else 
					stopLoop = !stopLoop;

				}
		}
		
		return isLetter;
	
	}	// end letterCheck
	
	public static boolean letterCheckJOption(String checkForLetters, int j) {	
		
		int asciiCharValue;				/* -- Variable Table -- */
  		char letterCheck;					/* letterCheck = Used to store a character from the current position in the String array. */
  		boolean isLetter = false;		/* asciiCharValue = holds ASCII value of the character at each stop in String array. */
		boolean stopLoop = true;		/* isDigit = boolean flag that is changed to true when digit is found, returned in the end */
												/* stopLoop = boolean flag that changes to false to exit for loop and prevent it from checking all characters in String array. */
												
		for (int i = 0; i <= checkForLetters.length() - 1 && stopLoop; i++) { // Check each character in String employeeName.
				letterCheck = checkForLetters.charAt(i); // Store character in index i of array and cast into int to get ASCII value.
				asciiCharValue = (int)letterCheck;
				
				if (asciiCharValue >= 33 && asciiCharValue <= 45 || asciiCharValue == 47 || asciiCharValue >= 58 && asciiCharValue <= 126) { 
					if (j == 0) {
						JOptionPane.showMessageDialog(null, "The hours worked for the current week must be a number, please re-enter",
						"Error", JOptionPane.ERROR_MESSAGE); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
					}
					else if (j == 1) {
						JOptionPane.showMessageDialog(null, "The hourly rate of pay must be a number, please re-enter", 
						"Error", JOptionPane.ERROR_MESSAGE); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
					}
					else if (j == 2) {
						JOptionPane.showMessageDialog(null, "The Federal Tax Withholding Percentage must be a number, please re-enter", 
						"Error", JOptionPane.ERROR_MESSAGE); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
					}
					else if (j == 3) {
						JOptionPane.showMessageDialog(null, "The State Tax Withholding Percentage must be a number, please re-enter", 
						"Error", JOptionPane.ERROR_MESSAGE); // if asciiCharValue is in these checked ranges prompt user that error is in input.
						isLetter = !isLetter; // switch isDigit boolean flag to true.
						stopLoop = !stopLoop; // switch stopLoop boolean flag to false to exit loop early.
 					}
					else 
					stopLoop = !stopLoop;

				}
		}
		
		return isLetter;
	
	} // end letterCheckJOption
	
	public static void calculateNetPayConsole(String employeeFirstName, String employeeLastName, double numOfHours, double hourlyPay, double federalTaxes, double stateTaxes) { // function that calculates gross pay for employee after all input is checked. Used only when console I/O is chosen.
		
		double grossPay; 			/* -- Variable Table -- */
		double netPay;				/* grossPay = Employee pay per hour * number of hours worked. */
		double fedTaxDeduct;		/* netPay = Employee's payment minus deductions. */
		double stateTaxDeduct;	/* stateTaxDeduct = Employee's gross pay * state tax. */
		double totalDeductions; /* federalTaxDeduct = Employee's gross pay * federal tax. */
										/* totalDeductions = Employee's added federal and state deductions. */
		
		grossPay = numOfHours * hourlyPay;
		fedTaxDeduct = grossPay * federalTaxes;
		stateTaxDeduct = grossPay * stateTaxes;
		totalDeductions = fedTaxDeduct + stateTaxDeduct;
		netPay = grossPay - totalDeductions;
		
		System.out.println();
		
		System.out.println("\t\t\t --PAYROLL STATEMENT--  "); // print title.
		System.out.println("EMPLOYEE NAME: " + employeeFirstName + " " + employeeLastName); // print employee name.
		System.out.println("HOURS WORKED: " + numOfHours); // print hours worked.
		System.out.printf("PAY RATE: " + "$" + "%.2f\n", hourlyPay); // print pay per hour formatted to 2 places after the decimal.
		System.out.printf("GROSS PAY: " + "$" + "%.2f\n", grossPay); // print gross pay formatted to 2 places after the decimal.
		System.out.printf("DEDUCTIONS: \n" + "\t" + "FEDERAL WITHHOLDING " + "(" + (federalTaxes * 1e2) + "%%" + "): " + "$" + "%.2f\n", fedTaxDeduct); // print federal tax deduction percentage and the value of deduction to two places after the decimal.
		System.out.printf("\t" + "STATE WITHHOLDING " + "(" + (stateTaxes * 1e2) + "%%" + "): " + "$" + "%.2f\n", stateTaxDeduct); // print state tax deduction percentage and the value of deduction to two places after the decimal.
		System.out.printf("\t" + "TOTAL DEDUCTIONS: " + "$" + "%.2f\n", totalDeductions); // print total deductions to two places after decimal.
		System.out.printf("NET PAY: " + "$" + "%.2f\n", netPay); // print net pay to two places after the decimal.
	
	} // end calculateNetPayConsole 
	
	public static void calculateNetPayJOption(String employeeName, double numOfHours, double hourlyPay, double federalTaxes, double stateTaxes) { // function that calculates gross pay for employee after all input is checked. Used only when console I/O is chosen.
		
		double grossPay; 			/* -- Variable Table -- */
		double netPay;				/* grossPay = Employee pay per hour * number of hours worked. */
		double fedTaxDeduct;		/* netPay = Employee's payment minus deductions. */
		double stateTaxDeduct;	/* stateTaxDeduct = Employee's gross pay * state tax. */
		double totalDeductions; /* federalTaxDeduct = Employee's gross pay * federal tax. */
		String output = "";		/* totalDeductions = Employee's added federal and state deductions. */
										/* output = String of output displayed in JOption window. */
		
		grossPay = numOfHours * hourlyPay;
		fedTaxDeduct = grossPay * federalTaxes;
		stateTaxDeduct = grossPay * stateTaxes;
		totalDeductions = fedTaxDeduct + stateTaxDeduct;
		netPay = grossPay - totalDeductions;
		
		output = "EMPLOYEE NAME: " + employeeName + '\n'; // set ouput string to employee name.
		output += "HOURS WORKED: " + numOfHours + '\n'; // concatenate to output string hours worked.
		output += "PAY RATE: " + "$" + (int)(hourlyPay * 100) / 100.0 + '\n'; // concatenate pay per hour to output formatted to 2 places (max) after the decimal.
		output += "GROSS PAY: " + "$" + grossPay + '\n'; // concatenate gross pay to output.
		output += "DEDUCTIONS: \n" + "\t" + "FEDERAL WITHHOLDING " + "(" + (federalTaxes * 1e2) + "%" + "): " + "$" + (int)(fedTaxDeduct * 100) / 100.0 + '\n'; // concatenate federal tax deduction percentage and the value of deduction with max two places after decimal to ouput string.
		output += "\t" + "STATE WITHHOLDING " + "(" + (stateTaxes * 1e2) + "%" + "): " + "$" + (int)(stateTaxDeduct * 100) / 100.0 + '\n'; // concatenate state tax deduction percentage and the value of deduction with max two places after decimal to ouput string.
		output += "\t" + "TOTAL DEDUCTIONS: " + "$" + (int)(totalDeductions * 100) / 100.0 + '\n'; // concatenate total deductions with two max places after decimal to output.
		output += "NET PAY: " + "$" + (int)(netPay * 100) /100.0  + '\n'; // concatenate net pay with max two places after the decimal to output.
		
		JOptionPane.showMessageDialog(null, output, "--PAYROLL STATEMENT--", JOptionPane.INFORMATION_MESSAGE); // display results in info window titles PAYROLL STATEMENT.
	
	} // end calculateNetPayJOption  

} // end class EmployeeWeeklyPay