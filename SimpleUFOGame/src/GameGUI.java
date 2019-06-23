import java.awt.BorderLayout;
import javax.swing.JFrame;


public class GameGUI extends JFrame {
	
	
	public GameGUI(AlienGame game) {
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(game, BorderLayout.CENTER);
		pack();
		
	}

}
