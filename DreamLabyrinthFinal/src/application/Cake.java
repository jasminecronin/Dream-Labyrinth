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
 * The Cake boss, the first of the bosses, is a simple boss that just randomizes its own movement (to reduce predictability)
 * while firing at random intervals, with a guaranteed "burst" fire at the "end" of the movement. 
 * @author Quinn
 *
 */
public class Cake extends Sprite implements Universals {
	
	Image cake = new Image("/Assets/Cake.png"); //or can run the alternative method
	ImageView boss = new ImageView(cake); //creates the node for Cake based on the image **Each image will have their own attributes

	/**
	 * The following private values are ONLY for animating the sprite and do not affect the creation
	 * of the Cake object.
	 * @author Quinn
	 */
    private final int COLUMNS  =  1; // The number of columns in the Sprite Spreadsheet
    private final int COUNT    =  2; // the number of images within the Sprite Spreadsheet
    private final double WIDTH    = boss.getImage().getWidth(); // the length of the spreadsheet
    private final double HEIGHT   = boss.getImage().getHeight()/(COUNT/COLUMNS); //the height of a specific view within the spreadsheet
    final Animation animation;
    private Random r = new Random();

    /**
     * Basic Constructor for the CakeBoss that takes in specific parameters that are stored and maintained in Main.
     * @param Layer - The Layer in Main where the object's image will appear
     * @param GameLevel - Current game level to appropriately scale the object's game values
     */
	public Cake(Pane Layer, int GameLevel) // basic properties/constructor of CakeBoss: HP, and simple movement values (scaling with level)
	{	
		this.setLayer(Layer);
		this.setImageView(boss);
		this.setXpos(10); //Ensures the object spawns within the window
		this.setYpos(10); // Ensures the object spawns in the "sky"
		this.setGameLevel(GameLevel); //Sets the current level as a value to assist in scaling
		this.setScore(75 + GameLevel/10);
		Layer.getChildren().add(boss); //Adds the Node to the layer
		boss.relocate(xpos, ypos); //Moves the image to the specified location
		
		Update(); // Updates the values of the CakeBoss to scale based off the level
		
		boss.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
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
	
	public void Update() // Updates the values of the Cake.
	{
		int n = this.getGameLevel()/3;
		setAnimated(true);
		this.setHealth(1250 * (0.9 + (0.1*n)));
		this.setMaxHealth(1250 * (0.9 + (0.1*n)));
		this.setDefaultX(SCRN_WIDTH/2 - this.getImageView().getImage().getWidth()/2); //Center of the screen
		this.setDefaultY(SCRN_HEIGHT/2 - this.getImageView().getImage().getHeight()/2); //Center of the screen.
		this.setTranslateDX((r.nextDouble()* 20)-10); // range of -10 to 10, inclusive
		this.setTranslateDY((r.nextDouble()* 20)-10); // range of -10 to 10, inclusive
		this.setCountdown(1500);
		this.setMaxCountdown(1500);
		this.setDamage(0); //Used as a secondary counter for the AI
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	
		
	}
	/**
	 * End of Countdown action: teleports to a random location and releases a spray of candles in a spread.
	 * @param Layer passes in the layer where the bullets would be placed.
	 */
	
	
	public void CakePhase(Pane Layer)  // end of countdown action
	{
		this.setTranslateDX(0);
		this.setTranslateDY(0);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - this.getImageView().getImage().getWidth()));
		this.setYpos(r.nextDouble() * SCRN_HEIGHT/1.3);
		this.getImageView().relocate(this.getXpos(), this.getYpos());

		for ( int i = 1; i < 20 ; i ++)
		{
			double direction = (SCRN_HEIGHT * Math.tan(Math.PI/12 * i)) + this.getImageView().getImage().getWidth()/2;
			Candle candle = new Candle (Layer, this.getGameLevel(), this.getXpos() + this.getImageView().getImage().getWidth()/2, this.getYpos(), direction, SCRN_HEIGHT);
			enemyBullets.add(candle);
		}
	}
	
	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player)
	{
		//countdown check to see the change/randomization in movement
		int count = this.getCountdown();
		double checkMove = this.getDamage();
		double compare = (this.getMaxCountdown()- count)/50; 
		if (compare > checkMove)
		{
			this.setTranslateDX((r.nextDouble() * 20 )-10);
			this.setTranslateDY((r.nextDouble() * 20 )-10);
			this.setDamage(checkMove +1);
		}
		count = count - r.nextInt(5); //randomizes the deduction in countdown to reduce predictability
		this.setCountdown(count);	
		
		//Apply movement and moving the imageview
		double checkX = this.getXpos() + this.getTranslateDX();
		double checkY = this.getYpos() + this.getTranslateDY();
		this.setXpos(checkX);
		this.setYpos(checkY);
		if (checkX < 0 || checkX > SCRN_WIDTH - this.getImageView().getImage().getWidth()) //boundary checking
		{
			this.setTranslateDX(-this.getTranslateDX());
			checkX = this.getXpos() + this.getTranslateDX();
			this.setXpos(checkX);
		}
		if (checkY < 0 || checkY > SCRN_HEIGHT * 2 / 3) //boundary checking
		{
			this.setTranslateDY(-this.getTranslateDY());
			checkY = this.getYpos() + this.getTranslateDY();
			this.setYpos(checkY);
		}
		
		//shoot method
		
		if (count % 233 ==0 && count > 0) // A fibonacci prime
		{
			Candle candle  = new Candle (bulletLayer, this.getGameLevel(), this.getXpos(), this.getYpos(), this.getXpos(), SCRN_HEIGHT);
			enemyBullets.add(candle);
		}
		if (count < 0) //reset countdown and initiate the "end of countdown" action.
		{
			CakePhase(bulletLayer);
			this.setCountdown(MaxCountdown);
			this.setDamage(0);
		}
		
		this.getImageView().relocate(this.getXpos(), this.getYpos());	//updates the front-end of the GUI
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		if (labyrinth == true)
		{
			spawnValues.Update(10);
			spawnValues.Balance(10);
		}
		player.setScore(player.getScore() + this.getScore());
	}

	public Animation getAnimation() {
		return this.animation;
	}
}
