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
 * A basic animated sprite that does not move until it shoots. Upon shooting, the textbook then 
 * teleports and resets the countdown before doing it again.
 * @author Quinn.
 *
 */

public class Textbook extends Sprite implements Universals 
{

	private Image image = new Image("/Assets/Textbook.png"); 
	private ImageView imageView = new ImageView(image); 
	private Random r = new Random();

	private final int COLUMNS = 1;
	private final int COUNT = 2;
	private final int ROWS = COUNT / COLUMNS;
	private final double WIDTH = imageView.getImage().getWidth() / COLUMNS;
	private final double HEIGHT = imageView.getImage().getHeight() / ROWS;
	final Animation animation;

	public Textbook(Pane Layer, int GameLevel) {
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - imageView.getImage().getHeight()));
		this.setGameLevel(GameLevel);
		Layer.getChildren().add(imageView);
		
		imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
		animation = new SpriteAnimation(imageView, Duration.millis(2000), COUNT, COLUMNS, WIDTH, HEIGHT);
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
		
		Update();
	}
	
	public void move(Pane playfieldLayer, Pane bulletLayer, PlayerSprite player)
	{
		if (this.getCountdown() > 0)
		{
			this.setCountdown(this.getCountdown() - r.nextInt(5));
		}
		else //teleport and releases a piece of paper
		{
			Paper repap = new Paper(playfieldLayer, this.getXpos(), this.getYpos(), player);
			enemies.add(repap);
			this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth()));
			this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - imageView.getImage().getHeight()));
			this.setCountdown(this.getMaxCountdown());
		}
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}
	
	public void Update()
	{
		int n = this.getGameLevel();
		setAnimated(true);
		this.setScore(13 + n/10);
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(450 * (0.9 + (n * .1))); // Health scales based on game level
		this.setTranslateDX(this.translateX * (0.9 + (0.1*n))); // movement scales based on game level
		this.setTranslateDY(this.translateY * (0.9 + (0.1*n))); // movement scales based on game level
		this.setCountdown(450 - r.nextInt(100)); // sets a timer for countdown for its movement method
		this.setMaxCountdown(450);
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);
	}
	
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) 
	{
		this.setDamage(r.nextInt(5) +1); //1-5, therefore 20% chance of NOT dropping a buff
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
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup
	{
		UpgradeChance(this.getLayer(), this.getXpos(), this.getYpos());
		if (labyrinth == true)
		{
			spawnValues.Update(6);
			spawnValues.Balance(6);
		}
		player.setScore(player.getScore() + this.getScore());
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

}
