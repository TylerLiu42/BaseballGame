// Extra imports required for GUI code
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Creates the JFrame for Connect Four and plays a simple game of Connect 4
 * using the ConnectFourBoard class
 * @author Tyler Liu
 * @version June 2015
 */

public class BaseballMain extends JFrame implements ActionListener
{
	// Program variables for the Menu items and the baseball diamond
	private JMenuItem newOption, exitOption, rulesMenuItem, aboutMenuItem;
	private BaseballPanel gameBoard;

	/**
	 * Constructs a new Baseball frame (sets up the Game)
	 */
	public BaseballMain()
	{
		// Sets up the frame for the game
		super("Baseball");
		setResizable(false);

		// Load up the icon image (penguin image from www.iconshock.com)
		setIconImage(Toolkit.getDefaultToolkit().getImage("ball.png"));

		// Sets up the baseball diamond in the centre of this frame
		gameBoard = new BaseballPanel();
		add(gameBoard, BorderLayout.CENTER);

		// Centre the frame in the middle of the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - gameBoard.BOARD_SIZE.width) / 2,
				(screen.height - gameBoard.BOARD_SIZE.height) / 2 - 100);

		// Adds the menu and menu items to the frame (see below for code)
		// Set up the Game MenuItems
		newOption = new JMenuItem("New");
		newOption.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		newOption.addActionListener(this);

		exitOption = new JMenuItem("Exit");
		exitOption.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
		exitOption.addActionListener(this);

		// Set up the Help Menu
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('H');
		rulesMenuItem = new JMenuItem("Rules...", 'R');
		rulesMenuItem.addActionListener(this);
		helpMenu.add(rulesMenuItem);
		aboutMenuItem = new JMenuItem("About...", 'A');
		aboutMenuItem.addActionListener(this);
		helpMenu.add(aboutMenuItem);

		// Add each MenuItem to the Game Menu (with a separator)
		JMenu gameMenu = new JMenu("Game");
		gameMenu.add(newOption);
		gameMenu.addSeparator();
		gameMenu.add(exitOption);
		JMenuBar mainMenu = new JMenuBar();
		mainMenu.add(gameMenu);
		mainMenu.add(helpMenu);
		// Set the menu bar for this frame to mainMenu
		setJMenuBar(mainMenu);
	} // Constructor

	/**
	 * Responds to a Menu Event.
	 * @param event the event that triggered this method
	 */
	public void actionPerformed(ActionEvent event)
	{
		if (event.getSource() == newOption) // Selected "New"
		{
			gameBoard.newGame();
		}
		else if (event.getSource() == exitOption) // Selected "Exit"
		{
			this.setVisible(false);
			System.exit(0);
		}
		else if (event.getSource() == rulesMenuItem) // Selected "Rules"
		{
			JOptionPane
					.showMessageDialog(
							this,
							"Press ENTER when you are ready to hit"
									+ "\n\nThe pitch will come shortly afterwards"
									+ "\nPress ENTER to swing"
									+ "\nPress ENTER again to bat once more"
									+ "\n\nPlay Ball!",
							"Rules",
							JOptionPane.INFORMATION_MESSAGE);
		}
		else if (event.getSource() == aboutMenuItem) // Selected "About"
		{
			JOptionPane.showMessageDialog(this,
					"by Tyler Liu and Quinton Short" +
							"\n\u00a9 2015", "About Baseball Game",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Starts up the Baseball Game frame
	 * @param args An array of Strings (ignored)
	 */
	public static void main(String[] args)
	{
		// Starts up the ConnectFourMain frame
		BaseballMain frame = new BaseballMain();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	} // main method
}
