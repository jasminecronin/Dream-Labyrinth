package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
/**
 * Handles the level generation of the game. It is split into three parts: 
 * 1) Methods - contains all methods releveant to all level production
 * 2) Story Mode - containing all methods relevant ONLY to story mode
 * 3) Labyrinth mode - containing all methods relevant ONLY to labyrinth mode
 * @author Admin
 *
 */
public class Level implements Universals {

	Pane currentLevel;
	Random randomizer = new Random();
	EnemyFormation trials;
	private int level = 1;
	public boolean gamestate = false; // Checks to see if the player has completed the level

/**
 * Methods
 */
	
	public boolean getGameState() {
		return gamestate;
	}

	public int getLevel() {
		return level;
	}
	
	public void resetGame() {
		enemyBullets.clear();
		upgrades.clear();
		bullets.clear();
		enemies.clear();
		this.level = 1;
	}
/**
 * Method that detects all collision within the game.
 * @param playfieldLayer the layer where all enemies/upgrades are
 * @param bulletLayer the layer where all bullets are
 * @param UILayer the layer where all relevant information for the game are
 * @param player the PlayerSprite is passed through
 * @param labyrinth Check to see if the Labyrinth mode is active
 * @param spawnValues the spawn rates for the labyrinth mode
 * @param highscore the score for the game regardless of game mode
 */
	public void detectCollision(Pane playfieldLayer, Pane bulletLayer, Pane UILayer, PlayerSprite player, boolean labyrinth, SpawnRate spawnValues, HighScore highscore) { // Collision
																											// check and
																											//deletion
		//"Invincibility" frames. Cannot get hit more than once per countdown, giving the player time to react.
		if (player.getCountdown() > 0) {
			player.setCountdown(player.getCountdown() - 1); 
		}
		//Checks to see if the player and enemies are colliding.
		for (Sprite enemy : enemies) {
			if (player.isColliding(enemy)) {
				if (player.getCountdown() == 0) {
					player.setHealth(player.getHealth() - 1);
					player.setCountdown(player.getMaxCountdown());
					for (Sprite heart : hearts) {
						UILayer.getChildren().remove(heart.getImageView());
					}
					player.displayHealth(UILayer);
				}
			}
		}
		// checks to see if the player bullets are colliding with any enemies
		for (Sprite bullet : bullets) {
			for (Sprite enemy : enemies) {
				if (bullet.isColliding(enemy)) {
					bullet.setAlive(false);
					enemy.hit();
					enemy.setHealth(enemy.getHealth() - bullet.getDamage());
					bulletLayer.getChildren().remove(bullet.getImageView());
					if (enemy.getHealth() <= 0) {
						enemy.dies(player, labyrinth, spawnValues);
						playfieldLayer.getChildren().remove(enemy.getImageView());
						enemy.setAlive(false);
						highscore.displayScore(UILayer, player);
					}
				}
			}
		}
		// checks to see if the enemy bullets are colliding with the player
		for (Sprite enemy : enemyBullets) {
			if (player.isColliding(enemy)) {
				enemy.setAlive(false);
				bulletLayer.getChildren().remove(enemy.getImageView());
				if (player.getCountdown() == 0) {
					player.setHealth(player.getHealth() - 1);
					player.setCountdown(player.getMaxCountdown());
					for (Sprite heart : hearts) {
						UILayer.getChildren().remove(heart.getImageView());
					}
					player.displayHealth(UILayer);
				}
			}
		}
		// checks to see if the upgrades are colliding with the player
		for (Sprite upgrade : upgrades) {
			if (upgrade.isColliding(player)) {
				upgrade.setAlive(false);
				playfieldLayer.getChildren().remove(upgrade.getImageView());
				upgrade.upgrade(player);
				player.displayHealth(UILayer);
			}
			if (upgrade.getYpos() > SCRN_HEIGHT) {
				upgrade.setAlive(false);
				playfieldLayer.getChildren().remove(upgrade.getImageView());
			}
		}
		//removing items from the list if they are set to dead
		bullets.removeIf(Sprite::isDead);
		enemyBullets.removeIf(Sprite::isDead);
		enemies.removeIf(Sprite::isDead);
		upgrades.removeIf(Sprite::isDead);

	}
	//used to animate the "hit" animation (the flicker when the enemy/player gets hit)
	public void resetImages(Pane playfieldLayer, Pane bulletLayer, PlayerSprite player) {
		for (Sprite enemy : enemies) {
			enemy.getImageView().setEffect(null);
		}
		player.getImageView().setEffect(null);

	}
	// Updates GameState based on the non-player objects on the screen
	public void gameState() { 
		if (enemies.size() == 0 && upgrades.size() == 0) {
			this.gamestate = true;
		} else {
			this.gamestate = false;
		}
	}
	
/**
 *  Story Mode
 */
	
	//level update
	public void nextLevel() {
		if (level < 8)
			level++;
	}
	
	//story generation by level
	public void StoryGeneration(Pane playfieldLayer) {
		this.currentLevel = playfieldLayer;
		switch (level) {
		case 1:
			trials = new EnemyFormation("donut", currentLevel, level);
			trials.radialFormation(SCRN_WIDTH/2, SCRN_HEIGHT/2, 150, 0, 360, 36);
			trials.radialFormation(100, 100, 50, 0, 360, 72);
			trials.radialFormation(1050, 100, 50, 0, 360, 72);
			break;
		case 2:
			trials = new EnemyFormation("donut", currentLevel, level);
			trials.radialFormation(SCRN_WIDTH/2, SCRN_HEIGHT/2, 300, 0, 360, 36);
			trials = new EnemyFormation("ice", currentLevel, level);
			trials.diagonalFormation(100, 100, 10, 10, false);
			break;
		case 3:
			trials = new EnemyFormation("melon", currentLevel, level);
			trials.unformalFormation(400, 100, 200, 180, 90, 90);
			break;
		case 4:
			Cake boss1 = new Cake(currentLevel, level);
			enemies.add(boss1);
			boss1.getImageView().relocate(SCRN_WIDTH/2 - boss1.getImageView().getImage().getWidth()/2, 0);
			trials = new EnemyFormation("donut", currentLevel, level);
			trials.radialFormation(100, 100, 50, 0, 360, 36);
			trials.radialFormation(1050, 100, 50, 0, 360, 36);
			trials = new EnemyFormation("melon", currentLevel, level);
			trials.unformalFormation(400, 100, 200, 180, 90, 90);
			break;			
		case 5:
			for (int i = 0; i < 4; i ++)
			{
				Flask chem = new Flask(currentLevel, level);
				chem.getImageView().relocate(randomizer.nextInt(SCRN_WIDTH), -100);
				enemies.add(chem);
			}
			break;	
		case 6:
			for (int i = 0; i < 5; i++)
			{
				StudentLoan debt = new StudentLoan(currentLevel, level);
				enemies.add(debt);
			}
			for (int i = 0; i < 5 ; i ++)
			{
				Exam test = new Exam(currentLevel, 2*level);
				enemies.add(test);
			}
			break;	
		case 7:
			for (int i = 0; i < 9; i ++)
			{
				Textbook reading= new Textbook (currentLevel, 2*level);
				enemies.add(reading);
			}
			Clock freeze = new Clock(currentLevel, level);
			enemies.add(freeze);
			break;	
		case 8:
			Professor boss2 = new Professor(currentLevel, level);
			enemies.add(boss2);
			for (int i = 0; i < 2; i++)
			{
				Clock frozen = new Clock(currentLevel, level);
				enemies.add(frozen);
			}
			for (int i = 0; i < 5; i++)
			{
				Exam tests = new Exam(currentLevel, 2*level);
				enemies.add(tests);
			}
			break;	
		}
	}
	
	
	/**
	 * Labyrinth Mode Methods/Functions
	 */
	
	//Pseudo-Random generation of enemies based on spawn rate
/**
 * Pseudo-Random generation of enemies that is based on spawn rate. If the random number is below 
 * the initial spawn rate, default to the position of the total values. Otherwise increase the index, increase
 * the total, and check again.
 * @param playfieldLayer Spawning location of the enemies
 * @param spawnValues The spawn rates for each enemy type
 */
	public void LabyrinthGeneration(Pane playfieldLayer, SpawnRate spawnValues) {
		this.currentLevel = playfieldLayer;		
		int enemies = 5 + this.level;
		for (int i = 0; i < enemies; i ++)
		{
			if (i >=15) //caps the enemy count to 15 to prevent lagging and frame rate issues
			{
				break;
			}
			double spawn = randomizer.nextDouble()*100; //Selects a random value between 
			boolean compare = false; // base case
			double threshold= spawnValues.get(0); //base spawn rate
			int enemyType = 0; // to signify the enemy type that is spawned
			while (compare == false)
			{
				//handles any errors should the random chance not be within range or the index is greater than the number of enemies.
				if (enemyType>=12) 
				{
					//reseting all values
					enemyType = 0;
					threshold = 0;
					spawn = randomizer.nextDouble()*100;
				}
				//The value is within the spawn rate, thus end the loop.
				if (spawn < threshold)
				{
					Spawn(enemyType);
					compare = true; 
				}
				//increase the threshold and the enemytype
				else
				{
					enemyType++;
					threshold += spawnValues.get(enemyType);
				}
			}
		}
	}
	
	public void Spawn(int i) //Spawn the respective enemy based on the spawn chance from the above method.
	{
		if(i == 0)
		{
			Donut donut = new Donut(currentLevel, 1 + (level/10));
			enemies.add(donut);
		}
		else if (i ==1)
		{

			IceCream icecream = new IceCream(currentLevel, 1 + (level/10));
			enemies.add(icecream);
		}
		else if (i == 2)
		{
			CottonCandy cloud= new CottonCandy(currentLevel, 1 + (level/10));
			enemies.add(cloud);
		}
		else if (i ==3)
		{
			Exam exam = new Exam(currentLevel, 1 + (level/10));
			enemies.add(exam);
		}
		else if (i ==4)
		{
			StudentLoan bleh = new StudentLoan(currentLevel, 1 + (level/10));
			enemies.add(bleh);
		}
		else if (i ==5)
		{
			Flask flask= new Flask(currentLevel, 1 + (level/10));
			enemies.add(flask);
		}
		else if (i ==6)
		{
			Textbook costly= new Textbook(currentLevel, 1 + (level/10));
			enemies.add(costly);
		}
		else if (i ==7)
		{
			Watermelon nummy = new Watermelon(currentLevel, 1 + (level/10));
			enemies.add(nummy);
		}
		else if (i ==8)
		{
			Pterodactyl pete = new Pterodactyl(currentLevel, 1 + (level/10));
			enemies.add(pete);
		}
		else if (i ==9)
		{
			Clock time = new Clock(currentLevel, 1 + (level/10));
			enemies.add(time);
		}
		else if (i ==10)
		{
			Cake cake= new Cake(currentLevel, 1 + (level/10));
			enemies.add(cake);
		}
		else if (i ==11)
		{
			Professor prof = new Professor(currentLevel, 1 + (level/10));
			enemies.add(prof);
		}

	}

	// Scaling and increasing difficulty while minimizing the chance of the player becoming "too strong"
	public void nextLabLevel(SpawnRate values) {
		this.level++;	
		if (this.level == 10)
		{
			values.PhaseOne();
		}
		if (this.level == 15)
		{
			values.PhaseTwo();
		}
		if (this.level == 20)
		{
			values.PhaseThree();
		}
		if (this.level == 30)
		{
			values.PhaseFour();
		}
		if (this.level == 50)
		{
			values.PhaseFive();
		}
	}
	

}
