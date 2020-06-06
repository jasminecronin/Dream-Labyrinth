package application;

import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * The Grade Upgrades that drop from various enemies. Similar to the Coffee, the grades have simple vertical movement that applies
 * a specific upgrade upon collision with the player.
 * A: upgrades all stats and heals
 * B: Upgrades damage and fire rate
 * C: Upgrades damage
 * D: Upgrades movement
 * @author Quinn
 *
 */
public class Grade extends Sprite implements Universals {
	private Image A = new Image ("/Assets/A.png");
	private ImageView GradeA = new ImageView(A);
	private Image B = new Image ("/Assets/B.png");
	private ImageView GradeB = new ImageView(B);
	private Image C = new Image ("/Assets/C.png");
	private ImageView GradeC = new ImageView(C);
	private Image D = new Image ("/Assets/D.png");
	private ImageView GradeD = new ImageView(D);
	private Random r = new Random();	
	
	public Grade (Pane Layer, double expos, double eypos, double grade)
	{
		this.setLayer(Layer);
		if (grade == 5)
		{
			this.setImageView(GradeA);
			this.setGameLevel((int)grade);
		}
		if (grade == 4)
		{
			this.setImageView(GradeB);
			this.setGameLevel((int)grade);
		}
		if (grade == 3)
		{
			this.setImageView(GradeC);
			this.setGameLevel((int)grade);
		}
		if (grade == 2)
		{
			this.setImageView(GradeD);
			this.setGameLevel((int)grade);
		}
		this.setLayer(Layer);
		this.getImageView().setScaleX(.45);
		this.getImageView().setScaleY(.45);
		this.getImageView().setPreserveRatio(true);
		Layer.getChildren().add(this.getImageView());
		this.setTranslateDY(2.5);
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
		int grade = this.getGameLevel();
		if (grade == 5) //Grade of A gives the most upgrades because its an A
		{
			if (player.getHealth() < 10) //increases health
			{
				player.setHealth(player.getHealth() + 1);
			}
			player.setDamage(player.getDamage()+3); //increase the damage by 10%
			player.setTranslateDX(player.getTranslateDX()*1.01); //increase movement speed by 10%
			player.setMaxFireRate(player.getMaxFireRate()-1);
			if (player.getMaxFireRate() <=5) //FireRate cap
			{
				player.setMaxFireRate(5);
			}

		}
		if (grade == 4)
		{
			player.setDamage(player.getDamage()*1.05);
			player.setMaxFireRate(player.getMaxFireRate()-1);
			if (player.getMaxFireRate() <=5) //FireRate cap
			{
				player.setMaxFireRate(5);
			}
		}
		if (grade == 3)
		{
			player.setDamage(player.getDamage()*1.03);
		}
		if (grade == 2)
		{
			player.setTranslateDX(player.getTranslateDX()*1.01);
		}
	}

}
