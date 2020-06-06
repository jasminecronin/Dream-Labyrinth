package application;

import java.util.List;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * Coffee upgrade that has a simple vertical movement downwards with an "upgrade" action upon collision (giving the player a damage boost).
 * @author Quinn
 *
 */
public class Coffee extends Sprite {
	private Image coffee = new Image ("/Assets/Coffee Upgrade.png");
	private ImageView covfefe = new ImageView(coffee);	
	
	public Coffee (Pane Layer, double expos, double eypos)
	{
		this.setLayer(Layer);
		this.setImageView(covfefe);
		this.getImageView().setScaleX(.35); //Scaling the image down from the default file
		this.getImageView().setScaleY(.35); //Scaling the image down from the default file
		this.getImageView().setPreserveRatio(true); //maintaining the original aspect ratio.
		this.setTranslateDY(5);
		Layer.getChildren().add(covfefe);
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
		player.setDamage(player.getDamage() + 5);		
	}

}
