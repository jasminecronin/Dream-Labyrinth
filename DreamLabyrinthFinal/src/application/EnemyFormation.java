package application;

import java.util.ArrayList;

import javafx.scene.layout.Pane;

public class EnemyFormation implements Universals {
	
	
	Sprite enemy;
	String enemyType;
	Pane currentLayer;
	int level;
	
	// Constructor for enemy formations, takes type of enemy, layer, and their level
	public EnemyFormation(String newEnemyType, Pane newCurrentLayer, int newLevel) {
		enemyType = newEnemyType;
		currentLayer = newCurrentLayer;
		level = newLevel;
	}
	
	// Makes a circular formation made up of the enemy type
	public void radialFormation(double initialX, double initialY, double radius, double lowerLimit, double greaterLimit, int spacing) {
		double radians = Math.toRadians(lowerLimit);
		while (Math.toRadians(greaterLimit) >= radians) {
			switch(enemyType) {
			case("donut"):
				enemy = new Donut(currentLayer, level);
				break;
			case("ice"):
				enemy = new IceCream(currentLayer, level);
				break;
			case("txtbk"):
				enemy = new Textbook(currentLayer, level);
				break;
			case("clock"):
				enemy = new Clock(currentLayer, level);
				break;
			case("flask"):
				enemy = new Flask(currentLayer, level);
				break;
			case("melon"):
				enemy = new Watermelon(currentLayer, level);
				break;
			case("loan"):
				enemy = new StudentLoan(currentLayer, level);
				break;
			}
			enemy.setYpos((Math.sin(radians) * radius) + initialY);
			enemy.setXpos((Math.cos(radians) * radius) + initialX);
			enemies.add(enemy);
			radians += Math.toRadians(spacing);
		}
	}
	
	
	// Makes a triangular formation made up of the enemy type
	public void triangularFormation(int initialX, int initialY, int bases, int spacing, boolean inverted) {
		int x = initialX;
		int count = 0;
		while(bases > 0) {
			for (int i = 0; i < bases; i++) {
				switch(enemyType) {
				case("donut"):
					enemy = new Donut(currentLayer, level);
					break;
				case("ice"):
					enemy = new IceCream(currentLayer, level);
					break;
				case("txtbk"):
					enemy = new Textbook(currentLayer, level);
					break;
				case("clock"):
					enemy = new Clock(currentLayer, level);
					break;
				case("flask"):
					enemy = new Flask(currentLayer, level);
					break;
				case("melon"):
					enemy = new Watermelon(currentLayer, level);
					break;
				case("loan"):
					enemy = new StudentLoan(currentLayer, level);
					break;
				}
				enemy.setXpos(initialX);
				enemy.setYpos(initialY);
				enemies.add(enemy);
				initialX -= spacing;
			}
			count ++;
			if (inverted) { initialY -= spacing; }
			else { initialY += spacing; }
			initialX = x;
			initialX -= (spacing / 2) * count;
			bases--;
		}
	}
	
	
	// Makes a diagonal formation made up of the enemy type
	public void diagonalFormation(int initialX, int initialY, int length, int spacing, boolean inverted) {
		if (inverted) { initialX = SCRN_WIDTH - initialX; }
		while(length > 0) {
			switch(enemyType) {
			case("donut"):
				enemy = new Donut(currentLayer, level);
				break;
			case("ice"):
				enemy = new IceCream(currentLayer, level);
				break;
			case("txtbk"):
				enemy = new Textbook(currentLayer, level);
				break;
			case("clock"):
				enemy = new Clock(currentLayer, level);
				break;
			case("flask"):
				enemy = new Flask(currentLayer, level);
				break;
			case("melon"):
				enemy = new Watermelon(currentLayer, level);
				break;
			case("loan"):
				enemy = new StudentLoan(currentLayer, level);
				break;
			}
			enemy.setXpos(initialX);
			enemy.setYpos(initialY);
			enemies.add(enemy);
			if (inverted) { initialX += spacing; initialY -= spacing; }
			else { initialX -= spacing; initialY -= spacing; }
			length --;
		}
	}
	
	
	// Makes a diamond formation made up of the enemy type
	public void diamondFormation(int initialX, int initialY, int bases, int spacing) {
		triangularFormation(initialX, initialY, bases, spacing, true);
		triangularFormation(initialX, initialY, bases, spacing, false);
	}
	
	
	// Makes a V formation made up of the enemy type
	public void vFormation(int initialX, int initialY, int length, int spacing) {
		diagonalFormation(initialX, initialY, length, spacing, true);
		diagonalFormation(initialX - 200, initialY, length, spacing, false);
	}
	
	
	// Makes a rectangular formation made up of the enemy type
	public void rectangleFormation(int initialX, int initialY, int bases, int length, int spacing) {
		int x = initialX;
		for (int i = 0; i < bases; i++) {
			for (int j = 0; j < length; j++) {
				switch(enemyType) {
				case("donut"):
					enemy = new Donut(currentLayer, level);
					break;
				case("ice"):
					enemy = new IceCream(currentLayer, level);
					break;
				case("txtbk"):
					enemy = new Textbook(currentLayer, level);
					break;
				case("clock"):
					enemy = new Clock(currentLayer, level);
					break;
				case("flask"):
					enemy = new Flask(currentLayer, level);
					break;
				case("melon"):
					enemy = new Watermelon(currentLayer, level);
					break;
				case("loan"):
					enemy = new StudentLoan(currentLayer, level);
					break;
				}
				enemy.setXpos(initialX);
				enemy.setYpos(initialY);
				enemies.add(enemy);
				initialX += spacing;
			}
			initialX = x;
			initialY += spacing;
		}
	}
	
	
	// // Makes an hour glass formation made up of the enemy type
	public void hourGlassFormation(int initialX, int initialY, int bases, int length, int spacing) {
		triangularFormation(initialX, initialY, bases, spacing, true);
		triangularFormation(initialX, initialY / 2, bases, spacing, false);
	}	
	
	// Makes a formation I made up
	public void unformalFormation(int centre, double radius, double mainRadius, double limiter, int spacing, int mainSpacing) {
		//Recommended input 400, 100, 200, 180, 45, 90
		double rads = Math.PI * 2;
		while (rads >= 0) {
			double radians = 2 * Math.PI;
			double x = ((Math.cos(rads) * mainRadius) + centre);
			double y = ((Math.sin(rads) * mainRadius) + centre);
			while (radians >= Math.toRadians(limiter)) {
				switch(enemyType) {
				case("donut"):
					enemy = new Donut(currentLayer, level);
					break;
				case("ice"):
					enemy = new IceCream(currentLayer, level);
					break;
				case("txtbk"):
					enemy = new Textbook(currentLayer, level);
					break;
				case("clock"):
					enemy = new Clock(currentLayer, level);
					break;
				case("flask"):
					enemy = new Flask(currentLayer, level);
					break;
				case("melon"):
					enemy = new Watermelon(currentLayer, level);
					break;
				case("loan"):
					enemy = new StudentLoan(currentLayer, level);
					break;
				}
				enemy.setYpos((Math.sin(radians) * radius) + y);
				enemy.setXpos((Math.cos(radians) * radius) + x);
				enemies.add(enemy);
				radians -= Math.toRadians(spacing);
			}
			
			rads -= Math.toRadians(mainSpacing);
		}
	}
}
