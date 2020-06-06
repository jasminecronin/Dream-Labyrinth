package application;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Welcome To the Purple Labyrinth final demo. As a group, we all agreed upon
 * making the Main class to be only in charge of making transitions between the scenes.
 * The Main class displays the different menus with multiple buttons, contains the gameloop
 * and updates scenes according to what level it is, what mode, whether the game is paused
 * and when the game is over.
 * 
 * @author Alec, Quinn
 *
 */

public class Main extends Application implements Universals {

	// The stage
		Stage window;
		
		// The root that takes everything
		public Pane root;
		
		// Panes for displaying different menus
		public Pane pauseMenu;
		public Pane gameOverMenu;
		public Pane menuBackgroundLayer;
		public Pane topTenDisplay;
		private Pane topTen;
		
		
		// Game art for menu and game over
		private Image titleScreen = new Image("/Assets/Title_Screen.png");
		private Image titleScreenText = new Image("/Assets/Title_Screen_Text.png");
		private Image deathSequence = new Image("/Assets/GameOverSequence.png");
		private ImageView titleScreenView = new ImageView(titleScreen);
		private ImageView titleScreenTextView = new ImageView(titleScreenText);
		private ImageView deathSequenceView = new ImageView(deathSequence);
		
		
		//Buttons
		private Button storyModeButton;
		private Button labyrinthModeButton;
		private Button quitButton;
		private Button resumeButton;
		private Button menuButton;
		private Button playAgainButton;
		private Button enterNameButton;
		private Button clearButton;
		
		
		// Game objects 
		private Layers phaseOneLayering = new Layers("/Assets/Tutorial Background No Clouds.png"); // takes care of animation
		private Level level = new Level(); // takes care of what spawns where
		private HighScore highscore = new HighScore(); // For storing and reading scores off a file
		public SpawnRate SpawnValues; // SpawnRate for LabyrinthMode
		private Text showscore = new Text();
		private TextField nameField;
		
		
		// Game modes
		public boolean story = false;
		public boolean labyrinth = false;
		
		
		// Other
		public AnimationTimer GameLoop; // Animation timer for general Animation, also known as GameLoop.
		public PlayerSprite player; // Creating player

	// Menus generation method
	public Parent createMenu() {
		// Main menu that displays art, prompts user for name and can display scores.

		root = new Pane();
		menuBackgroundLayer = new Pane();
		
		root.setPrefSize(this.SCRN_WIDTH, this.SCRN_HEIGHT);
		menuBackgroundLayer.setPrefSize(this.SCRN_WIDTH, this.SCRN_HEIGHT);
		this.story = false;
		this.labyrinth = false;
		

		createButtons();

		titleScreenTextView.relocate(SCRN_WIDTH / 4, SCRN_HEIGHT / 6);
		titleScreenTextView.setScaleY(SCRN_HEIGHT / 300);
		titleScreenTextView.setScaleX(SCRN_WIDTH / 400);
		titleScreenView.setFitWidth(SCRN_WIDTH);
		titleScreenView.setFitHeight(SCRN_HEIGHT);
		titleScreenView.setPreserveRatio(true);

		nameField = new TextField();
		nameField.setPrefSize(200, 10);
		nameField.setText("Enter your name");
		nameField.relocate(SCRN_WIDTH / 4, SCRN_HEIGHT / 2);
	
//		showscore.setText("Highscore  " + highscore.getHighscore());
//		showscore.setScaleX(1);
//		showscore.setScaleY(1);
//		showscore.setPrefSize(300, 1000);
//		showscore.relocate(SCRN_WIDTH/4, SCRN_HEIGHT / 2.25);
		
		showscore = new Text(SCRN_WIDTH / 4, SCRN_HEIGHT / 2.25, "Highscore " + highscore.getHighscore());
		showscore.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
		
		Button topTen = new Button();
		topTen.setText("Top 10");
		topTen.setPrefSize(200, 100);
		topTen.relocate(500, 550);
		topTen.setOnMousePressed(e -> topTenButtonHandler());
		
		storyModeButton.relocate(SCRN_WIDTH / 1.5, SCRN_HEIGHT / 9);
		labyrinthModeButton.relocate(SCRN_WIDTH / 1.5, SCRN_HEIGHT / 3.6);
		topTen.relocate(SCRN_WIDTH / 1.5, SCRN_HEIGHT / 2.25);
		quitButton.relocate(SCRN_WIDTH / 1.5, SCRN_HEIGHT / 1.64);
		clearButton.relocate(SCRN_WIDTH / 3.43, SCRN_HEIGHT / 1.64);
		enterNameButton.relocate(SCRN_WIDTH / 3.43, SCRN_HEIGHT / 1.8);

		menuBackgroundLayer.getChildren().addAll(titleScreenView, titleScreenTextView);
		root.getChildren().add(menuBackgroundLayer);
		root.getChildren().addAll(showscore, nameField);
		root.getChildren().addAll(storyModeButton, quitButton, labyrinthModeButton, clearButton, enterNameButton, topTen);

		return root;
	}

public void createPauseMenu() {
		
		// function adds buttons it needs and relocates
		// them to where it needs to for this particular menu
		
		pauseMenu = new Pane();

		createButtons();

		resumeButton.relocate(SCRN_WIDTH / 2.4 , SCRN_HEIGHT / 3.6);
		menuButton.relocate(SCRN_WIDTH / 2.4, SCRN_HEIGHT / 2.25);
		quitButton.relocate(SCRN_WIDTH / 2.4, SCRN_HEIGHT / 1.64);

		pauseMenu.getChildren().add(menuButton);
		pauseMenu.getChildren().add(quitButton);
		pauseMenu.getChildren().add(resumeButton);

		root.getChildren().add(pauseMenu);
	}

public void createGameOverMenu() {
	
	// function adds buttons it needs and relocates
	// them to where it needs to for this particular menu
	
	gameOverMenu = new Pane();

	createButtons();

	playAgainButton.relocate(SCRN_WIDTH / 2.4 , SCRN_HEIGHT / 3.6);
	menuButton.relocate(SCRN_WIDTH / 2.4, SCRN_HEIGHT / 2.25);
	quitButton.relocate(SCRN_WIDTH / 2.4, SCRN_HEIGHT / 1.64);

	gameOverMenu.getChildren().addAll(quitButton, menuButton, playAgainButton);
	root.getChildren().add(gameOverMenu);
}	

	// Buttons creation
		public void createButtons() {
			playAgainButton = new Button();
			playAgainButton.setText("Play again?");
			playAgainButton.setPrefSize(200, 100);
			playAgainButton.setOnMousePressed(e -> playAgainButtonHandler());

			labyrinthModeButton = new Button();
			labyrinthModeButton.setText("Labyrinth Mode");
			labyrinthModeButton.setPrefSize(200, 100);
			labyrinthModeButton.setOnMousePressed(e -> labyrinthButtonHandler());

			storyModeButton = new Button();
			storyModeButton.setText("Story Mode");
			storyModeButton.setPrefSize(200, 100);
			storyModeButton.setOnMousePressed(e -> storyModeButtonHandler());

			quitButton = new Button();
			quitButton.setText("Quit");
			quitButton.setPrefSize(200, 100);
			quitButton.setOnMousePressed(e -> quitButtonHandler());

			menuButton = new Button();
			menuButton.setText("Main Menu");
			menuButton.setPrefSize(200, 100);
			menuButton.setOnMousePressed(e -> menuButtonHandler());

			resumeButton = new Button();
			resumeButton.setText("Resume");
			resumeButton.setPrefSize(200, 100);
			resumeButton.setOnMousePressed(e -> resumeButtonHandler());
			
			clearButton = new Button();
			clearButton.setText("Clear");
			clearButton.setPrefSize(100, 10);
			clearButton.setOnMousePressed(e -> highscore.clearHighscore(showscore, "highscore.txt"));
			
			enterNameButton = new Button();
			enterNameButton.setText("Enter");
			enterNameButton.setPrefSize(100, 10);
			enterNameButton.setOnMousePressed(e -> nameButtonHandler());
		}
		
		// Button handlers
		private void topTenButtonHandler() {
			
			
			// Pane layers that will display top ten
			topTenDisplay = new Pane();
			menuBackgroundLayer = new Pane();
			
			menuBackgroundLayer.setPrefSize(this.SCRN_WIDTH, this.SCRN_HEIGHT);
			topTenDisplay.setPrefSize(SCRN_WIDTH, SCRN_HEIGHT);
			
			// Adding the same background as the menu
			titleScreenView.setFitWidth(SCRN_WIDTH);
			titleScreenView.setFitHeight(SCRN_HEIGHT);
			titleScreenView.setPreserveRatio(true);
			
			menuBackgroundLayer.getChildren().add(titleScreenView);
			topTenDisplay.getChildren().add(menuBackgroundLayer);
			
			createButtons();
			
			menuButton.relocate(525, 500);
			
			topTenDisplay.getChildren().add(menuButton);
			highscore.displayTopTen(topTenDisplay);
				
			window.setScene(new Scene(topTenDisplay));
			window.show();
		}
		
		private void labyrinthButtonHandler() {
			this.labyrinth = true;
			SpawnValues = new SpawnRate();
			window.setScene(new Scene(StartingLevel())); // Generates new Scene
			window.getScene().setOnKeyPressed(initialKeyPress -> player.playerKeyPressedHandler(initialKeyPress));
			window.getScene().setOnKeyReleased(initialKeyRelease -> player.playerKeyReleasedHandler(initialKeyRelease));
			window.show();

		}

	private void storyModeButtonHandler() {
		this.story = true;
		window.setScene(new Scene(StartingLevel())); // Generates new Scene
		window.getScene().setOnKeyPressed(initialKeyPress -> player.playerKeyPressedHandler(initialKeyPress));
		window.getScene().setOnKeyReleased(initialKeyRelease -> player.playerKeyReleasedHandler(initialKeyRelease));
		window.show();
	}

	private void quitButtonHandler() {
		window.close();
	}

	private void menuButtonHandler() {
		if (story || labyrinth) {
			highscore.checkHighscore(player, "highscore.txt");
		}
		story = false;
		labyrinth = false;
		level.resetGame();
		window.setScene(new Scene(createMenu()));
		window.show();
	}
	
	private void resumeButtonHandler() {
		player.setPause(false);
		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).isAnimated()) {
				enemies.get(i).getAnimation().play();
			}
		}
		player.getAnimation().play();
		root.getChildren().remove(pauseMenu);
		GameLoop.start();
	}
	
	private void playAgainButtonHandler() {
		if (this.story == true) {
			level.resetGame();
			storyModeButtonHandler();
		} else if (this.labyrinth == true) {
			level.resetGame();
			labyrinthButtonHandler();
		}
	}
	
public void nameButtonHandler() {
		
		// Gets name from text field
		
		// No spaces so file is more easily read
		if (nameField.getText().contains(" ")) {
			nameField.setText("No spaces allowed");
		}
		else {
			highscore.setName(nameField.getText());
			nameField.setText("Success!");
		}
	}

//Starting level for Story and labyrinth
	public Parent StartingLevel() // First Level Generation
	{
		phaseOneLayering = new Layers("/Assets/Tutorial Background No Clouds.png"); // Instantiating layer object
		root = phaseOneLayering.generate(root); // Making the root all the layers
		Pane playfieldLayer = phaseOneLayering.getPlayfieldLayer();
		Pane UILayer = phaseOneLayering.getUILayer();
		player = new PlayerSprite(playfieldLayer, START_FIRERATE, START_DAMAGE); // Creating the player
		player.displayHealth(UILayer); // Show hearts on screen
		highscore.displayScore(UILayer, player); // Displays score on bottom left of the screen
		player.setPhaseAmmo(level.getLevel());
		if (story == true) {
			level.StoryGeneration(playfieldLayer); // Generating the enemies for the first level
		}
		if (labyrinth == true) {
			level.LabyrinthGeneration(playfieldLayer, SpawnValues);
		}
		GameLoop = new AnimationTimer() // Instantiating the Animation timer
		{
			@Override
			public void handle(long now) {
				onUpdate(); // GameLoop
			}
		};

		GameLoop.start(); // Starting the timer

		return root; // Returning the root to set the "scene" on the stage
	}
	
	
	// Progression method for Story
	public Parent storyLevelProgression() { // See StartingLevel() notes

		level.nextLevel(); // Updates the GameLevel so all enemies can scale accordingly
		updateLevelBackground(); // Updates background to classroom after level 4
		root = phaseOneLayering.generate(root);
		Pane playfieldLayer = phaseOneLayering.getPlayfieldLayer();
		Pane UILayer = phaseOneLayering.getUILayer();
		PlayerSprite player2 = new PlayerSprite(playfieldLayer, player);
		player.displayHealth(UILayer); // Show hearts on screen
		highscore.displayScore(UILayer, player2);
		level.StoryGeneration(playfieldLayer);
		player = player2;
		player.setPhaseAmmo(level.getLevel()); // Updates ammo to pencils after level 4
		GameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				onUpdate();
			}
		};

		GameLoop.start();

		return root;

	}
	
	
	// Scene transition to next level method
	public void nextLevel() {
		if (level.getGameState() == true) {
			if (story == true) {
				// For story mode, gameloop is stopped and bullets are set to dead
				this.GameLoop.stop();
				for (Sprite bullet : bullets) {
					bullet.setAlive(false);
				}
				window.setScene(new Scene(storyLevelProgression())); // Generates new Scene
				window.getScene().setOnKeyPressed(initialKeyPress -> player.playerKeyPressedHandler(initialKeyPress));
				window.getScene().setOnKeyReleased(initialKeyRelease -> player.playerKeyReleasedHandler(initialKeyRelease));
				window.show();
			}
			if (labyrinth == true) {
				Pane playfieldLayer = phaseOneLayering.getPlayfieldLayer();
				level.nextLabLevel(this.SpawnValues);
				level.LabyrinthGeneration(playfieldLayer, SpawnValues);
			}
		}
	}
	
	
	// Update methods
	public void onUpdate() { // Game loop

		Pane playfieldLayer = phaseOneLayering.getPlayfieldLayer();
		Pane bulletLayer = phaseOneLayering.getBulletLayer();
		Pane UILayer = phaseOneLayering.getUILayer();
		player.move(); // Moves player

		level.resetImages(playfieldLayer, bulletLayer, player);
		phaseOneLayering.NPCIterator(player); // NPO update of all the values of the enemies
		if (level.getLevel() <= 4) {  // Makes sure that the layer object does not try to animate non existant clouds
			phaseOneLayering.animationIterator();
		}

		phaseOneLayering.bulletIterator(); // updates the Bullets
		player.shoot(bulletLayer); // Allows player to shoot at a given fire rate
		level.detectCollision(playfieldLayer, bulletLayer, UILayer, player, this.labyrinth, SpawnValues, highscore);
		
		
		// bullets
		phaseOneLayering.bulletBounds(); // Detects when bullets fly out of screen

		level.gameState(); // Checks to see if the level is completed
		pauseGame();
		nextLevel(); // If level is completed, this method will sitch scenes to the next level
		gameOver();

	}

	public void pauseGame() {
		// Pauses the game
		if (player.isPaused()) {
			// Pauses the enemies that have animations
			for (int i = 0; i < enemies.size(); i++) {
				if (enemies.get(i).isAnimated()) {
					enemies.get(i).getAnimation().stop();
				}
			}
			player.getAnimation().stop(); // Stops animation of player
			GameLoop.stop(); 
			createPauseMenu();
		}
	}

	public void gameOver() {
		if (player.getHealth() <= 0) {
			root.getChildren().add(deathSequenceView); // Displays a "You Died" message
			deathSequenceView.setOpacity(0); // Image starts invisible
			FadeTransition gameOverCutscene = new FadeTransition(Duration.millis(3000), deathSequenceView);
			// Image has a fade in transition
			deathSequenceView.setFitWidth(SCRN_WIDTH);
			deathSequenceView.setPreserveRatio(true);
			gameOverCutscene.setFromValue(0);
			gameOverCutscene.setToValue(1);
			gameOverCutscene.setCycleCount(1);
			gameOverCutscene.play();
			highscore.checkHighscore(player, "highscore.txt");
			GameLoop.stop();
			createGameOverMenu();
		}
	}
	
	public void updateLevelBackground() {
		if (level.getLevel() > 4) {
			Layers phaseTwoLayering = new Layers("/Assets/Classroom.png");
			phaseOneLayering = phaseTwoLayering;
		}
	}
	
	
	// Other
	@Override
	public void start(Stage primaryStage) { // Start method for the Game

		window = primaryStage;
		highscore.readFile("highscore.txt");
		primaryStage.setScene(new Scene(createMenu()));
		primaryStage.setTitle("Dream Labyrinth");
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
