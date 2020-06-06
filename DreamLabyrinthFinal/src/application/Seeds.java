package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * The basic bullet for Watermelons to fire at the enemy.
 * @author Admin
 *
 */
public class Seeds extends Sprite {
	private Image Seeds = new Image("/Assets/Seeds.png");
	private ImageView imageView = new ImageView(Seeds);
	
	public Seeds(Pane Layer, int gameLevel, double expos, double eypos, double pxpos, double pypos)
	{
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.setGameLevel(gameLevel);
		this.setXpos(expos);
		this.setYpos(eypos);
		this.getImageView().setScaleX(.5);
		this.getImageView().setScaleY(.5);
		Layer.getChildren().add(imageView);
		this.getImageView().relocate(this.xpos, this.ypos);
		Update(pxpos, pypos);
	}
	
	public void move()
	{
		this.setTranslateDX(this.getTranslateDX() * 1.005);
		this.setTranslateDY(this.getTranslateDY() * 1.005);
		this.setXpos(this.getXpos() + this.getTranslateDX());
		this.setYpos(this.getYpos() + this.getTranslateDY());
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}

	//Targets the player's location and rotates the image to reflect the direction of travel.
	public void Update(double f, double g)
	{
		int n = this.getGameLevel();
		double direction = this.getXpos() - f;
		double descent = g - this.getYpos();
		double rotation = Math.atan(direction/descent) * (180 / Math.PI);
		this.setTranslateDX(-direction/500);
		this.setTranslateDY(descent/500);
		this.getImageView().setRotate(rotation);
	}

}
