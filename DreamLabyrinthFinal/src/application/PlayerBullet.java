package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * Generation of the playerbullet, changing the bullet type based on the level of the game.
 * @author Quinn
 *
 */
public class PlayerBullet extends Sprite {
	private Image bulletImage;
	private ImageView bulletImageView;
	private double damage;

	PlayerBullet(Pane Layer, String image, double PlayerX, double PlayerY, double damage)
	// Constructor for the bullet taking in the player's position and firing from
	// the center of the player
	{
		bulletImage = new Image(image);
		bulletImageView = new ImageView(bulletImage);
		this.setLayer(Layer);
		this.setImageView(bulletImageView);
		this.setXpos(PlayerX - bulletImage.getWidth() / 2);
		this.setYpos(PlayerY);
		this.setTranslateDX(0);
		this.setTranslateDY(-5);
		this.damage = damage;
		Layer.getChildren().add(bulletImageView);
		bulletImageView.relocate(xpos, ypos);
	}

	public void Update() // To be migrated to main for applications of Upgrades
	{
		this.damage += 5;
	}

	public void setDamage(double d) {
		this.damage = d;
	}

	public double getDamage() {
		return this.damage;
	}

}
