package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Layers implements Universals {

	public Pane backgroundLayer; // Allocating the static background layer
	public Pane animationLayer; // Allocating the Animated background Layer
	public Pane playfieldLayer; // Allocating the playfield layer (layer in which most of the sprites are generated)
	public Pane bulletLayer; // Allocating the bullet Layer (layer in which bullets of both enemy and players are created)
	public Pane UILayer; // Allocating the UI layer for displaying player health and score while playing
	public Pane debugLayer; // Allocating a layer for debugging
	
	
	// Background art
	public Image background;
	public ImageView backgroundImage;

	
	// Current phase
	public String phaseOne;
	
	
	// Layers constructor
	public Layers(String imageDirectory) // Creating the background to fit the window
	{
		// Takes in a string directory to get the background art and display an image
		phaseOne = imageDirectory;
		background = new Image(imageDirectory);
		backgroundImage = new ImageView(background);
		backgroundImage.setFitHeight(SCRN_HEIGHT);
		backgroundImage.setFitWidth(SCRN_WIDTH);
		backgroundImage.setPreserveRatio(true); // maintain the ratio of the image when "stretching" the image
	}

	
	// Some getters and setters
	public ImageView getImageView() {
		return this.backgroundImage;
	}

	public void setImageView(ImageView imageView) {
		this.backgroundImage = imageView;
	}

	public Pane getBulletLayer() {
		return this.bulletLayer;
	}

	public Pane getPlayfieldLayer() {
		return this.playfieldLayer;
	}

	public Pane getUILayer() {
		return this.UILayer;
	}

	public Pane getAnimationLayer() {
		return this.animationLayer;
	}
	

	// Art generation and animation methods
	public void createLayers() // Creating the Layers
	{
		this.backgroundLayer = new Pane();
		this.animationLayer = new Pane();
		this.playfieldLayer = new Pane();
		this.bulletLayer = new Pane();
		this.UILayer = new Pane();
		this.debugLayer = new Pane();
		backgroundLayer.getChildren().add(backgroundImage); // Adding Background to the proper Layer
		if (phaseOne.equals("/Assets/Tutorial Background No Clouds.png")) {
			for (int i = 0; i < 7; i++) // creates an object for each cloud image in the asset
			{
				Clouds cloud = new Clouds(animationLayer, i);
				clouds.add(cloud);
			}
		}
	}

	public Pane generate(Pane root) // All panes generator
	{
		root = new Pane();
		root.setPrefSize(SCRN_WIDTH, SCRN_HEIGHT);
		createLayers();
		/**
		 * Adding all the Layers as children in the appropriate order so the art stacks
		 * properly
		 */
		root.getChildren().add(backgroundLayer);
		root.getChildren().add(animationLayer);
		root.getChildren().add(playfieldLayer);
		root.getChildren().add(bulletLayer);
		root.getChildren().add(UILayer);
		root.getChildren().add(debugLayer);

		return root;
	}

	// Iterator methods
	public void bulletIterator() // Updates the Values and Images of the Bullets
	{
		if (bullets.size() != 0) {
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).move();
			}
		}

		if (enemyBullets.size() != 0) {
			for (int i = 0; i < enemyBullets.size(); i++) {
				enemyBullets.get(i).move();
			}
		}
	}

	public void bulletBounds() { // Checks for bullets out of screen
		for (Sprite bullet : bullets) {
			if (bullet.getYpos() < 0) {
				bullet.setAlive(false);
				bulletLayer.getChildren().remove(bullet.getImageView());
			}

		}
		for (Sprite bullet : enemyBullets) {
			if (bullet.getYpos() > SCRN_HEIGHT) {
				bullet.setAlive(false);
				bulletLayer.getChildren().remove(bullet.getImageView());
			}
		}
	}

	public void NPCIterator(PlayerSprite Player) // updates the values of the enemies (Back-end)
	{
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).move(playfieldLayer, bulletLayer, Player);
		}

		if (upgrades.size() != 0) {
			for (Sprite coffee : upgrades) {
				coffee.move(playfieldLayer, bulletLayer, Player);
			}
		}
	}

	public void animationIterator() { // Animates clouds in first phase
		for (Sprite cloud : clouds) {
			cloud.move();
		}
	}
}
