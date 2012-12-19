package optimus.model;

import java.awt.Point;

public class Building extends Feld{

	protected int level;
	protected Fraktionen Fraktion; 
	
	public Building(Point location, int level, Fraktionen Fraktion) {
		super(location);
		this.level = level;
		this.Fraktion = Fraktion;
		
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Fraktionen getFraktion() {
		return Fraktion;
	}

	public String toString(){
		String s;
		s = this.getBaulichkeit() +" ("+this.getLevel()+")";
		return s; 
	}

	
	
	
	
}
