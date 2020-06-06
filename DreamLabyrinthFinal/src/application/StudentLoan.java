package application;

import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
/**
 * Grows in size until it reaches its original size. The it duplicates. There is a soft lock
 * to prevent the overflowing of student loans
 * @author Quinn
 *
 */
public class StudentLoan extends Sprite implements Universals {

	private Image student = new Image("/Assets/Student Loan.png");
	private ImageView loans = new ImageView(student);
	private Random r = new Random();
	
	public StudentLoan(Pane Layer, int GameLevel)
	{
		this.setLayer(Layer);
		this.setImageView(loans);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - loans.getImage().getWidth()));
		this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - loans.getImage().getHeight()));
		this.setTranslateDX(0); //random value between 1-6 
		this.setTranslateDY(0); //random value betweeen 1-6
		this.setGameLevel(GameLevel);
		Layer.getChildren().add(loans);
		loans.relocate(xpos, ypos);
		Update();		
	}
	
	public void Update()
	{
		this.getImageView().setFitWidth(0.25* this.getImageView().getImage().getWidth());
		this.getImageView().setFitHeight(0.25 * this.getImageView().getImage().getHeight());
		this.getImageView().setPreserveRatio(true);
		int n = this.getGameLevel();
		this.setScore(6 + n/10);
		this.setDefaultX(this.getXpos()); // used for calculations
		this.setDefaultY(this.getYpos()); // used for calculations within movement
		this.setHealth(150 * (0.9 + (n * .1))); // Health scales based on game level
		this.setDamage(0); // no damage whatsoever. just clutters up the board and takes projectiles.
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	
	}	
	
	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite Player) 
	{
		double currentArea = this.getImageView().getFitWidth()*this.getImageView().getFitHeight();
		double checkArea = (this.getImageView().getImage().getHeight() * this.getImageView().getImage().getWidth());
		if (currentArea < checkArea)
		{
			double scaleX = this.getImageView().getFitWidth() * 1.0025;
			double scaleY = this.getImageView().getFitHeight() * 1.0025;
			this.getImageView().setFitWidth(scaleX);
			this.getImageView().setFitHeight(scaleY);			
		}
		else
		{
			if (enemies.size() <=10 ) //soft-lock to prevent the flooding of student loans forever preventing the player from progressing
			{
			this.getImageView().setFitWidth(0.25* this.getImageView().getImage().getWidth());
			this.getImageView().setFitHeight(0.25 * this.getImageView().getImage().getHeight());
			StudentLoan offspring = new StudentLoan (playLayer, this.getGameLevel());
			enemies.add(offspring);
			}
			else
			{
				this.setXpos(r.nextDouble() * (SCRN_WIDTH - loans.getImage().getWidth()));
				this.setYpos(r.nextDouble() * (SCRN_HEIGHT / 1.3 - loans.getImage().getHeight()));
				this.getImageView().setFitWidth(0.25* this.getImageView().getImage().getWidth());
				this.getImageView().setFitHeight(0.25 * this.getImageView().getImage().getHeight());
				this.getImageView().relocate(this.getXpos(), this.getYpos());
			}
		}
	}
	
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) 
	{
		if(r.nextInt(100)%19 == 0) //10% to trigger a drop chance
		{
			this.setDamage(r.nextInt(6)); //66% chance to drop a grade
			if (this.getDamage() == 5)
			{
				Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 5);
				upgrades.add(boop);
			}
			else if (this.getDamage() == 4)
			{
				Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 4);
				upgrades.add(boop);
			}
			else if (this.getDamage() == 3)
			{
				Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 3);
				upgrades.add(boop);
			}
			else if (this.getDamage()== 2)
			{
				Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 2);
				upgrades.add(boop);
			}
		}
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup
	{
		UpgradeChance(this.getLayer(), this.getXpos(), this.getYpos());
		if (labyrinth == true)
		{
			spawnValues.Update(4);
			spawnValues.Balance(4);
		}
		player.setScore(player.getScore() + this.getScore());
	}
}
