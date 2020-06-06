package application;

import java.util.List;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
/**
 * PlayerSprite: the creation and handling of all Player stats and methods.
 * Movement, firing, scores, health, pausing, animation, and the works. 
 * @author Quinn
 *
 */
public class PlayerSprite extends Sprite implements Universals {

	private Image image = new Image("/Assets/Player (1).png");
	private ImageView imageView = new ImageView(image);
	
	private String phaseAmmoDirectory;

	private final int COLUMNS = 1;
	private final int COUNT = 2;
	private final int ROWS = COUNT / COLUMNS;
	private final double WIDTH = imageView.getImage().getWidth() / COLUMNS;
	private final double HEIGHT = imageView.getImage().getHeight() / ROWS;
	private final Animation animation;

	private int MaxFireRate; // Sets a MaxFireRate that can be adjusted via upgrades through gameplay
	private int FireRate; // Set a FireRate/Countdown to manage FireRate
	private int damage; // Sets a Base damage for player bullets
	private boolean leftPressed = false; // for multi-key press
	private boolean rightPressed = false; // for multi-key press
	private boolean spacePressed = false; // for multi-key press
	private boolean pause;
	private int score; // Current player's score
	private double DefaultDX; //Used for some enemies

	//default constructor for the player.
	public PlayerSprite(Pane Layer, int FireRate, double Damage) {
		// Establishing and setting base attributes
		this.setLayer(Layer);
		setAnimated(true);
		this.setImageView(imageView);
		this.setXpos((SCRN_WIDTH / 2) - (imageView.getImage().getWidth() / 2));
		this.setYpos(SCRN_HEIGHT - imageView.getImage().getHeight() - BOTTOM_BORDER);
		this.setTranslateDX(10);
		this.setDefaultDX(10);
		this.setScore(0);
		this.setHealth(5);
		this.setDamage(Damage);
		this.setMaxFireRate(FireRate);
		this.setFireRate(FireRate);
		this.setMaxCountdown(100);
		Layer.getChildren().addAll(imageView); // Allocating the new "entity" to a branch/tree. TLDR: it's now a node
		imageView.relocate(xpos, ypos); // produces the image on the screen
		// Start the player sprite in the center of the scene.
		this.setDefaultX(this.getXpos()); // used for calculations/transferring the sprite from one scene/level to
											// another

		/**
		 * Allocate Animation for the player sprite, then initialize said animation
		 */
		imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
		animation = new SpriteAnimation(imageView, Duration.millis(1000), COUNT, COLUMNS, WIDTH, HEIGHT);
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
	}

	//Copy constructor to avoid a "duplicate children" error.
	public PlayerSprite(Pane Layer, PlayerSprite player) {
		// Establishing and setting base attributes
		this.setLayer(Layer);
		setAnimated(true);
		this.setImageView(imageView);
		this.setXpos(player.getXpos());
		this.setYpos(player.getYpos());
		this.setTranslateDX(player.getTranslateDX());
		this.setDefaultDX(player.getDefaultDX());
		this.setScore(player.getScore());
		this.setHealth(player.getHealth());
		this.setDamage(player.getDamage());
		this.setMaxFireRate(player.getMaxFireRate());
		this.setFireRate(player.getMaxFireRate());
		this.setMaxCountdown(player.getMaxCountdown());
		Layer.getChildren().addAll(imageView); // Allocating the new "entity" to a branch/tree. TLDR: it's now a node
		imageView.relocate(xpos, ypos); // produces the image on the screen
		// Start the player sprite in the center of the scene.
		// another

		/**
		 * Allocate Animation for the player sprite, then initialize said animation
		 */
		imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
		animation = new SpriteAnimation(imageView, Duration.millis(1000), COUNT, COLUMNS, WIDTH, HEIGHT);
		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getMaxFireRate() {
		return MaxFireRate;
	}

	public void setMaxFireRate(int maxFireRate) {
		MaxFireRate = maxFireRate;
	}

	public int getFireRate() {
		return FireRate;
	}

	public void setFireRate(int fireRate) {
		FireRate = fireRate;
	}

	public boolean isLeftPressed() {
		return leftPressed;
	}

	public void setLeftPressed(boolean leftPressed) {
		this.leftPressed = leftPressed;
	}

	public boolean isRightPressed() {
		return rightPressed;
	}

	public void setRightPressed(boolean rightPressed) {
		this.rightPressed = rightPressed;
	}

	public boolean isSpacePressed() {
		return spacePressed;
	}

	public void setSpacePressed(boolean spacePressed) {
		this.spacePressed = spacePressed;
	}

	public void setPause(boolean stopGame) {
		pause = stopGame;
	}

	public boolean isPaused() {
		return pause;
	}
	
	public double getDefaultDX()
	{
		return this.DefaultDX;
	}
	
	public void setDefaultDX(double d) {
		this.DefaultDX=d;
	}
	
	public void setPhaseAmmo(int phase) {
		if (phase <= 4) {
			phaseAmmoDirectory = "/Assets/Heart Ammo.png";
		}
		else {
			phaseAmmoDirectory = "/Assets/Pencil Ammo.png";
		}
	}

	public void playerKeyPressedHandler(KeyEvent keyPressedEvent) { // KeyPressed Handler
		switch (keyPressedEvent.getCode()) {
		case LEFT:
			setLeftPressed(true);
			break;
		case RIGHT:
			setRightPressed(true);
			break;
		case SPACE:
			setSpacePressed(true);
			break;
		case P:
			setPause(true);
			break;
		default:
			break;
		}
	}

	public void playerKeyReleasedHandler(KeyEvent keyReleasedEvent) { // Key Released Handler
		switch (keyReleasedEvent.getCode()) {
		case LEFT:
			setLeftPressed(false);
			break;
		case RIGHT:
			setRightPressed(false);
			break;
		case SPACE:
			setSpacePressed(false);
			break;
		default:
			break;
		}
	}

	
	public void displayHealth(Pane layer) { // Show hearts corresponding to playerHealth
		for (int i = 1; i < this.getHealth() + 1; i++) {
			PlayerHealth heart = new PlayerHealth(layer, i * 25 - 40);
			hearts.add(heart);

		}
	}

	public void left() // Moving left with boundary checks to prevent the player from leaving the
						// window
	{
		double check = this.getXpos() - this.getTranslateDX();
		if (check > 0 && check < (SCRN_WIDTH - imageView.getImage().getWidth())) {
			this.setXpos(this.getXpos() - this.getTranslateDX());
			this.getImageView().relocate(this.getXpos(), this.getYpos());
		} else {
			double fix = Math.max(0.0, (this.getXpos() - this.getTranslateDX()));
			this.setXpos(fix);
			this.getImageView().relocate(this.getXpos(), this.getYpos());
		}
	}

	public void right() // moving right with boundary checks to prevent the player from leaving the
						// window
	{

		double check = this.getXpos() + this.getTranslateDX();
		if (check > 0 && check < (SCRN_WIDTH - imageView.getImage().getWidth())) {
			this.setXpos((this.getXpos() + this.getTranslateDX()));
			this.getImageView().relocate(this.getXpos(), this.getYpos());
		} else {
			double fix = Math.min((SCRN_WIDTH - imageView.getImage().getWidth()),
					(this.getXpos() + this.getTranslateDX()));
			this.setXpos(fix);
			this.getImageView().relocate(this.getXpos(), this.getYpos());
		}
	}

	@Override
	public void move() { // Move method for player
		if (leftPressed)
			this.left(); // Calling the movement method in PlayerSprite
		if (rightPressed)
			this.right(); // Calling the movement method in PlayerSprite
	}

	public void shoot(Pane layer) { // Shoot method for player
		FireRate--; // Reduces the FireRate counter

		if (spacePressed) {
			if (FireRate < 0) {
				PlayerBullet bullet = new PlayerBullet(layer, phaseAmmoDirectory,
						this.getXpos() + (this.getImageView().getImage().getWidth() / 2), this.getYpos(),
						this.getDamage());
				// Fires the bullet from the center of the player
				bullets.add(bullet); // Adds the bullet to the list for iteration
				this.setFireRate(this.getMaxFireRate());
			} // Resets FireRate after Firing
		}
	}
	
	public Animation getAnimation() {
		return this.animation;
	}

	

}
