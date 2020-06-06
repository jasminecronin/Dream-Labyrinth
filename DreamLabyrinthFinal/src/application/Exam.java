package application;

import java.util.List;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Exam Minion Archetype: Moves left to right only. And after covering a set
 * amount of distance the minion then "teleports".
 * The teleport will restart the horizontal movement. The Exam drops upgrades scaling with how long it takes for
 * the player to kill them. The faster the killing, the higher the grade. 
 * @author Quinn
 *
 */

public class Exam extends Sprite implements Universals {
	private Image image = new Image("/Assets/Exam_Enemy.png");
	private ImageView imageView = new ImageView(image);
	private Random r = new Random();

	public Exam(Pane layer, int GameLevel) // default constructor that applies properties and does NOT require any
											// arguments as a constructor.
	{
		this.setLayer(layer);
		this.setImageView(imageView);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - imageView.getImage().getHeight()));
		this.setTranslateDX(1);
		this.setTranslateDY(0);
		this.setScore(6 + GameLevel/10);
		this.setGameLevel(GameLevel);
		layer.getChildren().addAll(imageView); // Allocating the new "entity" to a branch/tree. TLDR: it's now a node
		imageView.relocate(xpos, ypos); // produces the image on the screen
		Update();
	}

	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) {
		int n = this.getCountdown(); // fetches current countdown timer
		double check = Math.abs(this.getDefaultX() - this.getXpos());
		if (n > 0) {
			if (check < 50) // only applies a movement and does NOT update countdown
			{
				double boundsCheck = this.getXpos() + this.getTranslateDX(); // creates a variable to check
				if (boundsCheck > 0 && boundsCheck < (SCRN_WIDTH - imageView.getImage().getWidth()))
				// checks to see if the new position is within bounds
				{
					this.setXpos(boundsCheck);
				} else
				// movement is out of bounds, so reverse it
				{
					this.setTranslateDX(-this.getTranslateDX());
					this.setXpos(this.getXpos() + this.getTranslateDX());
				}
			} else // applies a movement AND updates the countdown (as it has traveled over a
					// variable)
					// this will forego the need to manage a "sequence" based on timer and render it
					// based on total movements
			{
				double boundsCheck = this.getXpos() + this.getTranslateDX(); // creates a variable to check
				if (boundsCheck > 0 && boundsCheck < (SCRN_WIDTH - imageView.getImage().getWidth()))
				// checks to see if the new position is within bounds
				{
					this.setXpos(boundsCheck);
					this.setDefaultX(this.getXpos()); // updates the DefaultX for calculations
					this.setCountdown(n - 1); // updates countdown
				} else
				// movement is out of bounds, so reverse it
				{
					this.setTranslateDX(-this.getTranslateDX());
					this.setXpos(this.getXpos() + this.getTranslateDX());
					this.setDefaultX(this.getXpos()); // updates the DefaultX for calculations
					this.setCountdown(n - 1);
				}
			}
		} else // countdown is 0 and thus proceeds to do an effect
		{
			this.setDefaultX(this.getXpos()); // grabs and set the current xpos for move method
			this.setDefaultY(this.getYpos()); // grabs and set the current ypos for move method
			// do the teleportation
			this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
			this.setYpos(((r.nextDouble() * SCRN_HEIGHT / 1.8) + (100))); // minimum value of 100, max of 600
			this.imageView.relocate(this.getXpos(), this.getYpos());
			this.setCountdown(this.getMaxCountdown()); // reset countdown
			this.setDamage(this.getDamage()-1);
			
		}
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}

	public void Update() // Updates the stats of the Sprite to match the current game level. as well as
							// set the base values for calculation
	{
		int n = this.GameLevel;
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(130 * (0.9 + (0.1*n))); // Health scales based on game level
		this.setTranslateDX(this.translateX * (0.9 + (0.1*n))); // movement scales based on game level
		this.setCountdown(15 - r.nextInt(n)); // sets a timer for countdown for its movement method
		this.setMaxCountdown(15); 
		this.setDamage(5); // used to determine if the exam drops an upgrade on death
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);

	}
	
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) //Spawns the relevant upgrade based on their "damage"
	{
		if (this.getDamage() == 5)
		{
			Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 5);
			upgrades.add(boop);
		}
		else if (this.getDamage() == 4)
		{
			Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 4);
			upgrades.add(boop);
		}
		else if (this.getDamage() == 3)
		{
			Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 3);
			upgrades.add(boop);
		}
		else if (this.getDamage()== 2)
		{
			Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 2);
			upgrades.add(boop);
		}
		
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		UpgradeChance(this.getLayer(), this.getXpos(), this.getYpos());
		if (labyrinth == true)
		{
			spawnValues.Update(3);
			spawnValues.Balance(3);
		}
		player.setScore(player.getScore() + this.getScore());
	}

}
