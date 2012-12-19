package optimus.model;

import java.awt.Point;

import javax.swing.ImageIcon;

public class Kraftwerk extends Building{
		
	public Kraftwerk(Point location, int level, Fraktionen Fraktion) {
		super(location, level, Fraktion);

		icon = new ImageIcon("images/Buildings/" + Fraktion
				+ "/PowerPlant.PNG");
		baulichkeit = GebäudeArt.Kraftwerk;
	}

	public void changeFraktion() {
		if (Fraktion == Fraktionen.NOD) {
			Fraktion = Fraktionen.GDI;
		} else {
			Fraktion = Fraktionen.NOD;
		}
		icon = new ImageIcon("images/Buildings/" + Fraktion
				+ "/PowerPlant.PNG");
	}
}
