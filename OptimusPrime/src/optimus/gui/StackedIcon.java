package optimus.gui;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class StackedIcon implements Icon, java.io.Serializable {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Icon[] icons;
	  private final int width;
	  private final int height;

	  /** Creates a new <code>StackedIcon</code>. */
	  public StackedIcon(Icon... icons) {
	    this.icons = icons;
	    int width = 0;
	    int height = 0;
	    for (Icon icon : icons) {
	      width = Math.max(width, icon.getIconWidth());
	      height = Math.max(height, icon.getIconHeight());
	    }
	    this.width = width;
	    this.height = height;
	  }

	  @Override
	  public void paintIcon(Component c, Graphics g, int x, int y) {
	    for (Icon icon : icons) {
	      icon.paintIcon(c, g, x, y);
	     
	    }
	    
	  }

	  @Override
	  public int getIconWidth() {
	    return width;
	  }

	  @Override
	  public int getIconHeight() {
	    return height;
	  }


	}