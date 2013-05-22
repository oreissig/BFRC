package bfrc.ook;

public class OokTuple {
	public final Ook ook1;
	public final Ook ook2;

	public OokTuple(Ook ook1, Ook ook2) {
		if (ook1 == null || ook2 == null)
			throw new IllegalArgumentException();
		this.ook1 = ook1;
		this.ook2 = ook2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OokTuple) {
			OokTuple t = (OokTuple) obj;
			return ook1.equals(t.ook1) && ook2.equals(t.ook2);
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return (ook1.ordinal() >> 16) + ook2.ordinal();
	}

	@Override
	public String toString() {
		return ook1.toString() + " " + ook2.toString();
	}
}