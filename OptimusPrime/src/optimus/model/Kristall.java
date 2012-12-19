package optimus.model;

import java.awt.Point;

import javax.swing.ImageIcon;

public class Kristall extends Feld{

	public Kristall(Point location) {
		super(location);
		icon = new ImageIcon("images/Buildings/NOD/Crystals.PNG");
		baulichkeit = GebäudeArt.Kristalle;
	}

}
