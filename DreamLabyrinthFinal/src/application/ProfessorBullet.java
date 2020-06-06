package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * The basic bullet for the professor to shoot at the player.
 * @author Quinn
 */
public class ProfessorBullet extends Sprite implements Universals {
	
	private Image pencil = new Image("/Assets/Pencil Ammo.png");
	private ImageView ammo = new ImageView(pencil);
	
	ProfessorBullet (Pane Layer, double xpos, double ypos)
	{
		this.setLayer(Layer);
		this.setImageView(ammo);
		this.setXpos(xpos);
		this.setYpos(ypos);
		this.getImageView().setScaleX(.45);
		this.getImageView().setScaleY(.45);
		this.getImageView().setPreserveRatio(true);
		this.setTranslateDY(5);
		Layer.getChildren().add(ammo);
		this.getImageView().relocate(this.xpos, this.ypos);		
	}
	public void move(Pane playfieldLayer, Pane bulletLayer, PlayerSprite player) 
	{
		this.setYpos(this.getYpos() + this.getTranslateDY());
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}
}