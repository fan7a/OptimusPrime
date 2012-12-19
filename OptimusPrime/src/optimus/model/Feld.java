package optimus.model;

import java.awt.Point;

import javax.swing.ImageIcon;

public abstract class Feld {

	protected ImageIcon icon;
	protected Point location;
	protected GebäudeArt baulichkeit;
	
	
	public Feld(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	public GebäudeArt getBaulichkeit() {
		return baulichkeit;
	}

	public void setBaulichkeit(GebäudeArt baulichkeit) {
		this.baulichkeit = baulichkeit;
	}

	public String toString(){
		String s;
		s = this.getBaulichkeit()+"";
		return s; 
	}
}
