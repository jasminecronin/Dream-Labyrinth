package application;

import java.util.List;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *Spawns the ice cream at a random spot on the screen that moves horizontally in a sinusoidal wave.
 * @author Quinn
 *
 */
public class IceCream extends Sprite implements Universals {

	Image image = new Image("/Assets/Ice Cream.png");
	ImageView imageView = new ImageView(image);
	private Random r = new Random();

	public IceCream(Pane Layer, int GameLevel) {
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT - imageView.getImage().getHeight()));
		this.setTranslateDX(1);
		this.setTranslateDY(.1);
		this.setGameLevel(GameLevel);
		Layer.getChildren().add(imageView);
		imageView.relocate(xpos, ypos);

		Update();
	}

	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) {

		double check = this.getXpos() + this.getTranslateDX();
		if (check > 0 && check < (SCRN_WIDTH - imageView.getImage().getWidth()))
		// checks boundaries to make sure the right edge of the sprite is within the
		// border
		{
			this.setXpos(check);
			this.setDefaultX(this.getXpos());

		} else
		// if the sprite would be out of bounds, instead reverse direction of the
		// sprite to maintain it within the window.
		{
			this.setTranslateDX(-this.getTranslateDX()); //
			this.setXpos(this.getTranslateDX() + this.getXpos());

		}

		// vertical movement modeled as a sin wave
		this.setYpos((150 * Math.sin(Math.toDegrees(this.getXpos() / 2))) + 200); // updates the Y position
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}

	public void Update() // Updates the stats of the Sprite to match the current game level. as well as
							// set the base values for calculation
	{
		int n = this.GameLevel;
		this.setScore(2 + n/10);
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(75 * (0.9 + (n * .1))); // Health scales based on game level
		this.setTranslateDX(this.translateX + (.1)); // Need this for the amplification of the Sinusoidal wave
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	
	}
	
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) //5% chance to spawn the upgrade
	{
		if (r.nextInt(100)%19 == 0)
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
			spawnValues.Update(1);
			spawnValues.Balance(1);
		}
		player.setScore(player.getScore() + this.getScore());
	}

}