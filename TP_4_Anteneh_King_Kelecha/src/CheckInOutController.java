import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

public class CheckInOutController
{
	EventLog newEvent = new EventLog();

	protected void checkOut(Patron activePatron, Copy copyToCheckOut)
	{
		// Record copy out transaction
		activePatron.setCopiesStillOut(activePatron, copyToCheckOut);

		// Marking who is checking the copy out .

		copyToCheckOut.setOutTo(activePatron);

		// Set a due date
		LocalDateTime dueDate = LocalDateTime.now().plusDays(120);

		copyToCheckOut.setDueDate(dueDate);

		// create event instance

		newEvent.createCheckOutLog(copyToCheckOut);

	}

	protected void checkIn(Patron activePatron, Copy copyToCheckIn)
	{

		// create event instance
		newEvent.createCheckInLog(copyToCheckIn);

		// remove CopyOut property from Patron record
		// activePatron.getCopiesStillOut().remove(copyToCheckIn);

		// remove OutTo property from Copy record
		copyToCheckIn.setOutTo(null);

	}

	protected ArrayList<String> printEventLog()
	{
		return newEvent.getEventLogs();
	}

	protected Map<String, Patron> printAllPatrons()
	{
		Map<String, Patron> tempPatronStore = FakeDB.getPatronStore();
		return tempPatronStore;

	}

	public Map<String, Copy> printAllCopies()
	{
		Map<String, Copy> tempCopyStore = FakeDB.getCopyStore();
		return tempCopyStore;

	}

	public boolean markHold(Copy copyOnHold, Patron activePatron, String typeOfHold, String reason)
	{
		// Check if this patron checked out at least one copy
		if (activePatron.getCopiesStillOut() == null)
		{
			return false;
		}

		// Check if this patron checked out this particular copy to be
		// marked on hold
		if (!(activePatron.getCopiesStillOut().contains(copyOnHold)))
		{
			return false;
		}

		// Create new hold
		Hold newHold = new Hold(typeOfHold, reason);

		copyOnHold.setHoldTobeAdded(newHold);

		// Save this to a DB
		FakeDB.setPatronHolds(copyOnHold, copyOnHold.getCopyHolds());

		// create log record
		newEvent.createMarkHoldLog(copyOnHold, newHold, activePatron);

		// Attach fine with the hold
		applyFine(activePatron, newHold);

		return true;

	}

	public void applyFine(Patron activePatron, Hold newHold)
	{
		Fine newFine = new Fine();
		String holdType = newHold.getHoldType();

		if (holdType.equalsIgnoreCase("Damage"))
		{
			newFine.applyDamageFee(activePatron);
		}
		else if (holdType.equalsIgnoreCase("Book Dumping"))
		{
			newFine.applyDumpingFee(activePatron);
		}
		else if (holdType.equalsIgnoreCase("Overdue"))
		{
			newFine.applyOverDueFee(activePatron);
		}

	}

	// Check if there is any hold in the system
	public Map<Copy, ArrayList<Hold>> checkHoldsRecord()
	{

		return FakeDB.getHoldStore();
	}

	// Check if there is a hold on a specific patron. If there is,
	// retrieve all and return the list
	protected Map<Copy, ArrayList<Hold>> checkHoldsRecord(Patron activePatron)
	{

		Map<Copy, ArrayList<Hold>> tempHoldStore = new HashMap<Copy, ArrayList<Hold>>();

		// Check if there is at least one hold record
		if (FakeDB.getHoldStore() != null & !FakeDB.getHoldStore().isEmpty())
		{
			// A given copy might have two or more holds, so retrieve all
			// holds and return
			if (activePatron.getCopiesStillOut() != null)
			{
				for (int i = 0; i < activePatron.getCopiesStillOut().size(); i++)
				{
					// check if the active patron exist in the hold store
					if (FakeDB.getHoldStore().containsKey(activePatron.getCopiesStillOut().get(i)))
					{
						tempHoldStore.put(activePatron.getCopiesStillOut().get(i),
								FakeDB.getHoldRecord(activePatron.getCopiesStillOut().get(i)));
					}
				}
			}
			else
			{
				return tempHoldStore;
			}

			return tempHoldStore;

		}
		else
		{
			return null;
		}

	}

	protected Patron checkPatronExist(String newPatronID)
	{
		// Call to DB to check patron exist
		return FakeDB.getPatron(newPatronID);

	}

	protected Copy checkCopyExist(String newCopyID)
	{
		// Call to DB to check patron exist
		return FakeDB.getCopy(newCopyID);

	}

	protected String printPatronRecord(Patron activePatron)
	{

		return "Name: " + activePatron.getName() + "\nPatron ID: " + activePatron.getPatronID();

	}

	protected String isCopyCheckedOut(Copy activeCopy)
	{
		if (activeCopy.getOutTo() != null && activeCopy.getOutTo().getCopiesStillOut() != null)
		{
			return activeCopy.getOutTo().getName();
		}
		else
		{
			return null;
		}
	}

	public String printCheckedOutCopies(Patron activePatron)
	{
		String copiesCheckedOut = null;
		ArrayList<Copy> tempCopiesOut = activePatron.getCopiesStillOut();

		if (activePatron.getCopiesStillOut() != null)
		{

			for (int i = 0; i < tempCopiesOut.size(); i++)
			{
				if (tempCopiesOut.get(i).getOutTo() != null)
				{
					copiesCheckedOut += tempCopiesOut.get(i).getTitle() + " ( " + tempCopiesOut.get(i).getCopyID()
							+ " )\n";
				}
			}
			return copiesCheckedOut;
		}
		else
		{
			return null;
		}

	}

	public String[] getTypeOfHold()
	{
		return Hold.typeOfHold;

	}

	public void markHold()
	{
		Map<String, Copy> tempCopyStore = FakeDB.getCopyStore();

		for (String key : tempCopyStore.keySet())
		{
			// Check if the copy is checked out to at least 1 patron
			if (tempCopyStore.get(key).getOutTo() != null)
			{
				// Mark hold for copies overdue
				if (tempCopyStore.get(key).getDueDate().isBefore(LocalDateTime.now()))
				{
					this.markHold(tempCopyStore.get(key), tempCopyStore.get(key).getOutTo(), "overdue",
							"Book is not returned on time");
				}
			}
		}

	}

	public boolean isFeePaid(Patron activePatron)
	{
		// code to automatically call payment whenever a worker try to remove a
		// hold
		FakeDB.getFineStore().get(activePatron).payment(activePatron);

		// check if fee is paid
		if (FakeDB.getFineStore().get(activePatron).fee == 0)
		{

			return true;
		}
		else
		{
			return false;
		}

	}

	public void removeHold(Copy activeCopy, String holdType)
	{
		for (int i = 0; i < FakeDB.getHoldStore().size(); i++)
		{
			if (FakeDB.getHoldRecord(activeCopy).get(i).getHoldType().equalsIgnoreCase(holdType))
			{
				FakeDB.getHoldStore().remove(activeCopy);
				break;

			}

		}

		// create event log record
		newEvent.createRemoveHoldLog(activeCopy);
	}

	public boolean printNotice()
	{
		Map<Copy, ArrayList<Hold>> tempholdStore = FakeDB.getHoldStore();
		String overdueNotice = "";

		if (!tempholdStore.isEmpty())
		{
			// For each copy in the hold store
			for (Copy key : tempholdStore.keySet())
			{

				// Find if there is any overdue hold to that copy, retrieve the
				// info
				for (int i = 0; i < FakeDB.getHoldRecord(key).size(); i++)
				{
					if (FakeDB.getHoldRecord(key).get(i).getHoldType().equalsIgnoreCase("overdue"))
					{
						overdueNotice += key.getOutTo().getName() + " you haven't returned the copy of book - "
								+ key.getTitle() + "(" + key.getCopyID() + ")"
								+ " and your account has been charged <<Some Purchasing Price>>. You can keep the book!\n";
					}

				}

				// For each patron who has overdue hold for that copy, print
				// a file

				String sendTo = key.getOutTo().getName();

				generateNotice(overdueNotice, sendTo);

			}
			return true;

		}

		else
		{
			return false;
		}

	}

	protected void generateNotice(String overdueNotice, String sendTo)
	{
		final Formatter noticeFormater;
		try
		{
			noticeFormater = new Formatter("overdueNotice_" + sendTo + ".txt");

			noticeFormater.format("%s", overdueNotice);

			noticeFormater.close();

		}
		catch (FileNotFoundException e)
		{

			StdOut.println("Unable to create a file, please try again");
		}
	}
}
