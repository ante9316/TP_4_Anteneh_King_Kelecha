
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
	private CheckInOutController controller = new CheckInOutController();

	public static void main(String[] args) throws InterruptedException
	{
		boolean notValidChoice = false;
		TRLApp trlApp = new TRLApp();
		do
		{

			String userInput;
			StdOut.println("\n*************** Main Menu*****************");
			StdOut.println("*  Press 1 : To CHECK OUT Books\t\t *");
			StdOut.println("*  Press 2 : To CHECK IN Books\t\t *");
			StdOut.println("*  Press 3 : To See HOW to Run the App\t *");
			StdOut.println("*  Press 4 : To See EVENT Log    \t *");
			StdOut.println("*  Press 5 : To Mark HOLDS on Students  *");
			StdOut.println("*  Press 6 : To Remove HOLDS from Students*");
			StdOut.println("*  Press 0 : To EXIT the Application\t *");
			StdOut.println("******************************************\n");

			Map<Copy, ArrayList<Hold>> holdRecord = new HashMap<Copy, ArrayList<Hold>>();
			userInput = StdIn.readLine();
			if (userInput.equals("1"))
			{

				StdOut.println("\t~~~~Copy Check Out Session~~~~");

				// validate patron
				Patron activePatron = validatePatron(trlApp.controller);

				// print current patron record
				StdOut.println("\nCurrent Patron's Record:");

				StdOut.println(trlApp.controller.printPatronRecord(activePatron));

				StdOut.println("\nChecked Out Copies So Far: ");
				String copiesCheckedOut;
				copiesCheckedOut = trlApp.controller.printCheckedOutCopies(activePatron);

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

				holdRecord = trlApp.controller.checkHoldsRecord(activePatron);

				if (holdRecord != null && !holdRecord.isEmpty())
				{
					// Print all hold info for each unique copy for
					printAllHold(holdRecord);

					StdOut.println(
							"\n\n**** SORRY!, You can't check out a copy until the hold is removed from your account.**** \n\n**** Go Pay the fine and remove the hold ****");
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
					Copy activeCopy = validateCopies(trlApp.controller);

					// Check if the copy is already checked OUT by another
					// Patron
					String checkedOutTo = trlApp.controller.isCopyCheckedOut(activeCopy);
					if (checkedOutTo != null)
					{
						StdOut.println("\nSorry, this book is already checked out by: " + checkedOutTo);
					}
					else
					{
						StdOut.println("Checking Out. Please wait..");
						Thread.sleep(500);

						trlApp.controller.checkOut(activePatron, activeCopy);

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

				Patron activePatron = validatePatron(trlApp.controller);

				trlApp.controller.printPatronRecord(activePatron);

				boolean nextCheckOut = false;

				do
				{
					StdOut.println("\nPlease Scan a Copy to Check In: ");
					Copy activeCopy = validateCopies(trlApp.controller);

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

							trlApp.controller.checkIn(activePatron, activeCopy);

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

				Map<String, Patron> tempPatronStore = trlApp.controller.printAllPatrons();
				for (String key : tempPatronStore.keySet())
				{
					System.out.println(
							tempPatronStore.get(key).getPatronID() + " , " + tempPatronStore.get(key).getName());
				}
				StdOut.println("\nList of available Copies in the System:");

				Map<String, Copy> tempCopyStore = trlApp.controller.printAllCopies();
				for (String key : tempCopyStore.keySet())
				{
					System.out.println(tempCopyStore.get(key).getCopyID() + " , " + tempCopyStore.get(key).getTitle());
				}

				StdOut.println("\nList of HOLDS in the System:");
				holdRecord = trlApp.controller.checkHoldsRecord();

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

				ArrayList<String> tempEventLog = trlApp.controller.printEventLog();
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

				// Mark hold on all patrons with overdue copies
				trlApp.controller.markHoldOnOverDueCopies();

				// validate copies
				Copy activeCopy = validateCopies(trlApp.controller);

				// validate patron
				StdOut.println("Please Enter the Patron ID you want to mark hold against:");
				Patron activePatron = validatePatron(trlApp.controller);

				StdOut.println("Please select the type of hold to mark:");

				String[] tempHoldType = trlApp.controller.getTypeOfHold();

				for (int i = 0; i < tempHoldType.length; i++)
				{
					StdOut.println("\"" + tempHoldType[i] + "\"");
				}

				String holdType = StdIn.readLine();

				StdOut.println("Please write a description or type \"None\"");
				String description = StdIn.readLine();

				if (trlApp.controller.markHold(activeCopy, activePatron, holdType, description))
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
				// Check if the fee is paid
				// Remove the record from FakeDB.getHoldStore()

				// CHeck why a hold is removed when patron check a book in
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
				StdOut.println("\nThere is a \"" + holdRecord.get(key).get(i).getHoldType()
						+ "\" hold against a copy of " + key.getTitle() + " (" + key.getCopyID() + ")");
			}

		}

	}

	protected static Copy validateCopies(CheckInOutController controller)
	{
		boolean notValidID = true;
		Copy activeCopy;
		do
		{
			activeCopy = controller.createCopy();

			String newCopyID = StdIn.readLine().toUpperCase();

			// Check if the entered Copy is in the DB
			activeCopy = controller.checkCopyExist(newCopyID);
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

	protected static Patron validatePatron(CheckInOutController controller)
	{
		boolean notValidID = true;
		Patron activePatron;

		activePatron = controller.createPatron();
		StdOut.println("Please enter the patron ID:");
		do
		{
			String newPatronID = StdIn.readLine().toUpperCase();

			// Check if the entered patron is in the DB
			activePatron = controller.checkPatronExist(newPatronID);

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
