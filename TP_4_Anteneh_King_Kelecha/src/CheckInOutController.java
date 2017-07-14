import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckInOutController
{
	Event newEvent = new Event();

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

		// create event instance

		newEvent.createCheckOutLog(copyToCheckOut, now);

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

	public boolean markHold(Copy copyOnHold, String typeOfHold, String reason)
	{
		// Check if the copy ever checked out. If never checked out, no hold can
		// be applied
		if (copyOnHold.getOutTo() != null)
		{
			// Create new hold
			Hold newHold = new Hold(reason, typeOfHold);

			copyOnHold.setHoldTobeAdded(newHold);

			// Save this to a DB
			FakeDB.setPatronHolds(copyOnHold, copyOnHold.getHoldTobeAdded());

			// create log record
			newEvent.createMarkHoldLog(copyOnHold, newHold);

			return true;
		}
		else
		{
			return false;
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
		// ArrayList<Copy> tempCopiesOut = activePatron.getCopiesStillOut();

		Map<Copy, ArrayList<Hold>> tempHoldStore = new HashMap<Copy, ArrayList<Hold>>();

		// Check
		if (FakeDB.getHoldStore() != null & !FakeDB.getHoldStore().isEmpty())
		{
			// A given copy might have two or more holds, so retrieve all
			// holds and return
			if (activePatron.getCopiesStillOut() != null)
			{
				for (int i = 0; i < activePatron.getCopiesStillOut().size(); i++)
				{
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
