package dk.hitman.hitman2012.client;

class Reg {
	public final int id;
	public final String navn;
	public final String gruppe;
	public final String kvarter;
	public final String bydel;
	
	public Reg(int i, String n, String g, String k, String b) {
		id = i;
		navn = n;
		gruppe = g;
		kvarter = k;
		bydel = b;
	}
	
	public int compareTo(Reg other, int col) {
		if (this == other) return 0;
		if (col == 0) return id - other.id;
		if (col == 1) return navn.compareTo(other.navn);
		if (col == 2) return gruppe.compareTo(other.gruppe);
		if (col == 3) return kvarter.compareTo(other.kvarter);
		return bydel.compareTo(other.bydel);
	}
}