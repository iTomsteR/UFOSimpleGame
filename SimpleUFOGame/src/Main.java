public class Main {

	public static void main(String[] args) {
		AlienGame alienGame = null;
		try {
			alienGame = new AlienGame();
		} catch (Exception e) {
			System.out.println("Game resource files could not be found!");
			e.printStackTrace();
		}
		
		// Add the game panel.
		GameGUI gui = new GameGUI(alienGame);
		gui.setSize(980, 645);
		gui.setVisible(true);
		gui.setResizable(false);
	}
	
}
