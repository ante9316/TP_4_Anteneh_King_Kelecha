
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FakeDB
{
	private static Map<String, Patron> patronStore;
	private static Map<String, Copy> copyStore;
	private static Map<Copy, ArrayList<Hold>> holdStore;
	protected static Map<Patron, Double> fineStore;

	static // the following runs once when class is loaded: "static initializer"
	{
		patronStore = new HashMap<String, Patron>();
		copyStore = new HashMap<String, Copy>();
		fineStore = new HashMap<Patron, Double>();

		patronStore.put("P1", new Patron("P1", "Eric"));
		patronStore.put("P2", new Patron("P2", "Ash"));
		patronStore.put("P3", new Patron("P3", "Tafesse"));
		patronStore.put("P4", new Patron("P4", "Stephanie"));
		copyStore.put("C1", new Copy("C1", "Fun with Objects"));
		copyStore.put("C2", new Copy("C2", "More Fun with Objects"));
		copyStore.put("C3", new Copy("C3", "Introduction to Programming"));
		copyStore.put("C4", new Copy("C4", "Introduction to Big Data"));

		holdStore = new HashMap<Copy, ArrayList<Hold>>();

	}

	protected static Map<Patron, Double> getFineStore()
	{
		return fineStore;
	}

	public static Map<String, Patron> getPatronStore()
	{
		return patronStore;
	}

	public static Patron getPatron(String patronID)
	{
		return patronStore.get(patronID);
	}

	public static Copy getCopy(String copyID)
	{
		return copyStore.get(copyID);
	}

	public static void setPatronStore(Patron newPatron)
	{

		patronStore.put(newPatron.getPatronID(), newPatron);
	}

	public static void setCopyStore(Map<String, Copy> copyStore)
	{
		FakeDB.copyStore = copyStore;
	}

	public static Map<String, Copy> getCopyStore()
	{
		return copyStore;
	}

	protected static Map<Copy, ArrayList<Hold>> getHoldStore()
	{
		return holdStore;
	}

	protected static void setPatronHolds(Copy activeCopy, ArrayList<Hold> holdTobeAdded)
	{

		FakeDB.holdStore.put(activeCopy, holdTobeAdded);
	}

	public static ArrayList<Hold> getHoldRecord(Copy copyWithHold)
	{
		return FakeDB.holdStore.get(copyWithHold);

	}

}
