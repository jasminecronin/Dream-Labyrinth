package application;

import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Pterodactyl enemy that is agile (with its horizontal movement). 
 * As a result, they have a low amount of health, but are quite agile and move quickly. 
 * At the end of their countdown, they dive bomb towards the player.
 * @author Quinn
 *
 */
public class Pterodactyl extends Sprite implements Universals 	//Most basic of enemies: no projectile, no insane movements. Just a simple body to shoot at.
{
	

	Image ptero = new Image("/Assets/Pterodactyl Enemy.png"); //or can run the alternative method
	ImageView pteroImage = new ImageView(ptero); //creates the node for NyanCat based on the image **Each image will have their own attributes
	private Random r = new Random();
	
	/**
	 * The following private values are ONLY for animating the sprite and do not affect the creation
	 * of the Pterodactyl object.
	 * @author Quinn
	 */
    private final int COLUMNS  =  1; // The number of columns in the Sprite Spreadsheet
    private final int COUNT    =  2; // the number of images within the Sprite Spreadsheet
    private final double WIDTH    = pteroImage.getImage().getWidth(); // the length of the spreadsheet
    private final double HEIGHT   = pteroImage.getImage().getHeight()/(COUNT/COLUMNS); //the height of a specific view within the spreadsheet
    final Animation animation;

    /**
     * Basic Constructor for the Pterodactyl that takes in specific paramenters that are stored and maintained in Main.
     * @param Layer - The Layer in Main where the object's image will appear
     * @param r - randomizer to assist in randomizing the starting position for the object
     * @param GameLevel - Current game level to appropriately scale the object's game values
     */
	public Pterodactyl(Pane Layer, int GameLevel) // basic properties/constructor of Pterodactyle: HP, and simple movement values (scaling with level)
	{	
		this.setLayer(Layer);
		this.setImageView(pteroImage);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - pteroImage.getImage().getWidth())); //Ensures the object spawns within the window
		this.setYpos(r.nextDouble()*(SCRN_HEIGHT*7/9 - pteroImage.getImage().getHeight())); // Ensures the object spawns in the "sky"
		this.setTranslateDY(0); //Restricts the object's vertical movement
		this.setGameLevel(GameLevel); //Sets the current level as a value to assist in scaling
		Layer.getChildren().add(pteroImage); //Adds the Node to the layer
		pteroImage.relocate(xpos, ypos); //Moves the image to the specified location
		
		Update(); // Updates the values of the Pterodactyl to scale based off the level
		
		pteroImage.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
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
	
	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) //method of movement: move horizontally.
	//This is only the method for updating the values of the sprite to move.
	//does not update the graphics or scene (as that is managed by the scene/main) 
	{
		if (this.getDamage() ==0 )
		{
			this.setCountdown(this.getCountdown()-1);
			double checkX = this.getXpos() + this.getTranslateDX();
			double checkY = this.getYpos() + this.getTranslateDY();	
			if (checkX > 0 && checkX < (SCRN_WIDTH - pteroImage.getImage().getWidth())) 
				// checks boundaries to make sure the right edge of the sprite is within the border
			{
				this.setXpos(checkX);
				this.setDefaultX(this.getXpos());
			}
			else 
				//if the sprite would be out of bounds, instead reverse direction of the 
				//sprite to maintain it within the window.
			{
				this.setTranslateDX(-this.getTranslateDX()); //
				this.setXpos(this.getTranslateDX() + this.getXpos());
			}
			if (checkY > SCRN_HEIGHT) //if the object is beyond the window screen, they respawn and resume their horizontal movement.
			{
				this.setTranslateDX(5 * (.9+(this.getGameLevel()*.1))); //movement scales based on game level
				this.setTranslateDY(0);
				this.setXpos(r.nextDouble()*(SCRN_WIDTH - this.getImageView().getImage().getWidth()));
				this.setYpos(r.nextDouble()*450);
				this.setCountdown(this.getMaxCountdown());	
			}
			else
			{
				this.setYpos(checkY);
			}
			
			if (this.getCountdown() < 0)
			{
				this.setCountdown(this.getMaxCountdown());
				this.setDamage(1);
			}
		}
		else
		{
			int n = this.getGameLevel();
			double direction = this.getXpos() - Player.getXpos();
			double descent = Player.getYpos() - this.getYpos();
			double rotation = Math.atan(direction/descent) * (180 / Math.PI);
			this.setTranslateDX(-direction/250 *n);
			this.setTranslateDY(descent/250 *n);
			this.setDamage(0);
		}
		this.getImageView().relocate(this.getXpos(), this.getYpos());			
	}
	
	public void Update() //Updates the stats of the Sprite to match the current game level. as well as set the base values for calculation
	{
		int n = this.GameLevel ;
		this.setScore(25 + n/10);
		setAnimated(true);
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos());
		this.setHealth(200*(0.9+(0.1*n))); //Health scales based on game level
		this.setTranslateDX(5 * (.9+(n*.1))); //movement scales based on game level
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.setCountdown(350);
		this.setMaxCountdown(350);
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);		
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		if (labyrinth == true)
		{
			spawnValues.Update(8);
			spawnValues.Balance(8);
		}
		player.setScore(player.getScore() + this.getScore());
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
}
