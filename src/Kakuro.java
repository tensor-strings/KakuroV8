import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.JOptionPane;


public class Kakuro extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Dimension GAME_DIM = new Dimension(10, 10); // puzzle dimensions
	private static Dimension WINDOW_DIM; // window size. adjusts to screen size
	
	private KakuroBoard game_board;
	private JPanel game_panel;
	private KakuroGenerator game_gen;
	
	private JMenuBar menu_bar;
	private JMenu file_menu;
	private JMenu play_menu;
	
	private EventHandler eh;
	
	private ArrayList<Tile> tiles = new ArrayList<>();
	
	private Mode draw_mode = Mode.START; // default to empty board
	private ArrayList<JTextField> fields = new ArrayList<JTextField>();
	private boolean menu_des = false;

	/**
	 * @param args Creates window
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				WINDOW_DIM = Toolkit.getDefaultToolkit().getScreenSize();
				
				Kakuro gb = new Kakuro();
				
				gb.setSize(WINDOW_DIM);
				gb.setDefaultCloseOperation(EXIT_ON_CLOSE);
				gb.setVisible(true);
				gb.setTitle("Puzzle Time");
				gb.setResizable(false);
			}
		});
	}

	public Kakuro() {

		eh = new EventHandler();
		
		addMouseMotionListener(eh);
		addMouseListener(eh);
		Font f = new Font("sans-serif", Font.PLAIN, 24);
		UIManager.put("Menu.font", f);
		menu_bar = new JMenuBar();
		file_menu = new JMenu("File");
		file_menu.addMenuListener(new MenuHandler());
		menu_bar.add(file_menu);
		this.add(menu_bar, BorderLayout.NORTH);

		play_menu = new JMenu("Game");
		menu_bar.add(play_menu);
		this.add(menu_bar, BorderLayout.NORTH);
		play_menu.addMenuListener(new MenuHandler());

		JMenuItem menuItem = new JMenuItem("New");
		menuItem.addActionListener(eh);
		file_menu.add(menuItem);
		menuItem.setFont(f);

		menuItem = new JMenuItem("Play kakuro");
		menuItem.addActionListener(eh);
		play_menu.add(menuItem);
		menuItem.setFont(f);

		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(eh);
		file_menu.add(menuItem);
		menuItem.setFont(f);

		menuItem = new JMenuItem("Open");
		menuItem.addActionListener(eh);
		file_menu.add(menuItem);
		menuItem.setFont(f);

		game_panel = new JPanel();
		game_panel.addMouseMotionListener(eh);
		game_panel.addMouseListener(eh);
		game_panel.setLayout(null);
		this.add(game_panel);

		game_board = new KakuroBoard(GAME_DIM, Mode.KAKURO);
		game_gen = new TestKakuro(game_board); // gameGenerator here
		game_gen.generateGame();
		tiles = game_gen.getCells();

	}

	/**
	 * @param game_panel - sets up textfields
	 */
	public void setLabels(JPanel game_panel) {
		for (int i = 0; i < tiles.size(); i++) {
			if (tiles.get(i).getValue()[0] == 0 && tiles.get(i).getValue()[1] == 0 && !tiles.get(i).isStartBlock()) {
				JTextField textValues = new JTextField();
				fields.add(textValues);
				textValues.setSize(new Dimension(game_board.getCellSize() - (game_board.getCellSize() / 3),
						game_board.getCellSize() - (game_board.getCellSize() / 3)));
				textValues.setLocation(
						(game_board.getX()) + (tiles.get(i).getPosition().x * game_board.getCellSize())
								+ game_board.getCellSize() / 12,
						game_board.getY() + (tiles.get(i).getPosition().y * game_board.getCellSize()
								+ (game_board.getCellSize() / 12)));
				textValues.addMouseMotionListener(eh);
				textValues.addMouseListener(eh);
				textValues.addFocusListener(eh);
				textValues.addActionListener(eh);
				Font f = new Font("sans-serif", Font.PLAIN, 24);
				textValues.setFont(f);

			} 
			else if (tiles.get(i).getValue()[0] != 0 && tiles.get(i).getValue()[1] == 0 && !tiles.get(i).isStartBlock()) {
				JTextField textValues = new JTextField();
				fields.add(textValues);
				textValues.setSize(new Dimension(game_board.getCellSize() - (game_board.getCellSize() / 3),
						game_board.getCellSize() - (game_board.getCellSize() / 3)));
				textValues.setLocation(
						(game_board.getX()) + (tiles.get(i).getPosition().x * game_board.getCellSize())
								+ game_board.getCellSize() / 12,
						game_board.getY() + (tiles.get(i).getPosition().y * game_board.getCellSize()
								+ (game_board.getCellSize() / 12)));
				textValues.addMouseMotionListener(eh);
				textValues.addMouseListener(eh);
				textValues.addActionListener(eh);
				textValues.addFocusListener(eh);
				Font f = new Font("sans-serif", Font.PLAIN, 24);
				textValues.setFont(f);
			} 
			else
				fields.add(null);
		}
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i) != null) {
				game_panel.add(fields.get(i));
				fields.get(i).setText(String.valueOf(tiles.get(i).getValue()[0]));
			}
		}
	}

	/**
	 * @param game_panel - removes textfields
	 */
	public void resetLabels(JPanel game_panel) {
		for (int i = 0; i < fields.size(); i++) {
			if (fields.get(i) != null) {
				this.game_panel.remove(fields.get(i));

			}
		}
		fields.clear();
	}

	/**
	 * game_board on the game panel
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g1 = (Graphics2D) game_panel.getGraphics();
		if (menu_des) { 
			game_board.draw(g1, game_panel.getSize());
			menu_des = false;
		}
		if (draw_mode == Mode.START) {
			game_board.draw(g1, game_panel.getSize());

		}
		if (draw_mode == Mode.KAKURO) {
			game_board.draw(g1, game_panel.getSize());
			resetLabels(game_panel);
			setLabels(game_panel);

		}

	}

	private class EventHandler implements ActionListener, MouseListener, MouseMotionListener, FocusListener {
		private int startX, startY; 

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.isControlDown()) {
				if (e.getSource() instanceof JTextField) {
					for (int i = 0; i < fields.size(); i++) {
						if (e.getComponent() == fields.get(i)) {
							String result = Solver.getHelp(game_board.getGameData(), tiles.get(i));
							if (result == "") {
								JOptionPane.showMessageDialog(fields.get(i),
										"Available Options: " + "Back space, exit the cell and try again!");
							} 
							else
								JOptionPane.showMessageDialog(fields.get(i), "Available Options: " + result);

						}
					}
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			startX = e.getX();
			startY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		@Override
		public void mouseEntered(MouseEvent e) {

		}

		
		@Override
		public void mouseExited(MouseEvent e) {
			
		}

		@Override
		public void mouseDragged(MouseEvent e) {

		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (arg0.getActionCommand().equals("File")) {
				repaint();

			}
			if (arg0.getActionCommand().equals("New")) {
				resetLabels(game_panel);
				game_board.clear();
				repaint();
			}
			if (arg0.getActionCommand().equals("Save")) {
				repaint();
				JFileChooser save = new JFileChooser();
				save.setDialogTitle("File Explorer");
				save.setCurrentDirectory(new java.io.File("."));
				save.setPreferredSize(new Dimension(1200, 600));
				save.showSaveDialog(null);

				System.out.println("Saving");

				File gameFile = save.getSelectedFile();
				if (gameFile == null) {
					return;
				}
				try {
					if (save.getSelectedFile().getCanonicalPath().contains("txt")) {

					} 
					else {
						gameFile = new File(gameFile + ".txt");
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				FileWriter textStream = null;

				try {
					textStream = new FileWriter(gameFile);
					System.out.println(game_board.getGameData().length);
					for (int i = 0; i < game_board.getGameData().length; i++) {
						String data = "";
						for (int j = 0; j < game_board.getGameData().length; j++) {
							data += game_board.getGameData()[i][j] + " ";
						}
						textStream.write(data + "\n");
					}
					textStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.printf("File is located at %s%n", gameFile.getAbsolutePath());
				repaint();

			}

			int numCells = 0;
			if (arg0.getActionCommand().equals("Open") || arg0.getActionCommand().equals("Play kakuro")) {

				JFileChooser open = new JFileChooser();

				open.setDialogTitle("File Explorer");
				open.setCurrentDirectory(new java.io.File("."));
				open.setPreferredSize(new Dimension(1200, 600));

				int status = open.showOpenDialog(null);
				File inFile = null;

				if (status == JFileChooser.APPROVE_OPTION) {
					inFile = open.getSelectedFile();

				}
				if (inFile == null) {
					System.out.println("No file selected!");
					return;
				} 
				else {
					Scanner s = null;

					try {
						FileInputStream fis = new FileInputStream(inFile);
						s = new Scanner(fis);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					int counter = 0;
					draw_mode = Mode.KAKURO;
					while (s.hasNext()) {

						String[] tokens = s.nextLine().trim().split(" ");
						numCells = tokens.length;
						if (counter == 0) {
							game_board.reset();
							game_board = new KakuroBoard(new Dimension(numCells, numCells), Mode.KAKURO);
							game_gen = new TestKakuro(game_board);
							game_gen.generateGame();
							tiles = game_gen.getCells();
						}
						for (int i = 0; i < numCells; i++) {
							String[] newValue = tokens[i].split(",");
							int[] newValue2 = new int[] { Integer.parseInt(newValue[0]),
									Integer.parseInt(newValue[1]) };
							boolean start = Boolean.parseBoolean(newValue[2]);
							tiles.get(counter).setValue(newValue2);
							tiles.get(counter).setStartBlock(start);

							counter += 1;
						}

					}

				}
				resetLabels(game_panel);
				repaint();

			}
		}

		@Override
		public void focusGained(FocusEvent arg0) {
			
		}

		/**
		 * when tile is unselected, this sets the text value to the tile or rejects the input and sets the text to 0
		 */
		@Override
		public void focusLost(FocusEvent arg0) {
			if (!fields.isEmpty()) {
				for (int i = 0; i < fields.size(); i++) {
					if (arg0.getComponent() == fields.get(i)) {
						if (isInteger(fields.get(i).getText())) {
							String result = Solver.getHelp(game_board.getGameData(), tiles.get(i));
							String[] tokens = result.trim().split(", ");
							ArrayList<String> results = new ArrayList<>();
							for (String s : tokens) {
								results.add(s);
							}
							if (tiles.get(i).getValue()[0] != 0) {
								results.add(fields.get(i).getText());
							}
							if (results.contains(fields.get(i).getText())) {

								tiles.get(i).setValue(new int[] { Integer.parseInt(fields.get(i).getText()), 0 });
							} else {
								fields.get(i).setText("0");
								tiles.get(i).setValue(new int[] { 0, 0 });
							}
						} else {
							fields.get(i).setText("0");
							tiles.get(i).setValue(new int[] { 0, 0 });
						}
					}
				}
			}
			
		}
	}

	/**
	 * repaints after menu item is selected
	 */
	public class MenuHandler implements MenuListener {

		public void menuSelected(MenuEvent e) {

		}

		public void menuDeselected(MenuEvent e) {
			menu_des = true;
			repaint();
		}

		public void menuCanceled(MenuEvent e) {

		}
	}

	/**
	 * @param input - string 
	 * @return true if input is parsible an integer
	 */
	public boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}