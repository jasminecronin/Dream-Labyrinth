package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.Pane;

public interface Universals {

	public static final int SCRN_WIDTH = 1200; // Screen Width
	public static final int SCRN_HEIGHT = 900;
	public static final int BOTTOM_BORDER = -50;

	public static final int START_FIRERATE = 35;
	public static final double START_DAMAGE = 20;

	public List<Sprite> enemyBullets = new ArrayList<>(); // List for enemy projectiles
	public List<Sprite> upgrades = new ArrayList<>(); // List for managing upgrades
	public List<Sprite> enemies = new ArrayList<>(); // Creating Enemy List for Iteration
	public List<Sprite> bullets = new ArrayList<>(); // Creating Bullet List for Iteration
	public List<Sprite> hearts = new ArrayList<>(); // Player Health
	public List<Sprite> clouds = new ArrayList<>(); // List for clouds
}
