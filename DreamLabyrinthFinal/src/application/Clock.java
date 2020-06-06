package application;

import java.util.Random;
import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This class defines the behaviour for the Clock enemy type. It will spawn in a
 * random location and animate through 12 frames. After one cycle of animation
 * is played, the clock will freeze the player for a specified countdown length,
 * then unfreeze the player and restart its animation.
 * 
 * @author Jasmine Roebuck, Quinn Masters
 */
public class Clock extends Sprite implements Universals {

	private Image image = new Image("/Assets/Clock.png");
	private ImageView imageView = new ImageView(image);
	private Random r = new Random();

	private final int COLUMNS = 4; // Columns of the spritesheet
	private final int COUNT = 12; // Frames of the spritesheet
	private final int ROWS = COUNT / COLUMNS; // Rows of the spritesheet
	private final double WIDTH = imageView.getImage().getWidth() / COLUMNS; // Width of each frame
	private final double HEIGHT = imageView.getImage().getHeight() / ROWS; // Height of each frame
	final Animation animation;

	/**
	 * Constructs the Clock object and starts the animation.
	 * 
	 * @param Layer - which layer the clock will be displayed on
	 * @param GameLevel - current game level, used for scaling
	 */
	public Clock(Pane Layer, int GameLevel) {
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth())); // Randomize position
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - imageView.getImage().getHeight()));
		this.setGameLevel(GameLevel);
		Layer.getChildren().add(imageView); // Clock will not appear without this
		this.getImageView().relocate(this.getXpos(), this.getYpos()); // Move the ImageView to the right location
		Update();

		imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT)); // Define the animation viewport
		animation = new SpriteAnimation(imageView, Duration.millis((r.nextDouble() * 10000) + 5000), COUNT, COLUMNS,
				WIDTH, HEIGHT);
		animation.play();
	}

	/**
	 * Sets the chance for upgrade drops based, spawning the upgrade based on given parameters
	 */
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) {
		if (r.nextInt(100) % 37 == 0) {
			Coffee cup = new Coffee(playfieldLayer, this.getXpos(), this.getYpos());
			upgrades.add(cup);
		}
	}

	/**
	 * Updates statistics that scale based on the current game level
	 */
	public void Update() {
		int n = this.getGameLevel();
		setAnimated(true);
		this.setHealth(250 * (0.9 + (n * .1)));
		this.setCountdown(200);
		this.setMaxCountdown(200);
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);
	}

	/**
	 * Clock does not move, this method is only used as a way to manage the freezing
	 * behaviour. Parameters are passed to maintain consistency with other move
	 * methods, but are not used.
	 */
	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) {
		if (animation.getStatus() == Animation.Status.STOPPED) { // Animation has played one cycle
			Player.setTranslateDX(0); // Set player's speed to 0
			if (this.getCountdown() > 0) {
				this.setCountdown(this.getCountdown() - 1);
			}
			if (this.getCountdown() == 0) {
				animation.playFromStart();
				Player.setTranslateDX(10); // Reset player speed
				this.setCountdown(this.getMaxCountdown()); // Reset countdown
			}
		}
	}

	/**
	 * Defines behaviour on death of this enemy object.
	 * 
	 * @param player - the player object
	 * @param labyrinth - which game mode is being played
	 * @param spawnValues - defines spawning statistics
	 */
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) {
		UpgradeChance(this.getLayer(), this.getXpos(), this.getYpos());
		if (labyrinth == true) {
			spawnValues.Update(9);
			spawnValues.Balance(9);
		}
		player.setScore(player.getScore() + this.getScore());
		player.setTranslateDX(player.getDefaultDX()); // Prevents player from being frozen indefinitely if clock dies
														// before it finishes its countdown
	}

	/**
	 * Getter method for the animation object
	 */
	public Animation getAnimation() {
		return this.animation;
	}
}
