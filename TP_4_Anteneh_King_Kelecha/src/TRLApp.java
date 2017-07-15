
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
			printMenu();

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
					StdOut.println("\nPlease Enter a Copy to Check Out: ");
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

				notValidChoice = menuCheckIn(trlApp);
			}

			else if (userInput.equals("3"))
			{
				notValidChoice = menuHelp(trlApp);

			}
			else if (userInput.equals("4"))
			{
				notValidChoice = menuLogs(notValidChoice, trlApp);
			}

			else if (userInput.equals("5"))
			{
				notValidChoice = menuMarkHolds(trlApp);
			}

			else if (userInput.equals("6"))
			{
				notValidChoice = menuRemoveHolds(trlApp);

			}
			else if (userInput.equals("7"))
			{
				trlApp.controller.printNotice();
			}
			else if (userInput.equals("0"))
			{
				menuExit();

			}
			else
			{
				StdOut.println("Invalid input. Please select from the available options.\n");
				notValidChoice = true;
			}
		}
		while (notValidChoice);

	}

	private static void menuExit()
	{
		StdOut.println("Exiting...");
		StdOut.println("Application Closed Successfully. GoodBye!");
		System.exit(0);
	}

	private static boolean menuRemoveHolds(TRLApp trlApp)
	{
		boolean notValidChoice;
		// Accept and validate patron info
		Patron activePatron = validatePatron(trlApp.controller);

		StdOut.println("\nPlease Enter a Copy you want to remove a hold for: ");
		Copy activeCopy = validateCopies(trlApp.controller);

		// TODO: Validations; ASUMPTION: for now, patron has an hold and
		// fee

		StdOut.println("Please select the type of hold to remove:");
		String holdType = selectHoldType(trlApp);

		// Check if the fee is paid and remove hold record
		if (trlApp.controller.isFeePaid(activePatron))
		{
			trlApp.controller.removeHold(activeCopy, holdType);
			StdOut.println("Hold is removed from your record successfuly!");
		}
		else
		{
			StdOut.println("\n** Fine is not paid! Please go back and pay the fine **");
		}

		notValidChoice = true;
		return notValidChoice;
	}

	private static boolean menuMarkHolds(TRLApp trlApp)
	{
		boolean notValidChoice;
		StdOut.println("*******Marking a hold***************");
		StdOut.println(
				"*Note:\"Overdue\" hold is automatically marked on Students with overdue copies.*\nFor other type of holds, "
						+ "Please Enter the Copy ID :");

		// Mark hold on all patrons with overdue copies
		trlApp.controller.markHold();

		// validate copies
		Copy activeCopy = validateCopies(trlApp.controller);

		// validate patron
		Patron activePatron = validatePatron(trlApp.controller);

		StdOut.println("Please select the type of hold to mark:");
		String holdType = selectHoldType(trlApp);

		StdOut.println("Please write a description or type \"None\"");
		String reason = StdIn.readLine();

		if (trlApp.controller.markHold(activeCopy, activePatron, holdType, reason))
		{
			StdOut.print("A hold has been marked sucessfully!");
		}
		else
		{
			StdOut.print("Sorry, you can't mark a hold on copies never checked out");
		}

		notValidChoice = true;
		return notValidChoice;
	}

	private static boolean menuLogs(boolean notValidChoice, TRLApp trlApp)
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
		return notValidChoice;
	}

	private static boolean menuHelp(TRLApp trlApp)
	{
		boolean notValidChoice;
		Map<Copy, ArrayList<Hold>> holdRecord;
		StdOut.println("\t~~~~ Textbook Rental Library Help ~~~~");

		StdOut.println("List of available Patrons in the System:");

		Map<String, Patron> tempPatronStore = trlApp.controller.printAllPatrons();
		for (String key : tempPatronStore.keySet())
		{
			System.out.println(tempPatronStore.get(key).getPatronID() + " , " + tempPatronStore.get(key).getName());
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

		StdOut.println(
				"\n\n** ASSUMPTION: fees are automatically considered as paid when worker try to remove hold **");
		notValidChoice = true;
		return notValidChoice;
	}

	private static boolean menuCheckIn(TRLApp trlApp) throws InterruptedException
	{
		boolean notValidChoice;
		StdOut.println("\t~~~~Copy Check IN Session~~~~");

		Patron activePatron = validatePatron(trlApp.controller);

		trlApp.controller.printPatronRecord(activePatron);

		boolean nextCheckOut = false;

		do
		{
			StdOut.println("\nPlease Enter a Copy to Check In: ");
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
		return notValidChoice;
	}

	protected static String selectHoldType(TRLApp trlApp)
	{

		String[] tempHoldType = trlApp.controller.getTypeOfHold();

		for (int i = 0; i < tempHoldType.length; i++)
		{
			StdOut.println("\"" + tempHoldType[i] + "\"");
		}

		String holdType = StdIn.readLine();
		return holdType;
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
		Copy activeCopy = null;
		StdOut.println("Please enter the copy ID:");
		do
		{
			String newCopyID = StdIn.readLine().toUpperCase();
			// Check if the entered Copy is in the DB
			activeCopy = controller.checkCopyExist(newCopyID);
			if (activeCopy == null)
			{
				StdOut.println("You may have entered an invalid copy or unavailable copy. Please try again:");
			}

		}
		while (activeCopy == null);
		return activeCopy;
	}

	protected static Patron validatePatron(CheckInOutController controller)
	{
		Patron activePatron = null;
		StdOut.println("Please enter the patron ID:");
		do
		{
			String newPatronID = StdIn.readLine().toUpperCase();

			// Check if the entered patron is in the DB
			activePatron = controller.checkPatronExist(newPatronID);

			if (activePatron == null)
			{
				StdOut.println("You many have entered an invalid patron ID or unavailable patron. Please try again:");
			}

		}
		while (activePatron == null);
		return activePatron;
	}

	private static void printMenu()
	{
		StdOut.println("\n*************** Main Menu*******************");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 1 : To CHECK OUT Books\t\t   *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 2 : To CHECK IN Books\t\t   *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 3 : To See HOW to Run the App\t   *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 4 : To See EVENT Log    \t   *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 5 : To Mark HOLDS on Students     *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 6 : To Remove HOLDS from Students *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 7 : To PRINT OverDue Notice\t   *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("*  Press 0 : To EXIT the Application\t   *");
		StdOut.println("*\t\t\t\t\t   *");
		StdOut.println("********************************************\n");
	}
}
