
/** 
 * This is a Textbook Rental Library System developed by Ashenafi, Tafesse and  Stephanie
Date: June/July 2017
Class: SEIS 635 Software Analysis and Design

 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TRLApp
{

	public static void main(String[] args) throws InterruptedException
	{
		boolean notValidChoice = false;

		do
		{

			String userInput;
			StdOut.println("\n*************** Main Menu*****************");
			StdOut.println("*  Press 1 : To Check Out Books\t\t *");
			StdOut.println("*  Press 2 : To Check In Books\t\t *");
			StdOut.println("*  Press 3 : To See How to Run the App\t *");
			StdOut.println("*  Press 4 : To See Event Log    \t *");
			StdOut.println("*  Press 5 : To Mark a HOLD on Students  *");
			StdOut.println("*  Press 6 : To Remove a HOLD on Students*");
			StdOut.println("*  Press 0 : To Exit the Application\t *");
			StdOut.println("******************************************\n");

			CheckInOutController newTransaction = new CheckInOutController();
			Map<Copy, ArrayList<Hold>> holdRecord = new HashMap<Copy, ArrayList<Hold>>();
			userInput = StdIn.readLine();
			if (userInput.equals("1"))
			{

				StdOut.println("\t~~~~Copy Check Out Session~~~~");

				// validate patron
				Patron activePatron = validatePatron(newTransaction);

				// print current patron record
				StdOut.println("\nCurrent Patron's Record:");

				StdOut.println(newTransaction.printPatronRecord(activePatron));

				StdOut.println("\nChecked Out Copies So Far: ");
				String copiesCheckedOut;
				copiesCheckedOut = newTransaction.printCheckedOutCopies(activePatron);

				if (copiesCheckedOut != null)
				{
					StdOut.print(copiesCheckedOut.toString());
				}
				else
				{
					StdOut.println("\t-- None --");
				}

				// Check and display Hold info on Patron (if any)
				StdOut.println("\nHOLD on this account : ");

				holdRecord = newTransaction.checkHoldsRecord(activePatron);

				if (holdRecord != null && !holdRecord.isEmpty())
				{
					// Print all hold info for each unique copy for
					printAllHold(holdRecord);

					StdOut.println(
							"\n\n**** SORRY!, You can't check out a copy until the hold is removed from your account.**** \n**** Go Pay the fine and remove the hold ****");
					notValidChoice = true;
					continue;
				}
				else
				{
					StdOut.println("\t-- None --");
				}
				boolean nextCheckOut = false;

				do
				{
					// validate Copies exist
					StdOut.println("\nPlease Scan a Copy to Check Out: ");
					Copy activeCopy = validateCopies(newTransaction);

					// Check if the copy is already checked OUT by another
					// Patron
					String checkedOutTo = newTransaction.isCopyCheckedOut(activeCopy);
					if (checkedOutTo != null)
					{
						StdOut.println("\nSorry, this book is already checked out by: " + checkedOutTo);
					}
					else
					{
						StdOut.println("Checking Out. Please wait..");
						Thread.sleep(500);

						newTransaction.checkOut(activePatron, activeCopy);

						StdOut.println("\nCopies Checked Out Successfuly!");
						notValidChoice = true;
					}

					StdOut.println("\nDo you want to check out another copy? (Y/N)");
					String wantToContinue = StdIn.readLine();

					if (wantToContinue.equalsIgnoreCase("Y"))
					{
						nextCheckOut = true;

					}
					else
					{
						nextCheckOut = false;
					}
				}
				while (nextCheckOut);
			}

			else if (userInput.equals("2"))

			{

				StdOut.println("\t~~~~Copy Check IN Session~~~~");

				Patron activePatron = validatePatron(newTransaction);

				newTransaction.printPatronRecord(activePatron);

				boolean nextCheckOut = false;

				do
				{
					StdOut.println("\nPlease Scan a Copy to Check In: ");
					Copy activeCopy = validateCopies(newTransaction);

					// Check if the copy is already checked OUT by any Patron
					if (activeCopy.getOutTo() != null)
					{
						// Check if the copy is already checked OUT by this
						// Patron
						if (activeCopy.getOutTo().getPatronID() != activePatron.getPatronID())
						{
							StdOut.println("\nSorry, this book is not checked out by you: ");
							notValidChoice = true;
						}
						else
						{

							StdOut.println("\nChecking In. Please wait...");
							Thread.sleep(500);

							newTransaction.checkIn(activePatron, activeCopy);

							StdOut.println("\nCopies Checked In Successfuly!");
							notValidChoice = true;
						}
					}
					else
					{
						StdOut.println("\nSorry, this book is not checked out by anyone! ");
						notValidChoice = true;
					}

					StdOut.println("\nDo you want to check in another copy? (Y/N)");
					String wantToContinue = StdIn.readLine();

					if (wantToContinue.equalsIgnoreCase("Y"))
					{
						nextCheckOut = true;

					}
					else
					{
						nextCheckOut = false;
					}
				}
				while (nextCheckOut);
			}

			else if (userInput.equals("3"))
			{
				StdOut.println("\t~~~~ Textbook Rental Library Help ~~~~");

				StdOut.println("List of available Patrons in the System:");

				Map<String, Patron> tempPatronStore = newTransaction.printAllPatrons();
				for (String key : tempPatronStore.keySet())
				{
					System.out.println(
							tempPatronStore.get(key).getPatronID() + " , " + tempPatronStore.get(key).getName());
				}
				StdOut.println("\nList of available Copies in the System:");

				Map<String, Copy> tempCopyStore = newTransaction.printAllCopies();
				for (String key : tempCopyStore.keySet())
				{
					System.out.println(tempCopyStore.get(key).getCopyID() + " , " + tempCopyStore.get(key).getTitle());
				}

				StdOut.println("\nList of HOLDS in the System:");
				holdRecord = newTransaction.checkHoldsRecord();

				if (holdRecord != null && !holdRecord.isEmpty())
				{
					// Print all hold info for each unique copy for
					printAllHold(holdRecord);
				}
				else
				{
					StdOut.println("\t-- None --");
				}
				// Explain the Due Date Policy

				StdOut.println(
						"\n**** Library Policies ****\nA Copy is due after 120 days from the date it is checked out.\n\nIf there is a hold on your account, a worker can only remove the hold when the fine is paid");
				// Explain..other assumptions
				notValidChoice = true;

			}
			else if (userInput.equals("4"))
			{
				StdOut.println("\t~~~~Event Log~~~~");

				ArrayList<String> tempEventLog = newTransaction.printEventLog();
				if (tempEventLog.isEmpty())
				{
					StdOut.println("There is no Event Log at this time. Exiting the application...");
					notValidChoice = true;
				}
				else
				{
					StdOut.println(tempEventLog.toString());

				}
			}

			else if (userInput.equals("5"))
			{
				StdOut.println("*******Marking a hold***************");
				StdOut.println(
						"*Note:\"Overdue\" hold is automatically marked on Students with overdue copies.*\nFor other type of holds, "
								+ "Please Enter the Copy ID :");

				// Mark hold on all patron with overdue copies
				newTransaction.markHoldOnOverDueCopies();

				// validate copies
				Copy activeCopy = validateCopies(newTransaction);

				StdOut.println("Please select the type of hold to mark:");

				String[] tempHoldType = newTransaction.getTypeOfHold();

				for (int i = 0; i < tempHoldType.length; i++)
				{
					StdOut.println("\"" + tempHoldType[i] + "\"");
				}

				String holdType = StdIn.readLine();

				StdOut.println("Please write a description or type \"None\"");
				String description = StdIn.readLine();

				if (newTransaction.markHold(activeCopy, holdType, description))
				{
					StdOut.print("A hold has been marked sucessfully!");
				}
				else
				{
					StdOut.print("Sorry, you can't mark a hold on copies never checked out");
				}

				notValidChoice = true;
			}

			else if (userInput.equals("6"))
			{
				StdOut.println("This functionality is not implmented yet");
				notValidChoice = true;

			}
			else if (userInput.equals("0"))
			{
				StdOut.println("Exiting...");
				StdOut.println("Application Closed Successfully. GoodBye!");
				System.exit(0);

			}
			else
			{
				StdOut.println("Invalid input. Please select from the available options.\n");
				notValidChoice = true;
			}
		}
		while (notValidChoice);

	}

	// print all holds per a copy. a copy can have multiple holds
	protected static void printAllHold(Map<Copy, ArrayList<Hold>> holdRecord)
	{

		for (Copy key : holdRecord.keySet())
		{
			for (int i = 0; i < holdRecord.get(key).size(); i++)
			{
				StdOut.println("There is a \"" + holdRecord.get(key).get(i).getHoldType() + "\" hold against a copy of "
						+ key.getTitle() + " (" + key.getCopyID() + ") and was checked out by"
						+ key.getOutTo().getName() + " (" + key.getOutTo().getPatronID() + ")");
			}

		}

	}

	protected static Copy validateCopies(CheckInOutController newTransaction)
	{
		boolean notValidID = true;
		Copy activeCopy;
		do
		{
			activeCopy = newTransaction.createCopy();

			String newCopyID = StdIn.readLine().toUpperCase();

			// Check if the entered Copy is in the DB
			activeCopy = newTransaction.checkCopyExist(newCopyID);
			if (activeCopy == null)
			{
				StdOut.println("You may have scanned an invalid copy or unavailable copy. Please try again:");
				notValidID = true;
			}
			else
			{
				notValidID = false;
			}

		}
		while (notValidID);
		return activeCopy;
	}

	protected static Patron validatePatron(CheckInOutController newTransaction)
	{
		boolean notValidID = true;
		Patron activePatron;

		activePatron = newTransaction.createPatron();
		StdOut.println("Please enter the patron ID:");
		do
		{
			String newPatronID = StdIn.readLine().toUpperCase();

			// Check if the entered patron is in the DB
			activePatron = newTransaction.checkPatronExist(newPatronID);

			if (activePatron == null)
			{
				StdOut.println("You many have scanned an invalid patron ID or unavailable patron. Please try again:");
				notValidID = true;
			}
			else
			{
				notValidID = false;
			}

		}
		while (notValidID);
		return activePatron;
	}

}
