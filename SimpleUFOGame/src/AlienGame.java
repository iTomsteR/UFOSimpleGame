import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * This is the class which holds the Alien Maze Game panel.
 * @author Team "Still Looking 4 Partners"
 *
 */
public class AlienGame extends JPanel implements Observer, MouseListener, MouseMotionListener {
	
	private BufferedImage startingLabel, firstLevel, secondLevel, thirdLevel, currentStatus, gameWonLabel, gameLostLabel;
	private ImageIcon wonGameGIF, lostGameGIF;
	
	private boolean isInStartingLabel, isInEndingLabel;
	
	private Clip clip, clipForBackgroundSong;
	private AudioInputStream soundPlay, soundImpressive, soundPerfect, soundHumiliation, iWon, iLost, alienBackgroundSong;
	private Toolkit toolkit;
	private Image cursorImage;
	private Cursor cursor;
	
	/**
	 * Initialize all images and sounds of the game.
	 * @throws Exception
	 */
	public AlienGame() throws Exception {
		
		setLayout(new BorderLayout());
		
		startingLabel = ImageIO.read(new File("src/gameResources/startingLabel.png"));
		firstLevel = ImageIO.read(new File("src/gameResources/firstLevel.png"));
		secondLevel = ImageIO.read(new File("src/gameResources/secondLevel.png"));
		thirdLevel = ImageIO.read(new File("src/gameResources/finalLevel.png"));
		gameWonLabel = ImageIO.read(new File("src/gameResources/winLabel.png"));
		gameLostLabel = ImageIO.read(new File("src/gameResources/loseLabel.png"));
		wonGameGIF = new ImageIcon("src/gameResources/iWon.gif");
		lostGameGIF = new ImageIcon("src/gameResources/iLost.gif");
		
		//change the cursor of the game.
		toolkit = Toolkit.getDefaultToolkit();
		cursorImage = toolkit.getImage("src/gameResources/cursorShip.png");
		cursor = toolkit.createCustomCursor(cursorImage, new Point(0,0), "img");
		this.setCursor(cursor);
		
		// Load audio stream files.
		reloadAudioStream();
		
		isInStartingLabel = true;
		isInEndingLabel = false;
		
		// Set the starting position of the game.
		currentStatus = startingLabel;

		// Add the mouse listeners to the game panel.
		addMouseListener(this);
		addMouseMotionListener(this);
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method will be the main game controller.
	 * It will track the user's mouse movements and depending on that
	 * he may advance further into the game or simply lose.
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		
		// Check whether the player is currently in a level.
		if(!isInStartingLabel && !isInEndingLabel) {
			
	        int playerMouseIsOnColor = currentStatus.getRGB(e.getX(), e.getY());
	        
	        // This is the color of the ship track the cursor has to go through.
	        int trackColor = -16438710;
	        // This is the color where the cursor has to reach in order to advance further.
	        int shipTractorBeamColor = -8408833;
	        
	        // Check if the mouse has reached the mothership's tractor beam.
			if (playerMouseIsOnColor == shipTractorBeamColor) {
				
				// Proceed further depending on the level the user is currently on.
				if (currentStatus == firstLevel) {
					
					currentStatus = secondLevel;
					
					try {
						clip = AudioSystem.getClip();
						clip.open(soundImpressive);
					} catch (Exception ex) {
						System.out.println("'Impressive' game sound has not been found.");
						ex.printStackTrace();
					}
					clip.start();
					
				} else if (currentStatus == secondLevel) {

					currentStatus = thirdLevel;
					
					try {
						clip = AudioSystem.getClip();
						clip.open(soundPerfect);
					} catch (Exception ex) {
						System.out.println("'Perfect' game sound has not been found.");
						ex.printStackTrace();
					}
					clip.start();
					
				} else if (currentStatus == thirdLevel) {

					gameWon();

				}
			
			// If user lost on first level, play humiliating sound.
			} else if (currentStatus == firstLevel && playerMouseIsOnColor != trackColor) {
				
				try {
					
					clip = AudioSystem.getClip();
					clip.open(soundHumiliation);
				} catch (Exception ex) {
					System.out.println("'Humiliation' game sound has not been found.");
					ex.printStackTrace();
				}
				clip.start();
				gameLost();

			} else if (playerMouseIsOnColor != trackColor) {

				gameLost();
				
			}

			repaint();
			
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * This method is responsible for starting or restarting the game when
	 * an user presses the available labels of the program.
	 */
	@Override
	public void mousePressed(MouseEvent e) {

		// Check whether user is in starting label.
		if (currentStatus == startingLabel) {

			// Check if start button is pressed.
			if (e.getX() >= 330 && e.getX() <= 655 && e.getY() >= 500 && e.getY() <= 550) {

				System.out.println("Pressed start button. Starting game...");
				System.out.println("Bonus: A small cheat to win.");
				System.out.println("You only have to reach the tractor-beaming light coming from the mothership at the end of the level. \n"
						+ " Simply ALT+TAB during the level, remember the position of the beaming light and place your cursor there.");
				System.out.println("VOILA! You have reached the next level or won the game. You are welcome!");
				currentStatus = firstLevel;
				repaint();
				
				// Load and play the 'Start' game sound.
				try {
					clip = AudioSystem.getClip();
					clip.open(soundPlay);
				} catch (Exception ex) {

					System.out.println("'Start' game sound could not be played.");
					ex.printStackTrace();
				}
				clip.start();
				
				// Play looping background alien sound.
				try {
					clipForBackgroundSong = AudioSystem.getClip();
					clipForBackgroundSong.open(alienBackgroundSong);
					clipForBackgroundSong.loop(Clip.LOOP_CONTINUOUSLY);
				} catch (Exception ex) {

					System.out.println("'AlienBackgroundSong' game sound could not be played.");
					ex.printStackTrace();
				}
				clipForBackgroundSong.start();
				
				// User has exited starting label.
				isInStartingLabel = false;
				
			}
		
		} else if (currentStatus == gameWonLabel || currentStatus == gameLostLabel) {
			
			// Check if restart button is pressed.
			if (e.getX() >= 530 && e.getX() <= 960 && e.getY() >= 50 && e.getY() <= 90) {
				
				// Stop track if still playing.
				clip.stop();
				
				// Refresh audio stream since it has been used once.
				try {
					reloadAudioStream();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				// Remove all GIFs from screen.
				removeAll();
				
				System.out.println("Pressed restart button. Restarting game...");
				currentStatus = startingLabel;
				repaint();
				
				// User has restarted the game.
				isInStartingLabel = true;
				isInEndingLabel = false;
				
			}
			
		}

	}
	

	@Override
	public void mouseReleased(MouseEvent e) {

		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Set game background to the current status of game.
	 */
	@Override
	protected void paintComponent(Graphics g) {

		 g.drawImage(currentStatus, 0, 0, null);
		
	}
	
	/**
	 * This method will display the winning sounds, GIF and label of the game.
	 */
	private void gameWon()  {
		
		System.out.println("Congratulations! You did it. You are one hardcore gamer!");
		currentStatus = gameWonLabel;
		repaint();
		
		// Stop background track if still playing.
		clipForBackgroundSong.stop();
		
		// Display loser GIF.
		JLabel winGif = new JLabel(wonGameGIF, SwingConstants.CENTER);
		add(winGif, BorderLayout.CENTER);
		revalidate();
		
		isInEndingLabel = true;
		
		// Play victory sound.
		try {
			clip = AudioSystem.getClip();
			clip.open(iWon);
		} catch (Exception ex) {

			System.out.println("'Start' game sound could not be played.");
			ex.printStackTrace();
		}
		clip.start();
		
	}
	
	/**
	 * This method will display the losing sounds, GIF and label of the game.
	 */
	private void gameLost() {
		
		System.out.println("It doesn't matter if you win or lose, but you, weakling, lost!");
		currentStatus = gameLostLabel;
		repaint();
		
		// Stop background track if still playing..
		clipForBackgroundSong.stop();
		
		// Play defeat sound.
		try {
			clip = AudioSystem.getClip();
			clip.open(iLost);
		} catch (Exception ex) {

			System.out.println("'Lost' game sound could not be played.");
			ex.printStackTrace();
		}
		clip.start();
		
		// Display loser GIF.
		JLabel lostGif = new JLabel(lostGameGIF, SwingConstants.CENTER);
		add(lostGif, BorderLayout.SOUTH);
		revalidate();
		
		isInEndingLabel = true;
	}
	
	/**
	 * This method will reload all the audio input stream after
	 * some have already been played once.
	 * @throws Exception
	 */
	public void reloadAudioStream() throws Exception {
		
		soundPlay = AudioSystem.getAudioInputStream(new File("src/gameResources/play.wav"));
		soundImpressive = AudioSystem.getAudioInputStream(new File("src/gameResources/impressive.wav"));
		soundPerfect = AudioSystem.getAudioInputStream(new File("src/gameResources/perfect.wav"));
		soundHumiliation = AudioSystem.getAudioInputStream(new File("src/gameResources/humiliation.wav"));
		iWon = AudioSystem.getAudioInputStream(new File("src/gameResources/iWon.wav"));
		iLost = AudioSystem.getAudioInputStream(new File("src/gameResources/iLost.wav"));
		alienBackgroundSong = AudioSystem.getAudioInputStream(new File("src/gameResources/alienXFILESsong.wav"));
    	clip = AudioSystem.getClip();
    	clipForBackgroundSong = AudioSystem.getClip();
		
	}

}
