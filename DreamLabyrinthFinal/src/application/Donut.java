package application;

import java.util.List;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Donut is the most basic of enemies. Simple movement and never attacks the
 * player. Instead, their purpose is to "clog" up the board, absorbing
 * projectiles for other potential minions/bosses. As a result, they have a
 * decent amount of health, but nothing too absurd. Their movement is quite
 * simple: Some units horizontally, then one movement down (or up if they've
 * reached the 'horizon')
 * 
 * @author Quinn
 *
 */
public class Donut extends Sprite implements Universals // Most basic of enemies: no projectile, no insane movements.
														// Just a simple body to shoot at.
{

	Image image = new Image("/Assets/Donut Enemy.png"); // or can run the alternative method
	ImageView imageView = new ImageView(image); // creates the node for Donut based on the image **Each image will
													// have their own attributes
	private Random r = new Random();

	public Donut(Pane Layer, int GameLevel) // basic properties/constructor of Donut: HP, and simple movement
											// values (scaling with level)
	{
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - imageView.getImage().getHeight()));
		this.setTranslateDX(0.01);
		this.setTranslateDY(.1);
		this.setScore(1 + GameLevel/10);
		this.setGameLevel(GameLevel);
		Layer.getChildren().add(imageView);
		imageView.relocate(xpos, ypos);

		Update();
	}

	public void move(Pane playLayer, Pane bulletLayer,PlayerSprite Player) // method of movement: move horizontally, then move down. Repeat
	// This is only the method for updating the values of the sprite to move.
	// does not update the graphics or scene (as that is managed by the scene/main)
	{
		int n = this.getCountdown();
		if (n != 0) {
			double check = this.getXpos() + this.getTranslateDX();
			if (check > 0 && check < (SCRN_WIDTH - imageView.getImage().getWidth()))
			// checks boundaries to make sure the right edge of the sprite is within the
			// border
			{
				this.setXpos(check);
				this.setDefaultX(this.getXpos());
				this.setCountdown(n - 1); // updates countdown
			} else
			// if the sprite would be out of bounds, instead reverse direction of the
			// sprite to maintain it within the window.
			{
				this.setTranslateDX(-this.getTranslateDX()); //
				this.setXpos(this.getTranslateDX() + this.getXpos());
				this.setCountdown(n - 1); // updates countdown
			}

		} else // vertical movement and resetting the countdown
		{
			double check = this.getYpos() + this.getTranslateDY();
			if (check > 0 && check < SCRN_HEIGHT / 1.3)
			// checks to see if the sprites' position is within the window range and above
			// the "horizon"
			{
				this.setYpos(check); // updates to the position
				this.setCountdown(this.getMaxCountdown()); // resets countdown timer
			} else
			// check is out of bounds. if so reverse the vertical direction, update, then
			// reset countdown.
			{
				this.setTranslateDY(-this.getTranslateDY());
				this.setYpos(this.getYpos() + this.getTranslateDY());
				this.setCountdown(this.getMaxCountdown());
			}
		}
		
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}

	public void Update() // Updates the stats of the Sprite to match the current game level. as well as
							// set the base values for calculation
	{
		int n = this.GameLevel;
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(50 * (0.9 + (n * .1))); // Health scales based on game level
		this.setTranslateDX(this.translateX + (.1 * n)); // movement scales based on game level
		this.setTranslateDY(this.translateY + (.1 * n)); // movement scales based on game level
		this.setCountdown(250); // sets a timer for countdown for its movement method
		this.setMaxCountdown(250);
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	

	}
	
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) //upgrade chance of 2%
	{
		if (r.nextInt(100)%47 == 0)
		{
			Coffee boop = new Coffee (playfieldLayer, this.getXpos(), this.getYpos());
			upgrades.add(boop);
		}
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		UpgradeChance(this.getLayer(), this.getXpos(), this.getYpos());
		if (labyrinth == true)
		{
			spawnValues.Update(0);
			spawnValues.Balance(0);
		}
		player.setScore(player.getScore() + this.getScore());
	}

}
