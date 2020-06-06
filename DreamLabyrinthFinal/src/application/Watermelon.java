package application;

import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * From Spawn, the watermelon travels at a set speed (each spawn has a randomized speed) and "bounces" off the boundaries of the wall and the horizon.
 * At the end of the countdown, the watermelon fires seeds at the player.
 * @author Quinn
 *
 */
public class Watermelon extends Sprite implements Universals {
	private Image water = new Image("/Assets/Watermelon.png");
	private ImageView melon = new ImageView(water);
	private Random r = new Random();
	
	public Watermelon(Pane layer, int GameLevel) // default constructor that applies properties	to the created object.
	{		
		this.setLayer(layer);
		this.setImageView(melon);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - melon.getImage().getWidth()));
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - melon.getImage().getHeight()));
		this.setTranslateDX((r.nextDouble() *  5)+1); //random value between 1-6 
		this.setTranslateDY((r.nextDouble()*5)+1); //random value betweeen 1-6
		double rotation =  Math.atan(this.getTranslateDX()/this.getTranslateDY()) * (180 / Math.PI); //Set rotation to be parallel to the movement
		this.setGameLevel(GameLevel);
		layer.getChildren().add(melon);
		melon.relocate(xpos, ypos);
		this.getImageView().setRotate(-rotation);
		Update();
	}	
	
	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) 
	{
		int fire = this.getCountdown();
		double CheckX = this.getXpos() + this.getTranslateDX();
		double CheckY = this.getYpos() + this.getTranslateDY();
		this.setXpos(CheckX);
		this.setYpos(CheckY);
		fire = fire - r.nextInt(6);
		this.setCountdown(fire);
		if (CheckX < 0  ||  CheckX > SCRN_WIDTH - this.getImageView().getImage().getWidth()) //Vertical Border Check
		{
			this.setTranslateDX(-this.getTranslateDX());
			CheckX = this.getXpos() + this.getTranslateDX();
			if (this.getTranslateDX() > 0 )
			{
				double rotation = -this.getImageView().getRotate();
				this.getImageView().setRotate(rotation);
			}
			if (this.getTranslateDX() < 0)
			{
				double rotation = -this.getImageView().getRotate();
				this.getImageView().setRotate(rotation);
			}
			this.setXpos(CheckX);	
		}
		if (CheckY < 0 || CheckY > SCRN_HEIGHT * 7 / 9 ) //Horizon check
		{
			this.setTranslateDY(-this.getTranslateDY());
			CheckY = this.getYpos() + this.getTranslateDY();
			if (this.getTranslateDY() > 0)
			{
				double rotation = -this.getImageView().getRotate() + 180;
				this.getImageView().setRotate(rotation);
			}
			if (this.getTranslateDY() < 0)
			{
				double rotation = -this.getImageView().getRotate() + 180;
				this.getImageView().setRotate(rotation);
			}
			this.setYpos(CheckY);
		}	
		
		if (fire < 0)
		{
			Seeds seed  = new Seeds(bulletLayer, this.getGameLevel(), this.getXpos(), this.getYpos(), Player.getXpos(), Player.getYpos());
			enemyBullets.add(seed);
			this.setCountdown(this.getMaxCountdown()- r.nextInt(75));
		}
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}

	public void Update() 
	{
		int n = this.getGameLevel();
		this.setScore(20 + n/10);
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(150 * (0.9 + (n * .1))); // Health scales based on game level
		this.setTranslateDX(this.translateX * (0.9 + (0.1*n))); // movement scales based on game level
		this.setTranslateDY(this.translateY * (0.9 + (0.1*n))); // movement scales based on game level
		this.setCountdown(450 - r.nextInt(100)); // sets a timer for countdown for its movement method
		this.setMaxCountdown(450);
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	

	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup
	{
		if (labyrinth == true)
		{
			spawnValues.Update(7);
			spawnValues.Balance(7);
		}
		player.setScore(player.getScore() + this.getScore());
	}

}
