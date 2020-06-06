package application;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * This class was adapted from
 * https://netopyr.com/2012/03/09/creating-a-sprite-animation-with-javafx/
 */
public class SpriteAnimation extends Transition {

	private final ImageView imageView;
	private final int count; // The number of "frames" for the animation cycle
	private final int columns; // The number of columns in the spritesheet
	private final double width; // The width of each frame in pixels
	private final double height;// The height of each frame in pixels
	private int lastIndex;

	/**
	 * Takes in parameters and constructs an animation object that will define how
	 * the object's spritesheet is interpreted.
	 * 
	 * @param imageView - the ImageView that will contain the image
	 * @param duration - total duration of the animation in milliseconds
	 * @param count - number of frames in the spritesheet
	 * @param columns - number of columns in the spritesheet
	 * @param wIDTH2 - width of each frame in the spritesheet in pixels
	 * @param hEIGHT2 - height of each frame in the spritesheet in pixels
	 */
	public SpriteAnimation(ImageView imageView, Duration duration, int count, int columns, double wIDTH2,
			double hEIGHT2) {
		this.imageView = imageView;
		this.count = count;
		this.columns = columns;
		this.width = wIDTH2;
		this.height = hEIGHT2;
		setCycleDuration(duration);
		setInterpolator(Interpolator.LINEAR); // Each frame will be played for the same amount of time
	}

	/**
	 * Called when this.play() is invoked. Never called directly. Defines how the
	 * animation is to read the spritesheet.
	 */
	protected void interpolate(double k) {
		final int index = Math.min((int) Math.floor(k * count), count - 1); // Indicates the frame number
		if (index != lastIndex) {
			final double x = (index % columns) * width;
			final double y = (index / columns) * height;
			imageView.setViewport(new Rectangle2D(x, y, width, height)); // Reposition the Viewport to look at the next
																			// frame
			lastIndex = index;
		}
	}
}