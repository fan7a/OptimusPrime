package optimus.model;

import java.awt.Point;

import javax.swing.ImageIcon;

public abstract class Feld {

	protected ImageIcon icon;
	protected Point location;
	protected Geb�udeArt baulichkeit;
	
	
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

	public Geb�udeArt getBaulichkeit() {
		return baulichkeit;
	}

	public void setBaulichkeit(Geb�udeArt baulichkeit) {
		this.baulichkeit = baulichkeit;
	}

	public String toString(){
		String s;
		s = this.getBaulichkeit()+"";
		return s; 
	}
}
