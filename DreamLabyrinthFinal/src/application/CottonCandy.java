package application;

import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * A "Labyrinth Mode" exclusive. The cotton candy is a fast moving enemy whose movement methods mimic the Cake's: 
 * randomized directions and speed, but at a lower scaling. The Cotton candy has no hit points, thus one-shot-one-kill.
 * @author Quinn
 *
 */
public class CottonCandy extends Sprite implements Universals {
	Image cotton = new Image("/Assets/Cotton Candy.png");
	ImageView candy = new ImageView(cotton);
	private Random r = new Random();
	
	public CottonCandy(Pane currentLevel, int i) {
		this.setLayer(currentLevel);
		this.setImageView(candy);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - this.getImageView().getImage().getWidth()));
		this.setYpos(0);
		this.setTranslateDX((r.nextDouble()* 5)-2.5); // range of -2.5 to 2.5, inclusive
		this.setTranslateDY((r.nextDouble()* 5)-2.5); // range of -2.5 to 2.5, inclusive
		this.setCountdown(1500);
		this.setMaxCountdown(1500);
		currentLevel.getChildren().add(candy);
		this.getImageView().relocate(xpos, ypos);
	}

	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player)
	{
		//countdown check to see the change in movement/randomization in movement
		int count = this.getCountdown();
		double checkMove = this.getDamage(); //Used as a secondary countdown to keep track of randomization
		double compare = (this.getMaxCountdown()- count)/300;
		if (compare > checkMove)
		{
			this.setTranslateDX((r.nextDouble() * 20 )-10);
			this.setTranslateDY((r.nextDouble() * 20 )-10);
			this.setDamage(checkMove +1);
		}
		count = count - r.nextInt(5);
		this.setCountdown(count);	
		
		//Apply movement
		double checkX = this.getXpos() + this.getTranslateDX();
		double checkY = this.getYpos() + this.getTranslateDY();
		this.setXpos(checkX);
		this.setYpos(checkY);
		if (checkX < 0 || checkX > SCRN_WIDTH - this.getImageView().getImage().getWidth())
		{
			this.setTranslateDX(-this.getTranslateDX());
			checkX = this.getXpos() + this.getTranslateDX();
			this.setXpos(checkX);
		}
		if (checkY < 0 || checkY > SCRN_HEIGHT * 2 / 3)
		{
			this.setTranslateDY(-this.getTranslateDY());
			checkY = this.getYpos() + this.getTranslateDY();
			this.setYpos(checkY);
		}
		
		if (this.getCountdown() < 0)
		{
			this.setCountdown(this.getMaxCountdown());
			this.setDamage(0);
		}
		
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		if (labyrinth == true)
		{
			spawnValues.Update(2);
			spawnValues.Balance(2);
		}
		player.setScore(player.getScore() + this.getScore());
	}
	

}
