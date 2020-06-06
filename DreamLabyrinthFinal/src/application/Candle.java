package application;

import java.util.Random;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * The Candle is the cake's bullet that just fires in a general direction based on phase.
 * The Candle's movement has a default base and slowly increases by .5% every frame.
 * @author Quinn
 *
 */

public class Candle extends Sprite implements Universals {
	
	Image candle = new Image("/Assets/Candle.png");
	ImageView flame = new ImageView(candle);
	
    private final int COLUMNS  =  1; // The number of columns in the Sprite Spreadsheet
    private final int COUNT    =  2; // the number of images within the Sprite Spreadsheet
    private final double WIDTH    = flame.getImage().getWidth(); // the length of the spreadsheet
    private final double HEIGHT   = flame.getImage().getHeight()/(COUNT/COLUMNS); //the height of a specific view within the spreadsheet
    final Animation animation;
    private Random r = new Random();
	
	public Candle(Pane Layer, int gameLevel, double spawnX, double spawnY, double targetX, double targetY) // default constructor for the candle
	{
		this.setLayer(Layer);
		this.setImageView(flame);
		this.setGameLevel(gameLevel);
		this.setXpos(spawnX);
		this.setYpos(spawnY);
		this.getImageView().setScaleX(0.45);
		this.getImageView().setScaleY(0.45);
		Layer.getChildren().add(flame);
		this.getImageView().relocate(this.xpos, this.ypos);
		Update(targetX, targetY);
		
		flame.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
	    animation = new SpriteAnimation(
	            imageView,
	            Duration.millis(1000), //sets the duration of animation cycle
	            COUNT, //The number of frames the animation goes through
	            COLUMNS,
	            WIDTH, HEIGHT
	    );
	    animation.setCycleCount(Animation.INDEFINITE); //sets the number of cycles in the animation
	    animation.play(); //begin the animation cycle of the sprite
	}
	
	public void move() //Gradually increase the travel by .5% every frame.
	{
		this.setTranslateDX(this.getTranslateDX() * 1.005);
		this.setTranslateDY(this.getTranslateDY() * 1.005);
		this.setXpos(this.getXpos() + this.getTranslateDX());
		this.setYpos(this.getYpos() + this.getTranslateDY());
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}
	
	public void Update(double pXpos, double pYpos)
	{
		int n = this.getGameLevel();
		double direction = this.getXpos() - pXpos;
		double descent = pYpos - this.getYpos();
		this.setTranslateDX(-direction/500); // Sets the travel "time" to reach the destination in 500 frames
		this.setTranslateDY(descent/500); // Sets the travel "time" to reach the destination in 500 frames
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

}
