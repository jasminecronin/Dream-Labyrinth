package application;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * Similar to the Grades, the "F" grade is not dropped, but fired by some enemies.
 * Rather than upgrading the player, it downgrades the player.
 * @author Quinn
 *
 */

public class GradeF extends Sprite implements Universals {
	private Image Grade = new Image ("/Assets/F Ammo.png");
	private ImageView F = new ImageView(Grade);	
	
	public GradeF (Pane Layer, double expos, double eypos)
	{
		this.setLayer(Layer);
		this.setImageView(F);
		this.getImageView().setScaleX(.5);
		this.getImageView().setScaleY(.5);
		this.getImageView().setPreserveRatio(true);
		this.setTranslateDY(5);
		Layer.getChildren().add(F);
		this.setXpos(expos);
		this.setYpos(eypos);
		this.getImageView().relocate(this.getXpos(), this.getYpos());
	}
	
	public void move(Pane playfieldLayer, Pane bulletLayer, PlayerSprite player) 
	{
		this.setYpos(this.getYpos() + this.getTranslateDY());
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}
	public void upgrade (PlayerSprite player)
	{
		player.setMaxFireRate(player.getMaxFireRate() +1);
		player.setTranslateDX(player.getTranslateDX()*0.90);
		player.setDefaultDX(player.getTranslateDX());
	}

}