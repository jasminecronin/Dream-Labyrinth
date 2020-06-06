package application;

import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Flask spawns at a random specific point at the top of the screen and chases down the player
 * While the distance between flask and player is greater than 450, the speed is doubled.
 * @author Quinn
 *
 */
public class Flask extends Sprite implements Universals 
{

	private Image image = new Image("/Assets/Erlenmeyer Flask.png"); 
	private ImageView imageView = new ImageView(image); 
	private Random r = new Random();

	public Flask(Pane Layer, int GameLevel) {
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.getImageView().setScaleX(0.6);
		this.getImageView().setScaleY(0.6);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
		this.setYpos(0);
		this.setGameLevel(GameLevel);
		Layer.getChildren().add(imageView);
		imageView.relocate(xpos, ypos);

		Update();
		
	}
	
	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) {
		double distance = Math.sqrt(Math.pow(this.getXpos()-Player.getXpos(), 2) + Math.pow(this.getYpos() - Player.getYpos(), 2)); //Calculates the distance between player and the enemy
		double direction = this.getXpos() - Player.getXpos();
		double descent = Player.getYpos() - this.getYpos();
		if (distance > 450)
		{
			this.setTranslateDX(-direction/250);
			this.setTranslateDY(descent/250);
		}
		else
		{
			this.setTranslateDX(-direction/500);
			this.setTranslateDY(descent/500);
		}
		
		this.setXpos(this.getXpos() +this.getTranslateDX());
		this.setYpos(this.getYpos() +this.getTranslateDY());
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}

	public void Update() // Updates the stats of the Sprite to match the current game level. as well as
							// set the base values for calculation
	{
		int n = this.GameLevel;
		this.setScore(10 + n/10);
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(145 * (0.9 + (n * .1))); // Health scales based on game level
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	

	}

	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		if (labyrinth == true)
		{
			spawnValues.Update(5);
			spawnValues.Balance(5);
		}
		player.setScore(player.getScore() + this.getScore());
	}
}
