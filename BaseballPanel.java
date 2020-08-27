import java.awt.*;

import javax.swing.*;

import java.awt.event.*;

/**
 * Handles the gameplay
 * @author Tyler Liu 
 * @version June 2015
 */
public class BaseballPanel extends JPanel implements KeyListener
{
	private Image baseball;
	private Image baseballField;
	private Image playerImage;
	private Image catcher;
	private Image runner;
	public final Dimension BOARD_SIZE =
			new Dimension(862, 768);

	private Point[] playerPos;
	private Point[] playerVelocity;
	private Point ballPos;
	private Point finalBallPos;
	private Point ballVelocity;
	private boolean gameOver;
	private final int TIME_INTERVAL = 20;
	private Timer timer;
	private int status;
	private int ballStop;
	private int noOfStrikes = 0;
	private int noOfOuts;
	private int jaysRuns;
	private int playerToMove;
	private boolean [] onBase;
	private String message;
	private String pitchMessage = "";

	/**
	 * Constructs a new Baseball Panel object
	 */
	public BaseballPanel()
	{
		// Sets up the board area, loads in piece images and starts a new game
		setPreferredSize(BOARD_SIZE);
		setBackground(new Color(200, 200, 200));
		baseballField = new ImageIcon("BaseballField.png").getImage();
		baseball = new ImageIcon("ball.png").getImage();
		playerImage = new ImageIcon("OtherPlayer.png").getImage();
		catcher = new ImageIcon("OtherCatcher.png").getImage();
		runner = new ImageIcon ("Player.png").getImage(); 

		// Add mouse listeners and Key Listeners to the game board
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();
		playerPos = new Point[9];
		playerVelocity = new Point[9];
		onBase = new boolean[4];

		newGame();
		// Create a timer object. This object generates an event every
		// TIME_INTERVAL milliseconds
		// The TimerEventHandler object that will handle this timer
		// event is defined below as a inner class
		timer = new Timer(TIME_INTERVAL, new TimerEventHandler());
		timer.start();

	}

	/**
	 * Starts a new game
	 */
	public void newGame()
	{
		status = 1;
		ballPos = new Point(428, 515);
		ballVelocity = new Point(0, 0);
		ballStop = 0;
		resetPlayers();
		noOfOuts = 0;
		jaysRuns = 0; 
		message = "";
		gameOver = false; 
		for (int base = 0; base < onBase.length; base++)
		{
			onBase [base]= false; 
		}
	}

	/**
	 * Places the players back to their original positions
	 */
	public void resetPlayers()
	{
		playerPos[0] = new Point(330, 340); // SS
		playerVelocity[0] = new Point(0, 0);
		playerPos[1] = new Point(500, 340); // 2B
		playerVelocity[1] = new Point(0, 0);
		playerPos[2] = new Point(250, 440); // 3B
		playerVelocity[2] = new Point(0, 0);
		playerPos[3] = new Point(575, 450); // 1B
		playerVelocity[3] = new Point(0, 0);
		playerPos[4] = new Point(200, 200); // LF
		playerVelocity[4] = new Point(0, 0);
		playerPos[5] = new Point(425, 125); // CF
		playerVelocity[5] = new Point(0, 0);
		playerPos[6] = new Point(625, 210); // RF
		playerVelocity[6] = new Point(0, 0);
		playerPos[7] = new Point(422, 495); // P
		playerVelocity[7] = new Point(0, 0);
		playerPos[8] = new Point(410, 675); // C
		playerVelocity[8] = new Point(0, 0);
		playerToMove = -1;
	}

	/**
	 * An inner class to deal with the timer events
	 */
	private class TimerEventHandler implements ActionListener
	{

		/**
		 * Decides if the outcome is a strike, single, double, triple, HR, or an out
		 * @param event the Timer event
		 */
		public void actionPerformed(ActionEvent event)
		{
			ballPos.x += ballVelocity.x;
			ballPos.y += ballVelocity.y;
			if (ballPos.y > playerPos[8].y && (status == 2 || status == 0))
			{
				//Called strike
				ballPos = new Point (428, 515);
				noOfStrikes++;
				message = "Strike " + noOfStrikes + "!";
				ballVelocity = new Point (0,0);
				status = 1;
				repaint();
				return;
			}
				
			if (ballPos.y < ballStop && (ballVelocity.y != 0)) {
				ballVelocity = new Point(0, 0);
				 System.out.println(Math.abs(ballPos.x - playerPos[playerToMove].x) + "    "
				 + Math.abs(finalBallPos.y - playerPos[playerToMove].y));
				// Decide whether it is a single or extra base hit
				if (Math.abs(ballPos.x - playerPos[playerToMove].x) < 15
						&& (Math.abs(finalBallPos.y - playerPos[playerToMove].y) < 15))
				{
					//Batter is out
					noOfOuts++; 
					message = "You Are Out!"; 
					pitchMessage = "Press ENTER to hit again";
					ballPos = new Point (-10,-10);
					noOfStrikes = 0;
				}
				else if (Math.abs(ballPos.x - playerPos[playerToMove].x) < 18
						&& (Math.abs(finalBallPos.y - playerPos[playerToMove].y) < 18))
				{
					// This is a single
					if (onBase[3])
					{
						jaysRuns++;
						onBase[3] = false;
					}
					if (onBase[2])
					{
						onBase[2] = false;
						onBase[3] = true;
					}
					if (onBase[1])
					{
						onBase[1] = false;
						onBase[2] = true;
					}
					onBase[1] = true; 
					message = "Single!"; 
					noOfStrikes = 0;

				}
				else if (Math.abs(finalBallPos.x - playerPos[playerToMove].x) < 22
						&& (finalBallPos.y - playerPos[playerToMove].y) < 22)
				{
					// Double
					if (onBase[3])
					{
						jaysRuns++;
						onBase[3] = false;
					}
					if (onBase[2])
					{
						onBase[2] = false;
						jaysRuns++;
					}
					if (onBase[1])
					{
						onBase[1] = false;
						onBase[3] = true;
					}
					onBase[2] = true;
					message = "Double!";
					noOfStrikes = 0;
				}
				else if (Math.abs(finalBallPos.x - playerPos[playerToMove].x) < 26 && Math.abs(finalBallPos.y - playerPos[playerToMove].y) < 26)
				{
					// Triple
					if (onBase[3])
					{
						jaysRuns++;
						onBase[3] = false;
					}
					if (onBase[2])
					{
						onBase[2] = false;
						jaysRuns++;
					}
					if (onBase[1])
					{
						onBase[1] = false;
						onBase[3] = true;
					}
					onBase[3] = true;
					message = "Triple!"; 
					noOfStrikes = 0;
				}
				else if (Math.abs(finalBallPos.x = playerPos[playerToMove].x) > 26 && Math.abs(finalBallPos.y - playerPos[playerToMove].y) > 26)
				{
					//Home run. Clears all the bases and adds the appropriate number of runs
					if (onBase[3])
					{
						jaysRuns++; 
						onBase[3] = false;
					}
					if (onBase [2])
					{
						jaysRuns++;
						onBase[2] = false;
					}
					if (onBase [1])
					{
						jaysRuns++;
						onBase[1] = false;
					}
					message = "HOME RUN!";
					noOfStrikes = 0; 
					jaysRuns++;
				}
				
				if (playerToMove >= 0)
				{
					playerVelocity[playerToMove] = new Point(0, 0);
					// playerToMove = -1;
				}
				pitchMessage = "Press ENTER to hit again";
			}

			if (playerToMove >= 0)
			{
				playerPos[playerToMove].x += playerVelocity[playerToMove].x;
				playerPos[playerToMove].y += playerVelocity[playerToMove].y;
			}

			// Repaint the screen
			repaint();
		}
	}

	/**
	 * Delays the given number of milliseconds
	 * @param milliSec The number of milliseconds to delay
	 */
	private void delay(int milliSec)
	{
		try
		{
			Thread.sleep(milliSec);
		}
		catch (InterruptedException e)
		{
		}
	}

	/**
	 * Repaint the field's drawing panel
	 * @param g The Graphics context
	 */
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.drawImage(baseballField, 0, 0, this);
		g.drawImage(baseball, ballPos.x, ballPos.y, this);
		if (onBase[1])
			//Man on first
			g.drawImage(runner, 560, 500, this);
		if (onBase[2])
			//Man on second
			g.drawImage(runner, 405, 340, this);
		if (onBase[3])
			//Man on third
			g.drawImage(runner, 255, 500, this);
		if (status == 2)
		{
			//Display instructions on how to swing
			g.setFont(new Font ("Arial", Font.BOLD, 20));
			g.setColor(new Color (255,255,255)); 
			pitchMessage = "Pitching...Press ENTER to swing"; 
			g.drawString(pitchMessage, 550, 700);
			message = "";
		}
		else if (status == 3)
		{
			//Display instructions on how to reset players
			g.setFont(new Font ("Arial", Font.BOLD, 20));
			g.setColor(new Color (255,255,255)); 
			pitchMessage = "Press ENTER to hit again"; 
			g.drawString(pitchMessage, 550, 750);
		}

		// Place the players in their respective positions
		for (int player = 0; player < playerPos.length - 1; player++)
		{
			g.drawImage(playerImage, playerPos[player].x, playerPos[player].y,
					this);
		}
		g.drawImage(catcher, playerPos[8].x, playerPos[8].y, this); 
		if (noOfOuts == 3)
		{
			g.setFont(new Font ("Arial", Font.BOLD, 30));
			g.setColor(new Color (255, 255, 255));
			g.drawString("Three out! You scored " + jaysRuns + " run(s)", 300, 200); 
			gameOver = true;
		}
		if (noOfStrikes == 3)
		{
			//Display feedback to the user
			g.setColor (new Color (255,255,255));
			message = "Strike Three! Batter Out!"; 
			noOfStrikes = 0;
			noOfOuts++; 
		}
		g.setFont(new Font ("Arial", Font.BOLD, 20));
		g.setColor(new Color (255, 255, 255));
		g.drawString("Outs: " + noOfOuts, 15, 650);
		g.setColor(new Color (255, 255, 255));
		g.drawString(message, 15, 700); 
		g.drawString("Strikes: " + noOfStrikes, 15, 600); 
		g.drawString("Runs: " + jaysRuns, 15, 550);
		g.drawString(pitchMessage, 550, 700);
		pitchMessage = "Press ENTER when ready"; 
	} // paint component method

	// Keyboard events you can listen for since this JPanel is a KeyListener
	/**
	 * Responds to a keyPressed event
	 * @param event Information about the key pressed event
	 */
	public void keyPressed(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if (gameOver)
			{
				//Stop the game after three outs are recorded
				JOptionPane.showMessageDialog(this,
						"Game Over. Please Select A New Game",
						"Game Over", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (status == 1)
			{
				// Set a random delay time between pressing the enter key and
				// the time of pitch
				delay((int) (Math.random() * 1000 + 1500)); 
				status = 2;
				ballVelocity = new Point(0, (int)( Math.random()*6 + 5));
			} 
			else if (status == 2)
			{
				status = 3;
				//Set all possible trajectories for the ball
				// Pull hit
				if (ballPos.y > 641 && ballPos.y <= 642)
				{
					ballVelocity = new Point(-1, -20);
				}
				else if (ballPos.y > 640 && ballPos.y <= 641)
				{
					ballVelocity = new Point(-2, -20);
				}
				else if (ballPos.y > 639 && ballPos.y <= 640)
				{
					ballVelocity = new Point(-3, -20);
				}
				else if (ballPos.y > 638 && ballPos.y <= 639)
				{
					ballVelocity = new Point(-4, -20);
				}
				else if (ballPos.y > 637 && ballPos.y <= 638)
				{
					ballVelocity = new Point(-5, -20);
				}
				else if (ballPos.y > 636 && ballPos.y <= 637)
				{
					ballVelocity = new Point(-6, -20);
				}
				else if (ballPos.y > 635 && ballPos.y <= 636)
				{
					ballVelocity = new Point(-7, -20);
				}
				else if (ballPos.y > 634 && ballPos.y <= 635)
				{
					ballVelocity = new Point(-8, -20);
				}
				else if (ballPos.y > 633 && ballPos.y <= 634)
				{
					ballVelocity = new Point(-9, -20);
				}
				else if (ballPos.y > 632 && ballPos.y <= 633)
				{
					ballVelocity = new Point(-10, -20);
				}
				else if (ballPos.y > 631 && ballPos.y <= 632)
				{
					ballVelocity = new Point(-11, -20);
				}
				else if (ballPos.y > 630 && ballPos.y <= 631)
				{
					ballVelocity = new Point(-12, -20);
				}
				else if (ballPos.y > 629 && ballPos.y <= 630)
				{
					ballVelocity = new Point(-13, -20);
				}
				else if (ballPos.y > 628 && ballPos.y <= 629)
				{
					ballVelocity = new Point(-14, -20);
				}
				else if (ballPos.y > 627 && ballPos.y <= 628)
				{
					ballVelocity = new Point(-15, -20);
				}
				else if (ballPos.y > 626 && ballPos.y <= 627)
				{
					ballVelocity = new Point(-16, -20);
				}
				else if (ballPos.y > 625 && ballPos.y <= 626)
				{
					ballVelocity = new Point(-17, -20);
				}
				else if (ballPos.y > 624 && ballPos.y <= 625)
				{
					ballVelocity = new Point(-18, -20);
				}
				else if (ballPos.y > 623 && ballPos.y <= 624)
				{
					ballVelocity = new Point(-19, -20);
				}
				else if (ballPos.y > 622 && ballPos.y <= 623)
				{
					ballVelocity = new Point(-20, -20);
				}

				// Hit up the middle
				else if (ballPos.y > 642 && ballPos.y <= 643)
				{
					ballVelocity = new Point(0, -20);
				}

				// Push hit
				else if (ballPos.y > 643 && ballPos.y <= 644)
				{
					ballVelocity = new Point(1, -20);
				}
				else if (ballPos.y > 644 && ballPos.y <= 645)
				{
					ballVelocity = new Point(2, -20);
				}
				else if (ballPos.y > 645 && ballPos.y <= 646)
				{
					ballVelocity = new Point(3, -20);
				}
				else if (ballPos.y > 646 && ballPos.y <= 647)
				{
					ballVelocity = new Point(4, -20);
				}
				else if (ballPos.y > 647 && ballPos.y <= 648)
				{
					ballVelocity = new Point(5, -20);
				}
				else if (ballPos.y > 648 && ballPos.y <= 649)
				{
					ballVelocity = new Point(6, -20);
				}
				else if (ballPos.y > 649 && ballPos.y <= 650)
				{
					ballVelocity = new Point(7, -20);
				}
				else if (ballPos.y > 650 && ballPos.y <= 651)
				{
					ballVelocity = new Point(8, -20);
				}
				else if (ballPos.y > 651 && ballPos.y <= 652)
				{
					ballVelocity = new Point(9, -20);
				}
				else if (ballPos.y > 652 && ballPos.y <= 653)
				{
					ballVelocity = new Point(10, -20);
				}
				else if (ballPos.y > 653 && ballPos.y <= 654)
				{
					ballVelocity = new Point(11, -20);
				}
				else if (ballPos.y > 654 && ballPos.y <= 655)
				{
					ballVelocity = new Point(12, -20);
				}
				else if (ballPos.y > 655 && ballPos.y <= 656)
				{
					ballVelocity = new Point(13, -20);
				}
				else if (ballPos.y > 656 && ballPos.y <= 657)
				{
					ballVelocity = new Point(14, -20);
				}
				else if (ballPos.y > 657 && ballPos.y <= 658)
				{
					ballVelocity = new Point(15, -20);
				}
				else if (ballPos.y > 658 && ballPos.y <= 659)
				{
					ballVelocity = new Point(16, -20);
				}
				else if (ballPos.y > 659 && ballPos.y <= 660)
				{
					ballVelocity = new Point(17, -20);
				}
				else if (ballPos.y > 660 && ballPos.y <= 661)
				{
					ballVelocity = new Point(18, -20);
				}
				else if (ballPos.y > 661 && ballPos.y <= 662)
				{
					ballVelocity = new Point(19, -20);
				}
				else if (ballPos.y > 662 && ballPos.y <= 663)
				{
					ballVelocity = new Point(20, -20);
				}
				else
				{
					//Swinging strike
					status = 0;
					repaint();
					return;
				}
				
				// The ball comes to a stop at a random place along its
				// trajectory
				// There are various probabilities for the distance of a hit
				double hitType = Math.random();

				// Fairly weak contact
				if (hitType < 0.5)
				{
					ballStop = (int) (Math.random() * 150 + 250);
				}

				// Base hit, potential extra base hit
				else if (hitType < 0.3)
				{
					ballStop = (int) (Math.random() * 100 + 125);
				}

				// Extra base hit
				else if (hitType < 0.15)
				{
					ballStop = (int) (Math.random() * 100 + 100);
				}

				// Home run
				else if (hitType < 0.05)
				{
					ballStop = (int) (Math.random() + 40);
				}

				finalBallPos = new Point(ballPos.x, ballPos.y);
				int timeUnits = 0;
				while (finalBallPos.y > ballStop)
				{
					//Make the ball stop
					finalBallPos.x += ballVelocity.x;
					finalBallPos.y += ballVelocity.y;
					timeUnits++;
				}

				playerToMove = 0;
				double leastDistance = Integer.MAX_VALUE;
				for (int player = 0; player < playerPos.length; player++)
				{
					//Find out which player is closest to the ball. That player will try to field the ball
					double distance = Math
							.sqrt(Math.pow(
									finalBallPos.x - playerPos[player].x, 2)
									+ Math.pow(finalBallPos.y
											- playerPos[player].y, 2));
					if (distance < leastDistance)
					{
						leastDistance = distance;
						playerToMove = player;
					}
				}

				//Move the player toward the batted ball
				playerVelocity[playerToMove] = new Point(
						(finalBallPos.x - playerPos[playerToMove].x)
								/ timeUnits,
						((finalBallPos.y - playerPos[playerToMove].y) / timeUnits));

			}
			else if (status == 3)
			{
				//Give the ball back to the pitcher and place the fielders in their original position
				status = 1;
				ballPos.x = 428;
				ballPos.y = 518;
				ballVelocity.x = 0;
				ballVelocity.y = 0;
				message = "";
				resetPlayers();
			}

			// Repaint the screen after the change
			repaint();
		}
	}

	// These are not needed
	public void keyReleased(KeyEvent event)
	{
	}

	public void keyTyped(KeyEvent event)
	{
	}

}
