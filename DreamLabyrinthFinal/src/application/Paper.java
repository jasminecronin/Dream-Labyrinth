package application;

import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * The "bullet" for the textbook. The paper, when released, slowly drifts towards the player.  
 * @author Quinn
 */
public class Paper extends Sprite implements Universals {
	private Image paper = new Image("/Assets/Paper.png");
	private ImageView PAPER = new ImageView(paper);
	private Random r = new Random();	
	
	public Paper (Pane Layer, double xpos, double ypos, PlayerSprite Player)
	{
		this.setLayer(Layer);
		this.setImageView(PAPER);
		this.setXpos(xpos);
		this.setYpos(ypos);
		this.getImageView().setScaleX(0.5);
		this.getImageView().setScaleY(0.5);
		Layer.getChildren().add(PAPER);
		this.getImageView().relocate(this.getXpos(), this.getYpos());
		Update(Player);		
	}
	
	public void move(Pane playfieldLayer, Pane bulletLayer, PlayerSprite player)
	{
		this.setXpos(this.getXpos() + this.getTranslateDX());
		this.setYpos(this.getYpos() + this.getTranslateDY());
		if (this.getXpos() > SCRN_WIDTH || this.getXpos() < 0)//self-deletes when the object is beyond the window's boundaries
		{
			this.setAlive(false);
		}
		if (this.getYpos() > SCRN_HEIGHT) //self-deletes when the object is beyond the window's boundaries
		{
			this.setAlive(false);
		}
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}
	//using the player's position when the object is created to determine travel path and speed.
	public void Update(PlayerSprite Player)
	{
		this.setHealth(100);
		double direction = this.getXpos() - Player.getXpos();
		double descent = Player.getYpos() - this.getYpos();
		this.setTranslateDX(-direction/500);
		this.setTranslateDY(descent/500);
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	
	}

}
