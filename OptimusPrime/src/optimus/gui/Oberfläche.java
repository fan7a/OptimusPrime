package optimus.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
//import java.awt.datatransfer.DataFlavor;
//import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
//import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import optimus.model.Akkumulator;
import optimus.model.Building;
import optimus.model.Feld;
import optimus.model.Fraktionen;
import optimus.model.GebäudeArt;
import optimus.model.Kraftwerk;
import optimus.model.Kristall;
import optimus.model.Tiberium;

public class Oberfläche extends JFrame implements MouseListener,
		DropTargetListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPopupMenu menu;
	private JPanel spielBrett;
	private JLabel[][] spielFeld;
	private Feld[][] gebaeudeFeld;
	private GebäudeArt activeBuilding = GebäudeArt.Akkumulator;
	private ImageIcon icon;
	private ImageIcon building;
	private JPanel auswahlPanel;
	public Fraktionen Fraktion;
	private JLabel startPanel;
	private JLabel dragLabel;
	private int activeLevel = -1;

	private JButton auswahlNOD;
	private JButton auswahlGDI;

	public Oberfläche() {
		initialize();
	}

	public void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		Fraktion = Fraktionen.NOD;

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));

		this.setContentPane(contentPane);

		icon = new ImageIcon("images/Base/Felder.PNG");
		building = new ImageIcon("images/Buildings/" + Fraktion
				+ "/Accumulator.PNG");

		this.setResizable(false);
		this.setTitle("OptimusPrime");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, icon.getIconWidth() + 14,
				icon.getIconHeight() + 50);

		erstelleMenue();
		auswahlFraktion();
	}

	public void auswahlFraktion() {
		startPanel = new JLabel();
		startPanel.setLayout(new BorderLayout());
		contentPane.add(startPanel, BorderLayout.CENTER);

		startPanel.setIcon(new ImageIcon("images/backgrounds/CCTAEndgame.jpg"));

		auswahlPanel = new JPanel();
		auswahlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 30));

		startPanel.add(auswahlPanel, BorderLayout.SOUTH);

		ImageIcon fraktionIcon = new ImageIcon();

		JLabel banner = new JLabel();
		startPanel.add(banner, BorderLayout.NORTH);
		fraktionIcon = new ImageIcon("images/logo3.PNG");
		banner.setIcon(fraktionIcon);
		banner.setHorizontalAlignment(JLabel.CENTER);

		auswahlPanel.setOpaque(false);

		auswahlNOD = new JButton();
		auswahlPanel.add(auswahlNOD);
		fraktionIcon = new ImageIcon("images/NOD.PNG");
		auswahlNOD.setIcon(fraktionIcon);
		auswahlNOD.addMouseListener(this);

		auswahlGDI = new JButton();
		auswahlPanel.add(auswahlGDI);
		fraktionIcon = new ImageIcon("images/GDI.PNG");
		auswahlGDI.setIcon(fraktionIcon);
		auswahlGDI.setSize(fraktionIcon.getIconWidth(),
				fraktionIcon.getIconHeight());
		auswahlGDI.addMouseListener(this);
		auswahlGDI.setOpaque(true);
		System.out.println("Wähle Fraktion..");

	}

	public void erstelleMenue() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnDatei = new JMenu("Datei");
		menuBar.add(mnDatei);

		JMenuItem mntmNeu = new JMenuItem("Neu");
		mntmNeu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (spielFeld != null) {
					for (int i = 0; i < spielFeld.length; i++) {
						for (int j = 0; j < spielFeld[0].length; j++) {
							gebaeudeFeld[i][j] = null;

						}
					}
				}
				repaint();
			}
		});
		mnDatei.add(mntmNeu);

		mnDatei.addSeparator();

		JMenuItem mntmImportieren = new JMenuItem("Importieren");
		mntmImportieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnDatei.add(mntmImportieren);

		JMenuItem mntmExportieren = new JMenuItem("Exportieren");
		mntmExportieren.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnDatei.add(mntmExportieren);

		mnDatei.addSeparator();

		JMenuItem mntmBeenden = new JMenuItem("Schließen");
		mntmBeenden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnDatei.add(mntmBeenden);

		JMenu mnBearbeiten = new JMenu("Bearbeiten");
		menuBar.add(mnBearbeiten);

		final JMenuItem mntmFraktion = new JMenuItem("Fraktion");
		mntmFraktion.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (spielFeld != null) {
					for (int i = 0; i < spielFeld.length; i++) {
						for (int j = 0; j < spielFeld[0].length; j++) {
							if (gebaeudeFeld[i][j] != null) {
								if (gebaeudeFeld[i][j] instanceof Akkumulator) {
									((Akkumulator) gebaeudeFeld[i][j])
											.changeFraktion();
								}
								if (gebaeudeFeld[i][j] instanceof Kraftwerk) {
									((Kraftwerk) gebaeudeFeld[i][j])
											.changeFraktion();
								}
							}
						}
					}
				}

				if (Fraktion == Fraktionen.NOD) {
					Fraktion = Fraktionen.GDI;

				} else {
					Fraktion = Fraktionen.NOD;
				}
				mntmFraktion.setIcon(new ImageIcon("images/" + Fraktion
						+ "_kl.PNG"));
				repaint();

			}
		});
		mntmFraktion.setIcon(new ImageIcon("images/" + Fraktion + "_kl.PNG"));
		mnBearbeiten.add(mntmFraktion);

		mnBearbeiten.addSeparator();

		JMenuItem mntmOptimize = new JMenuItem("optimize");
		mntmOptimize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// new AuftragAnlegenForm(legoDB).setVisible(true);
				// refreshTable();
			}
		});
		mnBearbeiten.add(mntmOptimize);

		JMenu mnHilfe = new JMenu("Hilfe");
		menuBar.add(mnHilfe);

		JMenuItem mntmInfo = new JMenuItem("Info");
		mntmInfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane
						.showMessageDialog(
								contentPane,
								"Optimus Prime was created to optimize your Base to its FINEST - fan7astic.",
								"Über OptimusPrime..",
								JOptionPane.INFORMATION_MESSAGE);
				// new Info();
			}
		});
		mnHilfe.add(mntmInfo);

		mnHilfe.addSeparator();

		JMenuItem mntmDonate = new JMenuItem("Donate");
		mntmDonate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// new AuftragAnlegenForm(legoDB).setVisible(true);
				// refreshTable();
			}
		});

		mnHilfe.add(mntmDonate);

	}

	public void erstelleSpielbrett() {
		icon = new ImageIcon("images/Base/Felder.PNG");
		System.out.println("Initialisiere Spielfeld..");

		gebaeudeFeld = new Feld[8][9];

		spielBrett = new JPanel();
		spielBrett.setLayout(new GridLayout(8, 9, 0, 0));
		contentPane.add(spielBrett, BorderLayout.CENTER);
		spielFeld = new JLabel[8][9];

		int feldNummer = 0;

		for (int i = 0; i < spielFeld.length; i++) {
			for (int j = 0; j < spielFeld[0].length; j++) {
				feldNummer++;
				icon = new ImageIcon("images/Base/feld_" + feldNummer + ".PNG");
				spielFeld[i][j] = new JLabel();

				spielFeld[i][j].setLocation(j * icon.getIconWidth(),
						i * icon.getIconHeight());
				spielFeld[i][j].setVisible(true);
				spielFeld[i][j].setSize(icon.getIconWidth(),
						icon.getIconHeight());
				spielFeld[i][j].setOpaque(true);
				spielFeld[i][j].setBorder(null);
				spielFeld[i][j].addMouseListener(this);
				spielFeld[i][j].setIcon(icon);
				spielBrett.add(spielFeld[i][j]);
				// System.out.println(i +"   "+j+"         "+icon);
				// System.out.println(icon.getIconWidth()+"  "+icon.getIconHeight());

				// spielFeld[i][j].setVerticalTextPosition(JLabel.BOTTOM);
				// spielFeld[i][j].setHorizontalTextPosition(JLabel.CENTER);

				new DropTarget(spielFeld[i][j], this);
				spielFeld[i][j].setTransferHandler(new TransferHandler("text"));
			}
		}

		icon = new ImageIcon("images/Base/Felder.PNG");
		spielBrett.setSize(icon.getIconWidth(), icon.getIconHeight());

		repaint();
	}

	public void showMenu(MouseEvent e) {
		menu = new JPopupMenu();
		JMenuItem itemAkk = new JMenuItem("Akkumulator");
		JMenuItem itemKrftwrk = new JMenuItem("Kraftwerk");
		JMenuItem itemTiberium = new JMenuItem("Tiberium");
		JMenuItem itemKristalle = new JMenuItem("Kristalle");

		itemAkk.setIcon(new ImageIcon("images/Buildings/" + Fraktion
				+ "/Accumulator_kl.PNG"));
		itemKrftwrk.setIcon(new ImageIcon("images/Buildings/" + Fraktion
				+ "/PowerPlant_kl.PNG"));
		itemTiberium.setIcon(new ImageIcon("images/Buildings/" + Fraktion
				+ "/Tiberium_kl.PNG"));
		itemKristalle.setIcon(new ImageIcon("images/Buildings/" + Fraktion
				+ "/Crystals_kl.PNG"));

		itemAkk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				building = new ImageIcon("images/Buildings/" + Fraktion
						+ "/Accumulator.PNG");
				activeBuilding = GebäudeArt.Akkumulator;
				// if (gebaeudeFeld[clickedY][clickedX] == activeBuilding) {
				// gebaeudeFeld[clickedY][clickedX] = null;
				// } else {
				// gebaeudeFeld[clickedY][clickedX] = activeBuilding;
				// }

				repaint();
			}
		});
		itemKrftwrk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				building = new ImageIcon("images/Buildings/" + Fraktion
						+ "/PowerPlant.PNG");
				activeBuilding = GebäudeArt.Kraftwerk;

				// if (gebaeudeFeld[clickedY][clickedX] == activeBuilding) {
				// gebaeudeFeld[clickedY][clickedX] = null;
				// } else {
				// gebaeudeFeld[clickedY][clickedX] = activeBuilding;
				// }

				repaint();

			}
		});
		itemTiberium.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				building = new ImageIcon("images/Buildings/" + Fraktion
						+ "/Tiberium.PNG");
				activeBuilding = GebäudeArt.Tiberium;

				// if (gebaeudeFeld[clickedY][clickedX] == activeBuilding) {
				// gebaeudeFeld[clickedY][clickedX] = null;
				// } else {
				// gebaeudeFeld[clickedY][clickedX] = activeBuilding;
				// }

				repaint();
			}
		});
		itemKristalle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				building = new ImageIcon("images/Buildings/" + Fraktion
						+ "/Crystals.PNG");
				activeBuilding = GebäudeArt.Kristalle;

				// if (gebaeudeFeld[clickedY][clickedX] == activeBuilding) {
				// gebaeudeFeld[clickedY][clickedX] = null;
				// } else {
				// gebaeudeFeld[clickedY][clickedX] = activeBuilding;
				// }

				repaint();
			}
		});

		menu.add(itemAkk);
		menu.add(itemKrftwrk);
		menu.add(itemTiberium);
		menu.add(itemKristalle);

		menu.show((Component) e.getSource(), e.getX(), e.getY());
	}

	public void paint(Graphics g) {
		super.paint(g);

		// building = new ImageIcon("images/Buildings/NOD/Accumulator.PNG");
		icon = new ImageIcon("images/Base/Felder.PNG");
		StackedIcon stackedicon = new StackedIcon(building);

		if (spielFeld != null) {

			for (int i = 0; i < spielFeld.length; i++) {
				for (int j = 0; j < spielFeld[0].length; j++) {

					if (gebaeudeFeld[i][j] != null) {
						if (gebaeudeFeld[i][j].getBaulichkeit() == GebäudeArt.Akkumulator) {
							stackedicon = new StackedIcon(
									gebaeudeFeld[i][j].getIcon());
							stackedicon.paintIcon(spielFeld[i][j], g,
									spielFeld[i][j].getX(),
									spielFeld[i][j].getY() + 40);
						}
						if (gebaeudeFeld[i][j].getBaulichkeit() == GebäudeArt.Kraftwerk) {
							stackedicon = new StackedIcon(
									gebaeudeFeld[i][j].getIcon());
							stackedicon.paintIcon(spielFeld[i][j], g,
									spielFeld[i][j].getX(),
									spielFeld[i][j].getY() + 40);
						}
						if (gebaeudeFeld[i][j].getBaulichkeit() == GebäudeArt.Tiberium) {
							stackedicon = new StackedIcon(
									gebaeudeFeld[i][j].getIcon());
							stackedicon.paintIcon(spielFeld[i][j], g,
									spielFeld[i][j].getX(),
									spielFeld[i][j].getY() + 40);
						}
						if (gebaeudeFeld[i][j].getBaulichkeit() == GebäudeArt.Kristalle) {
							stackedicon = new StackedIcon(
									gebaeudeFeld[i][j].getIcon());
							stackedicon.paintIcon(spielFeld[i][j], g,
									spielFeld[i][j].getX(),
									spielFeld[i][j].getY() + 40);
						}

						// Draw Stufen

						String family = "Lucida Sans Typewriter";
						int style = Font.LAYOUT_RIGHT_TO_LEFT;
						int size = 24;
						Font font = new Font(family, style, size);
						
						g.setFont(font);
						
						
						
						int x = spielFeld[i][j].getX() + spielFeld[i][j].getWidth() * 3 / 4;
						int y = spielFeld[i][j].getY() + 45
								+ g.getFontMetrics().getAscent() + spielFeld[i][j].getHeight() * 3 / 4;

						if (gebaeudeFeld[i][j] instanceof Building) {
							g.drawString(
									((Building) gebaeudeFeld[i][j]).getLevel()
											+ "", x, y);
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if (e.getSource() == auswahlNOD || e.getSource() == auswahlGDI) {
			if (e.getSource() == auswahlGDI) {
				Fraktion = Fraktionen.GDI;
			} else {
				Fraktion = Fraktionen.NOD;
			}

			startPanel.setVisible(false);

			erstelleSpielbrett();
			System.out.println(Fraktion + " wird geladen..");

		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent me) {
		if (me.getButton() == 1) {
			dragLabel = new JLabel();
			dragLabel = (JLabel) me.getSource();
			// System.out.println(dragLabel.getIcon());

			final TransferHandler handler = dragLabel.getTransferHandler();
			handler.exportAsDrag(dragLabel, me, TransferHandler.COPY);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (spielFeld != null && gebaeudeFeld != null) {

			// Linksklick:
			if (e.getButton() == 1) {

				// // Stufenauswahl
				// if(activeBuilding == GebäudeArt.Akkumulator || activeBuilding
				// == GebäudeArt.Kraftwerk){
				// while (activeLevel < 0 || activeLevel > 50) {
				// String Level = "";
				// try {
				// Level = (String) JOptionPane
				// .showInputDialog(contentPane, activeBuilding+" Stufe:",
				// "Eingabe:", JOptionPane.PLAIN_MESSAGE);
				// if(Level==null){
				// return;
				// }
				// activeLevel = Integer.parseInt(Level);
				// } catch (Exception exception) {
				// JOptionPane.showMessageDialog(contentPane,
				// "Ihre Base ist explodiert..",
				// "Fehlerhafte Eingabe",
				// JOptionPane.WARNING_MESSAGE);
				// }
				//
				// }
				// }
				//
				// // Füge Gebäude ein @gebaeudeFeld
				// for (int i = 0; i < spielFeld.length; i++) {
				// for (int j = 0; j < spielFeld[0].length; j++) {
				// if (e.getSource().equals(spielFeld[i][j])) {
				//
				//
				// if(gebaeudeFeld[i][j] != null && activeBuilding ==
				// gebaeudeFeld[i][j].getBaulichkeit() ){
				// if(gebaeudeFeld[i][j] != null && gebaeudeFeld[i][j]
				// instanceof Building && activeLevel > 0){
				// System.out.print(gebaeudeFeld[i][j].getBaulichkeit()+" von ("+((Building)
				// gebaeudeFeld[i][j]).getLevel()+") zu (");
				// ((Building) gebaeudeFeld[i][j]).setLevel(activeLevel);
				// System.out.println(((Building)
				// gebaeudeFeld[i][j]).getLevel()+") geändert.");
				// }
				// else{
				// System.out.print("Tschüss: "+
				// gebaeudeFeld[i][j].getBaulichkeit()+" (");
				// if(gebaeudeFeld[i][j] != null && gebaeudeFeld[i][j]
				// instanceof Building){
				// System.out.print(((Building) gebaeudeFeld[i][j]).getLevel());
				// }
				// System.out.println(")");
				// gebaeudeFeld[i][j] = null;
				// }
				// // nodraw = true;
				// repaint();
				// } else {
				// System.out.print("Erstelle: ");
				// if(activeBuilding == GebäudeArt.Akkumulator) {
				// gebaeudeFeld[i][j] = new Akkumulator(new Point(j,i),
				// activeLevel, Fraktion);
				// } else if(activeBuilding == GebäudeArt.Kraftwerk){
				// gebaeudeFeld[i][j] = new Kraftwerk(new Point(j,i),
				// activeLevel, Fraktion);
				// } else if(activeBuilding == GebäudeArt.Tiberium){
				// gebaeudeFeld[i][j] = new Tiberium(new Point(j,i));
				// } else if(activeBuilding == GebäudeArt.Kristalle){
				// gebaeudeFeld[i][j] = new Kristall(new Point(j,i));
				// }
				// System.out.println(gebaeudeFeld[i][j].getBaulichkeit() +" ("+
				// (activeLevel>0 ? activeLevel : "") +")");
				// nodraw = false;
				// // clickedX = j;
				// // clickedY = i;
				// }
				//
				// break;
				// }
				// }
				//
				// }

				//
				// activeLevel = -1;
				// repaint();
			}
		}

		if (e.getSource() == auswahlNOD || e.getSource() == auswahlGDI) {
			if (e.getSource() == auswahlGDI) {
				Fraktion = Fraktionen.GDI;
			} else {
				Fraktion = Fraktionen.NOD;
			}

			startPanel.setVisible(false);

			erstelleSpielbrett();
			System.out.println(Fraktion + " wird geladen..");

		}
		if (e.getButton() == 3) {
			if (e.isPopupTrigger()) {

				showMenu(e);

				// Setze Gebäude direkt bei Rechtsklick
				// for (int i = 0; i < spielFeld.length; i++) {
				// for (int j = 0; j < spielFeld[0].length; j++) {
				// if (e.getSource().equals(spielFeld[i][j])) {
				// clickedX = j;
				// clickedY = i;
				// break;
				// }
				// }
				//
				// }
			}
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public void drop(final DropTargetDropEvent dtde) {

		// DropTarget dt = (DropTarget) dtde.getSource();
		// NameSlot ns = (NameSlot) dt.getComponent();

//		DropTarget dt = (DropTarget) dtde.getSource();
//		String s = ((JLabel) dt.getComponent()).getIcon() + "";

		int m = 0;
		int n = 0;

		// Sucht raus welches Feld Objekt auf dem Drag JLabel liegt
		loop1: for (int k = 0; k < spielFeld.length; k++) {
			for (int l = 0; l < spielFeld[0].length; l++) {
				if (vergleicheJLabels(spielFeld[k][l], dragLabel)) {
					// System.out.println("DRAG:" + k +" , "+ l);
					m = k;
					n = l;
					break loop1;

				}
			}
		}

		for (int i = 0; i < spielFeld.length; i++) {
			for (int j = 0; j < spielFeld[0].length; j++) {
				if (((DropTarget) dtde.getSource()).getComponent().equals(
						spielFeld[i][j])) {
//					s = spielFeld[i][j].getIcon() + "";
					Feld feld1;
					Feld feld2;

					// DRAG AND DROP IM SELBEN FELD -> GEBÄUDE BAUEN
					if (i == m && j == n) {
						erstelleFeld(i, j);
					} else {

						if (gebaeudeFeld[i][j] != null
								&& gebaeudeFeld[m][n] != null) {
							System.out.println(gebaeudeFeld[m][n]
									.toString()
									+ " -> "
									+ gebaeudeFeld[i][j].toString());

							// VON
							if (gebaeudeFeld[m][n] instanceof Akkumulator) {
								feld1 = new Akkumulator(new Point(n, m),
										((Building) gebaeudeFeld[m][n])
												.getLevel(),
										((Building) gebaeudeFeld[m][n])
												.getFraktion());
							} else if (gebaeudeFeld[m][n] instanceof Kraftwerk) {
								feld1 = new Kraftwerk(new Point(n, m),
										((Building) gebaeudeFeld[m][n])
												.getLevel(),
										((Building) gebaeudeFeld[m][n])
												.getFraktion());
							} else if (gebaeudeFeld[m][n] instanceof Tiberium) {
								feld1 = new Tiberium(new Point(n, m));
							} else if (gebaeudeFeld[m][n] instanceof Kristall) {
								feld1 = new Kristall(new Point(n, m));
							} else {
								// PLATZHALTER
								feld1 = new Feld(null) {
								};
							}

							// NACH
							if (gebaeudeFeld[i][j] instanceof Akkumulator) {
								feld2 = new Akkumulator(new Point(j, i),
										((Building) gebaeudeFeld[i][j])
												.getLevel(),
										((Building) gebaeudeFeld[i][j])
												.getFraktion());
							} else if (gebaeudeFeld[i][j] instanceof Kraftwerk) {
								feld2 = new Kraftwerk(new Point(j, i),
										((Building) gebaeudeFeld[i][j])
												.getLevel(),
										((Building) gebaeudeFeld[i][j])
												.getFraktion());
							} else if (gebaeudeFeld[i][j] instanceof Tiberium) {
								feld2 = new Tiberium(new Point(j, i));
							} else if (gebaeudeFeld[i][j] instanceof Kristall) {
								feld2 = new Kristall(new Point(j, i));
							} else {
								// PLATZHALTER
								feld2 = new Feld(null) {
								};
							}

							gebaeudeFeld[i][j] = feld1;
							gebaeudeFeld[m][n] = feld2;

						} else if (gebaeudeFeld[i][j] != null) {

							System.out.println("Leer -> "
									+ gebaeudeFeld[i][j].toString());
							// NACH
							if (gebaeudeFeld[i][j] instanceof Akkumulator) {
								feld2 = new Akkumulator(new Point(j, i),
										((Building) gebaeudeFeld[i][j])
												.getLevel(),
										((Building) gebaeudeFeld[i][j])
												.getFraktion());
							} else if (gebaeudeFeld[i][j] instanceof Kraftwerk) {
								feld2 = new Kraftwerk(new Point(j, i),
										((Building) gebaeudeFeld[i][j])
												.getLevel(),
										((Building) gebaeudeFeld[i][j])
												.getFraktion());
							} else if (gebaeudeFeld[i][j] instanceof Tiberium) {
								feld2 = new Tiberium(new Point(j, i));
							} else if (gebaeudeFeld[i][j] instanceof Kristall) {
								feld2 = new Kristall(new Point(j, i));
							} else {
								// PLATZHALTER
								feld2 = new Feld(null) {
								};
							}

							gebaeudeFeld[i][j] = null;
							gebaeudeFeld[m][n] = feld2;

						} else if (gebaeudeFeld[m][n] != null) {
							System.out.println(gebaeudeFeld[m][n]
									.toString() + " -> Leer");

							// VON
							if (gebaeudeFeld[m][n] instanceof Akkumulator) {
								feld1 = new Akkumulator(new Point(n, m),
										((Building) gebaeudeFeld[m][n])
												.getLevel(),
										((Building) gebaeudeFeld[m][n])
												.getFraktion());
							} else if (gebaeudeFeld[m][n] instanceof Kraftwerk) {
								feld1 = new Kraftwerk(new Point(n, m),
										((Building) gebaeudeFeld[m][n])
												.getLevel(),
										((Building) gebaeudeFeld[m][n])
												.getFraktion());
							} else if (gebaeudeFeld[m][n] instanceof Tiberium) {
								feld1 = new Tiberium(new Point(n, m));
							} else if (gebaeudeFeld[m][n] instanceof Kristall) {
								feld1 = new Kristall(new Point(n, m));
							} else {
								// PLATZHALTER
								feld1 = new Feld(null) {
								};
							}

							gebaeudeFeld[i][j] = feld1;
							gebaeudeFeld[m][n] = null;

						} else {
							System.out.println("Leer -> Leer");
						}
					}
					repaint();
					break;
				}
			}
		}
		// System.out.println("drop detected from " + dragLabel.getIcon() +
		// " to "+ ((JLabel)
		// dtde.getDropTargetContext().getDropTarget().getComponent()).getIcon());
		// //+this.getText());

	}

	private void erstelleFeld(int i, int j) {

		// Stufenauswahl
		if (activeBuilding == GebäudeArt.Akkumulator
				|| activeBuilding == GebäudeArt.Kraftwerk) {
			while (activeLevel < 0 || activeLevel > 50) {
				String Level = "";
				try {
					Level = (String) JOptionPane.showInputDialog(contentPane,
							activeBuilding + " Stufe:", "Eingabe:",
							JOptionPane.PLAIN_MESSAGE);
					if (Level == null) {
						return;
					}
					activeLevel = Integer.parseInt(Level);
				} catch (Exception exception) {
					JOptionPane.showMessageDialog(contentPane,
							"Ihre Base ist explodiert..",
							"Fehlerhafte Eingabe", JOptionPane.WARNING_MESSAGE);
				}

			}
		}

		// Füge Gebäude ein @gebaeudeFeld

		if (gebaeudeFeld[i][j] != null
				&& activeBuilding == gebaeudeFeld[i][j].getBaulichkeit()) {
			if (gebaeudeFeld[i][j] != null
					&& gebaeudeFeld[i][j] instanceof Building
					&& activeLevel > 0) {
				System.out
						.print(gebaeudeFeld[i][j].getBaulichkeit() + " von ("
								+ ((Building) gebaeudeFeld[i][j]).getLevel()
								+ ") zu (");
				((Building) gebaeudeFeld[i][j]).setLevel(activeLevel);
				System.out.println(((Building) gebaeudeFeld[i][j]).getLevel()
						+ ") geändert.");
			} else {
				System.out.print("Tschüss: "
						+ gebaeudeFeld[i][j].getBaulichkeit() + " (");
				if (gebaeudeFeld[i][j] != null
						&& gebaeudeFeld[i][j] instanceof Building) {
					System.out
							.print(((Building) gebaeudeFeld[i][j]).getLevel());
				}
				System.out.println(")");
				gebaeudeFeld[i][j] = null;
			}
			// nodraw = true;
			repaint();
		} else {
			System.out.print("Erstelle: ");
			if (activeBuilding == GebäudeArt.Akkumulator) {
				gebaeudeFeld[i][j] = new Akkumulator(new Point(j, i),
						activeLevel, Fraktion);
			} else if (activeBuilding == GebäudeArt.Kraftwerk) {
				gebaeudeFeld[i][j] = new Kraftwerk(new Point(j, i),
						activeLevel, Fraktion);
			} else if (activeBuilding == GebäudeArt.Tiberium) {
				gebaeudeFeld[i][j] = new Tiberium(new Point(j, i));
			} else if (activeBuilding == GebäudeArt.Kristalle) {
				gebaeudeFeld[i][j] = new Kristall(new Point(j, i));
			}
			System.out.println(gebaeudeFeld[i][j].getBaulichkeit() + " ("
					+ (activeLevel > 0 ? activeLevel : "") + ")");
		}

		activeLevel = -1;
		repaint();

	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub

	}

	public Boolean vergleicheJLabels(JLabel l1, JLabel l2) {
		if ((l1.getIcon().equals(l2.getIcon())) && (l1.getX() == l2.getX())
				&& (l1.getY() == l2.getY())) {
			return true;
		}
		return false;
	}

}
