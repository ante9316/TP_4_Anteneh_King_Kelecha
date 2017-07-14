import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckInOutController
{

	protected void checkOut(Patron activePatron, Copy copyToCheckOut)
	{
		// Record copy out transaction
		activePatron.setCopiesStillOut(activePatron, copyToCheckOut);

		// Marking who is checking the copy out .

		copyToCheckOut.setOutTo(activePatron);

		// Set a due date
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime dueDate = LocalDateTime.now().plusDays(120);

		copyToCheckOut.setDueDate(dueDate);

		// create log record
		Event.createCheckOutLog(copyToCheckOut, now);

	}

	protected void checkIn(Patron activePatron, Copy copyToCheckIn)
	{

		// create log record
		Event.createCheckInLog(copyToCheckIn);

		// remove CopyOut property from Patron record

		activePatron.getCopiesStillOut().remove(copyToCheckIn);

		// remove OutTo property from Copy record
		copyToCheckIn.setOutTo(null);

	}

	protected ArrayList<String> printEventLog()
	{
		return Event.getEventLogs();
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

	public boolean markHold(Copy activeCopy, String typeOfHold, String reason)
	{
		// Check if the copy ever checked out. If never checked out, no hold can
		// be applied
		if (activeCopy.getOutTo() != null)
		{
			// Create new hold
			Hold newHold = new Hold(reason, typeOfHold);

			activeCopy.setHoldTobeAdded(newHold);

			// Save this to a DB
			FakeDB.setPatronHolds(activeCopy, activeCopy.getHoldTobeAdded());

			// create log record
			// Event.createMarkHoldLog(activeCopy, newHold);

			return true;
		}
		else
		{
			return false;
		}
	}

	public void printAllHolds()
	{
		if (FakeDB.getHoldStore().isEmpty())
			StdOut.println("\n***There is NO HOLD on the System***");
		else
		{
			// TODO:

			// implement getAllHolds();
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
		ArrayList<Copy> tempCopiesOut = activePatron.getCopiesStillOut();

		Map<Copy, ArrayList<Hold>> tempHoldStore = new HashMap<Copy, ArrayList<Hold>>();

		if (tempCopiesOut != null)
		{
			for (int i = 0; i < tempCopiesOut.size(); i++)
			{
				// A given copy might have two or more holds, so retrieve all
				// holds and return
				if (FakeDB.getHoldStore().containsKey(tempCopiesOut.get(i)))
				{
					tempHoldStore.put(tempCopiesOut.get(i), FakeDB.getHoldRecord(tempCopiesOut.get(i)));

				}

			}

			return tempHoldStore;

		}
		else
		{
			return null;
		}

	}

	public Copy createCopy()
	{

		return new Copy();
	}

	public Patron createPatron()
	{

		return new Patron();
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
		String copiesCheckedOut = "";
		ArrayList<Copy> tempCopiesOut = activePatron.getCopiesStillOut();

		if (activePatron.getCopiesStillOut() != null)
		{
			for (int i = 0; i < tempCopiesOut.size(); i++)
			{
				copiesCheckedOut += tempCopiesOut.get(i).getTitle() + " ( " + tempCopiesOut.get(i).getCopyID() + " )\n";

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

	public void markHoldOnOverDueCopies()
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
					this.markHold(tempCopyStore.get(key), "Book is not returned on time", "overdue");
				}
			}
		}

	}

}
