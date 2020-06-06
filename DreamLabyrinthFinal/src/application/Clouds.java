package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * Class contains the management and generation for the Clouds in the background
 * along with their movement
 * 
 * @author Quinn
 *
 */
public class Clouds extends Sprite implements Universals {
	private Image Cloud1 = new Image("/Assets/Cloud 1.png"); // Creating the references for images and pulling them forward
	private Image Cloud2 = new Image("/Assets/Cloud2.png"); // Creating the references for images and pulling them forward
	private Image Cloud3 = new Image("/Assets/Cloud 3.png"); // Creating the references for images and pulling them forward
	private Image Cloud4 = new Image("/Assets/Cloud4.png"); // Creating the references for images and pulling them forward
	private Image Cloud5 = new Image("/Assets/Cloud5.png"); // Creating the references for images and pulling them forward
	private Image Cloud6 = new Image("/Assets/Cloud6.png"); // Creating the references for images and pulling them forward
	private Image Cloud7 = new Image("/Assets/Cloud7.png"); // Creating the references for images and pulling them forward
	private ImageView cloud1 = new ImageView(Cloud1); // Creating the references for images and pulling them forward
	private ImageView cloud2 = new ImageView(Cloud2); // Creating the references for images and pulling them forward
	private ImageView cloud3 = new ImageView(Cloud3); // Creating the references for images and pulling them forward
	private ImageView cloud4 = new ImageView(Cloud4); // Creating the references for images and pulling them forward
	private ImageView cloud5 = new ImageView(Cloud5); // Creating the references for images and pulling them forward
	private ImageView cloud6 = new ImageView(Cloud6); // Creating the references for images and pulling them forward
	private ImageView cloud7 = new ImageView(Cloud7); // Creating the references for images and pulling them forward
	private List<ImageView> clouds = new ArrayList<>(); // Creating the list
	private Random r = new Random();

	public Clouds(Pane Layer, int i) // Constructor for the clouds
	{
		// Adding all the ImageViews to the list
		clouds.add(cloud1);
		clouds.add(cloud2);
		clouds.add(cloud3);
		clouds.add(cloud4);
		clouds.add(cloud5);
		clouds.add(cloud6);
		clouds.add(cloud7);

		// Fetching a specific ImageView to create
		this.setLayer(Layer); // Setting a default Layer
		this.setImageView(clouds.get(i)); // Applying a specific Imageview
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - clouds.get(i).getImage().getWidth())); // Randomizing Spawn location
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 2.25 - clouds.get(i).getImage().getHeight())); // Randomizing Spawn
																									// Location
		this.setTranslateDX((r.nextDouble() * 3) + 0.01); // Randomizing movement
		this.setTranslateDY(0); // Restricting the vertical Movement
		Layer.getChildren().add(clouds.get(i)); // Adding the Node to a specified Layer
		clouds.get(i).relocate(xpos, ypos); // Moving the image to the specified location
	}

	/**
	 * Movement Method for the clouds: Moves horizontally. upon reaching the end of
	 * the stage, allocate it to a new random x,y Outside the stage to move across
	 * the screen
	 */
	public void move() // movement method for the clouds
	{
		Random r = new Random();
		double check = this.getXpos() + this.getTranslateDX();
		if (check > SCRN_WIDTH) // If the cloud moves out of the window
		{
			this.setXpos(0 - this.getImageView().getImage().getWidth()); // Set the new location to be just outside of
																			// the left end
			this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 2.25 - this.getImageView().getImage().getHeight())); // Randomize
																												// the
			// Y-coordinate for the
			// new location
			this.setTranslateDX((r.nextDouble() * 3) + 0.01); // Randomizing the new speed
		} else {
			this.setXpos(check); // Updating position
		}

		this.getImageView().relocate(this.getXpos(), this.getYpos()); // Updating the Image

	}

}
