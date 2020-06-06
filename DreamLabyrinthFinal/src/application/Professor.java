package application;

import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Professor Boss: basic movement with 5 phases based on health.
 * Phase 1: between 90%-100% health: simple horizontal movement with firing at the end of the countdown
 * Phase 2: between 70% - 89% health: simple horizontal movement with a chance to teleport. Fires at a faster rate
 * Phase 3: between 45% - 69% health: simple horizontal movement with a higher chance to teleport, shoot, and fire debuffs. 
 * 			spawns papers at the end of the countdown
 * Phase 4: between 10% - 44% health: simple horizontal movement with an even higher chance to teleport, shoot, fire debuffs
 * 			Spawns more enemies at the end of the countdown.
 * Phase 5: between 0% - 9% health: while on screen, fire at an even faster rate. While off screen, spawn and send flasks towards the player.
 * @author Quinn.
 *
 */
public class Professor extends Sprite implements Universals {
	private Image image = new Image("/Assets/Professor.png");
	private ImageView imageView = new ImageView(image);
	private Random r = new Random();
	private int bossScale = 2500;
	private final int COLUMNS  =  1; // The number of columns in the Sprite Spreadsheet
    private final int COUNT    =  2; // the number of images within the Sprite Spreadsheet
    private final double WIDTH    = imageView.getImage().getWidth(); // the length of the spreadsheet
    private final double HEIGHT   = imageView.getImage().getHeight()/(COUNT/COLUMNS); //the height of a specific view within the spreadsheet
    final Animation animation;

	public Professor(Pane Layer, int gameLevel) {
		// Establishing and setting base attributes
		this.setLayer(Layer);
		this.setImageView(imageView);
		this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth())); // Ensuring it spawns within the
																						// window
		this.setYpos(50);
		this.setTranslateDX(5 * (.9 + ((gameLevel/6) * .1))); // Scaling the Professor down from its specific spawn
																// levels
		this.setTranslateDY(0);
		this.setGameLevel(gameLevel);
		Layer.getChildren().addAll(imageView); // Allocating the new "entity" to a branch/tree. TLDR: it's now a node
		imageView.relocate(xpos, ypos); // produces the image on the screen

		Update();
		
		imageView.setViewport(new Rectangle2D(0, 0, WIDTH, HEIGHT));
	    animation = new SpriteAnimation(
	            imageView,
	            Duration.millis(1000), //sets the duration of animation cycle
	            COUNT, //The number of frames the animation goes through
	            COLUMNS,
	            WIDTH, HEIGHT
	    );
	    animation.setCycleCount(Animation.INDEFINITE); //sets the number of cycles in the animation
	    animation.play(); //begin the animation cycle of the sprite
	}

	public void Update() {
		this.setScore(125 * this.getGameLevel() /10);
		setAnimated(true);
		if (this.getGameLevel() < 8)
		{
			this.setGameLevel(8);
		}
		this.setHealth(bossScale * (.9 + ((this.GameLevel/8) * .1))); // Scaling the professor's health
		this.setMaxHealth(bossScale * (.9 + ((this.GameLevel/8) * .1))); // Scaling the professor's health
		this.setDefaultX(this.getXpos()); // used for calculations when shooting is implemented
		this.setDefaultY(this.getYpos()); // used for calcualtions when shooting is implemented
		this.setCountdown(bossScale / (this.GameLevel/8)); // Scales the countdown for action
		this.setDamage(0);
		this.setMaxCountdown(bossScale / (this.GameLevel/8));
		this.hit.setContrast(-1.0);
		this.hit.setHue(0.05);	
	}

	public void move(Pane playLayer, Pane bulletLayer, PlayerSprite player) // movement method (split into 4 different phases based on health)
	{
		double healthCheck = this.getMaxHealth();
		double current = this.getHealth();
		
		if (current >= 0.9*healthCheck)
		{
			move1(playLayer ,bulletLayer, player);
		}
		else if (current < 0.9*healthCheck && current >= 0.70*healthCheck)
		{
			move2(playLayer ,bulletLayer, player);
		}
		else if (current < 0.7*healthCheck && current >= 0.45*healthCheck)
		{
			move3(playLayer ,bulletLayer, player);
		}
		else if (current< 0.45*healthCheck && current > 0.1*healthCheck)
		{
			move4(playLayer ,bulletLayer, player);
		}
		else
		{
			finalPhase(playLayer ,bulletLayer, player);
		}
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}

	private void move1(Pane playLayer, Pane bulletLayer, PlayerSprite player) //simple movement with shooting only at the end of countdown
	{
		double check = this.getXpos() + this.getTranslateDX();
		this.setCountdown(this.getCountdown()-r.nextInt(2));
		if (check < 0 || check > SCRN_WIDTH - this.getImageView().getImage().getWidth())
		{
			this.setTranslateDX(-this.getTranslateDX());
			check = this.getXpos() + this.getTranslateDX();
		}
		if (this.getCountdown() <= 0)
		{
			this.setCountdown(this.getMaxCountdown());
			firebullet1(bulletLayer);
		}
		
		this.setXpos(check);
	}
	
	public void move2(Pane playLayer, Pane bulletLayer, PlayerSprite player) // adds blinking randomly at random intervals in addition from things in move1
	{
		double check = this.getXpos() + this.getTranslateDX();
		this.setCountdown(this.getCountdown()-r.nextInt(3));
		if (check < 0 || check > SCRN_WIDTH - this.getImageView().getImage().getWidth())
		{
			this.setTranslateDX(-this.getTranslateDX());
			check = this.getXpos() + this.getTranslateDX();
		}
		this.setXpos(check);
		
		if (this.getCountdown()%233 == 0 )
		{
			this.setXpos(r.nextDouble() * (SCRN_WIDTH - this.getImageView().getImage().getWidth()));
			this.setYpos(r.nextDouble() * (SCRN_WIDTH/3));
		}
		
		if (this.getCountdown() <= 0)
		{
			this.setCountdown(this.getMaxCountdown());
			firebullet1(bulletLayer);
		}		
	}
	
	public void move3(Pane playLayer, Pane bulletLayer, PlayerSprite player) // Adds more distinct firing methods (bullets and downgrades)
	{
		double check = this.getXpos() + this.getTranslateDX();
		this.setCountdown(this.getCountdown()-r.nextInt(4));
		if (check < 0 || check > SCRN_WIDTH - this.getImageView().getImage().getWidth())
		{
			this.setTranslateDX(-this.getTranslateDX());
			check = this.getXpos() + this.getTranslateDX();
		}
		this.setXpos(check);
		if (this.getCountdown()%89 == 0)
		{
			firebullet1(bulletLayer);
		}
		if (this.getCountdown()%137 == 0)
		{
			fireDebuff(playLayer);
		}
		if (this.getCountdown()%233 == 0 )
		{
			this.setXpos(r.nextDouble() * (SCRN_WIDTH - this.getImageView().getImage().getWidth()));
			this.setYpos(r.nextDouble() * (SCRN_WIDTH/3));
			firebullet1(bulletLayer);
		}
		
		if (this.getCountdown() <= 0)
		{
			this.setCountdown(this.getMaxCountdown());
			firebullet2(playLayer, player);
		}
	}
	
	public void move4(Pane playLayer, Pane bulletLayer, PlayerSprite player) //professor gets angry and starts to fling enemies
	{
		double check = this.getXpos() + this.getTranslateDX();
		this.setCountdown(this.getCountdown()-r.nextInt(3));
		if (check < 0 || check > SCRN_WIDTH - this.getImageView().getImage().getWidth())
		{
			this.setTranslateDX(-this.getTranslateDX());
			check = this.getXpos() + this.getTranslateDX();
		}
		this.setXpos(check);
		if (this.getCountdown()%89 == 0)
		{
			firebullet1(bulletLayer);
		}
		
		if (this.getCountdown()%137 == 0)
		{
			fireDebuff(playLayer);
		}		
		if (this.getCountdown()%233 == 0 )
		{
			this.setXpos(r.nextDouble() * (SCRN_WIDTH - this.getImageView().getImage().getWidth()));
			this.setYpos(r.nextDouble() * (SCRN_WIDTH/3));
			firebullet2(playLayer, player);
		}
		
		if (this.getCountdown() <= 0)
		{
			this.setCountdown(this.getMaxCountdown());
			Textbook minion1 = new Textbook(playLayer, this.getGameLevel());
			Exam minion2 = new Exam (playLayer, this.getGameLevel());
			Pterodactyl pet = new Pterodactyl(playLayer, this.getGameLevel());
			enemies.add(minion1); enemies.add(minion2); enemies.add(pet);
		}
	}
	
	public void finalPhase(Pane playLayer, Pane bulletLayer, PlayerSprite player) //Professor's final attempts to survive: spends half the time on screen, half the time off screen.
	//methods will be split based on if off-screen or on-screen.
	{
		if (this.getDamage() == 0) //professor is on-screen	
		{
			double check = this.getXpos() + this.getTranslateDX();
			this.setCountdown(this.getCountdown()-r.nextInt(10));
			if (check < 0 || check > SCRN_WIDTH - this.getImageView().getImage().getWidth())
			{
				this.setTranslateDX(-this.getTranslateDX());
				check = this.getXpos() + this.getTranslateDX();
			}
			this.setXpos(check);
			if (this.getCountdown()%137 == 0)
			{
				firebullet2(playLayer, player);
			}
			if (this.getCountdown()%233 == 0 )
			{
				this.setXpos(r.nextDouble() * (SCRN_WIDTH - this.getImageView().getImage().getWidth()));
				this.setYpos(r.nextDouble() * (SCRN_WIDTH/3));
				firebullet2(playLayer, player);
			}
			if (this.getCountdown() <=0)//moves off-screen
			{
				this.setCountdown(this.getMaxCountdown());
				this.setDamage(1);
				this.setXpos(0);
				this.setYpos(-300);
				this.setTranslateDX(0);
			}
		}
		else //professor is off-screen. while offscreen, fires bullets (almost non-stop) and spawns enemies
		{
			this.setCountdown(this.getCountdown()-1);
			if (this.getCountdown()% 89==0)
			{
				firebullet2(playLayer, player);
			}
			if (this.getCountdown()%137 == 0)
			{
				int i = this.getCountdown()/137;
				for (int j = 0; j<i; j++)
				{
					Flask minion = new Flask(playLayer, this.getGameLevel()/6);
					minion.setYpos(-300);
					enemies.add(minion);
				}
			}
			if(this.getCountdown()%233 == 0)
			{
				fireDebuff(playLayer);
			}
			
			if(this.getCountdown()<= 0)
			{
				this.setXpos(r.nextDouble() * (SCRN_WIDTH - imageView.getImage().getWidth())); // Ensuring it spawns within the
				// window
				this.setYpos(50);
				this.setTranslateDX(5 * (.9 + ((this.getGameLevel()/6) * .1))); // Scaling the Professor down from its specific spawn
				this.setDamage(0);
				this.setCountdown(this.getMaxCountdown());
			}
		}
	}
	
	public void firebullet1(Pane Layer) //basic random firing
	{
		ProfessorBullet ammo = new ProfessorBullet(Layer, this.getXpos(), this.getYpos());
		bullets.add(ammo);
	}
	
	public void firebullet2(Pane Layer, PlayerSprite player) //targeted shots
	{
		Paper annoying = new Paper (Layer, this.getXpos(), this.getYpos() , player);
		enemies.add(annoying);
	}
	
	public void fireDebuff (Pane Layer) // fires a negative upgrade
	{
		GradeF boo = new GradeF(Layer, this.getXpos(), this.getYpos());
		upgrades.add(boo);
	}
	
	public void UpgradeChance(Pane playfieldLayer, double xpos, double ypos) 
	{
		if (this.getDamage() == 5)
		{
			Grade boop = new Grade (playfieldLayer, this.getXpos(), this.getYpos(), 5);
			upgrades.add(boop);
		}
	}
	
	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) //Cleanup on death
	{
		if (labyrinth == true)
		{
			this.setDamage(5);
			UpgradeChance(this.getLayer(), this.getXpos(), this.getYpos());
			spawnValues.Update(11);
			spawnValues.Balance(11);
		}
		player.setScore(player.getScore() + this.getScore());
	}
	
	public Animation getAnimation() {
		return this.animation;
	}
	
}
