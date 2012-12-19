package optimus.model;

import java.awt.Point;

import javax.swing.ImageIcon;

public class Akkumulator extends Building {

	public Akkumulator(Point location, int level, Fraktionen Fraktion) {
		super(location, level, Fraktion);

		icon = new ImageIcon("images/Buildings/" + Fraktion
				+ "/Accumulator.PNG");
		baulichkeit = GebäudeArt.Akkumulator;
	}

	public void changeFraktion() {
		if (Fraktion == Fraktionen.NOD) {
			Fraktion = Fraktionen.GDI;
		} else {
			Fraktion = Fraktionen.NOD;
		}
		icon = new ImageIcon("images/Buildings/" + Fraktion
				+ "/Accumulator.PNG");
	}

}
