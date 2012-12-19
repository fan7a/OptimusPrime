package optimus.model;

import java.awt.Point;

import javax.swing.ImageIcon;

public class Tiberium extends Feld{

	public Tiberium(Point location) {
		super(location);
		icon = new ImageIcon("images/Buildings/NOD/Tiberium.PNG");
		baulichkeit = GebäudeArt.Tiberium;
	}

}
