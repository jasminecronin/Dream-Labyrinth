package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.Animation;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * Sprite Manager. Most methods are purposefully left empty as all the called methods are overridden by subclasses.
 * @author Quinn
 *
 */
public class Sprite {
	Animation animation;
	protected boolean isAnimated;
	protected Pane Layer;
	protected ImageView imageView;
	protected boolean alive = true;
	protected double defaultX;
	protected double defaultY;
	protected double xpos;
	protected double ypos;
	protected double translateX;
	protected double translateY;
	protected double MaxHealth;
	protected double health;
	protected double damage;
	protected double maxDamage;
	protected int GameLevel; // used for scaling... no idea how to fetch this data yet (from the scene)
	protected int MaxCountdown;
	protected int Countdown; // Used for the enemies various movement/action methods as well as other
								// attributes for the various sprites
	protected int score;
	ColorAdjust hit = new ColorAdjust(); //used to trigger the "hit" animation/effect
	public Sprite() {} // empty constructor

	// All these attributes will be inherited by the subcategories
	// Scaling for movement, health, etc are handled within each class of sprite.

	public int getCountdown() {
		return this.Countdown;
	}

	public void setCountdown(int result) {
		this.Countdown = result;
	}

	public Pane getLayer() {
		return this.Layer;
	}

	public void setLayer(Pane layer) {
		this.Layer = layer;
	}

	public ImageView getImageView() {
		return this.imageView;
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	public double getXpos() {
		return this.xpos;
	}

	public void setXpos(double x) {
		this.xpos = x;
	}

	public double getYpos() {
		return this.ypos;
	}

	public void setYpos(double y) {
		this.ypos = y;
	}

	public double getTranslateDX() {
		return this.translateX;
	}

	public void setTranslateDX(double dx) {
		this.translateX = dx;
	}

	public double getTranslateDY() {
		return this.translateY;
	}

	public void setTranslateDY(double dy) {
		this.translateY = dy;
	}

	public boolean isAlive() {
		return alive;
	}

	public boolean isDead() {
		return !alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getGameLevel() {
		return this.GameLevel;
	}

	public void setGameLevel(int i) {
		this.GameLevel = i;
	}

	public double getDefaultX() {
		return this.defaultX;
	}

	public double getDefaultY() {
		return this.defaultY;
	}

	public void setDefaultX(double d) {
		this.defaultX = d;
	}

	public void setDefaultY(double d) {
		this.defaultY = d;
	}

	public double getHealth() {
		return this.health;
	}

	public void setHealth(double d) {
		this.health = d;
	}

	public double getDamage() {
		return this.damage;
	}

	public void setDamage(double d) {
		this.damage = d;
	}
	
	public double getMaxDamage() {
		return this.maxDamage;
	}

	public void setMaxDamage(double d) {
		this.maxDamage = d;
	}

	public double getMaxHealth() {
		return this.MaxHealth;
	}

	public void setMaxHealth(double d) {
		this.MaxHealth = d;
	}

	public int getMaxCountdown() {
		return this.MaxCountdown;
	}

	public void setMaxCountdown(int i) {
		this.MaxCountdown = i;
	}
	
	public void setScore(int i)
	{
		this.score = i;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public void setAnimated(boolean animated) {
		isAnimated = animated;
	}
	
	public boolean isAnimated() {
		return isAnimated;
	}
	
	public Animation getAnimation() {
		return animation;
		
	}

	public void move() // generic "move" for any and all sprites (all subclasses will either have, or
						// not, their own unique move())"
	// e.g. bullets do not have a move() thus they use this move
	// Minions have their own move()
	{
		this.setXpos(this.xpos + this.translateX);
		this.setYpos(this.ypos + this.translateY);
		this.getImageView().relocate(this.getXpos(), this.getYpos());	
	}

	public boolean isColliding(Sprite enemy) // Using to condense and applying the FX collision checking method
	{
		return getImageView().getBoundsInParent().intersects(enemy.getImageView().getBoundsInParent());
	}

	public void move(Pane playfieldLayer, Pane bulletLayer,  PlayerSprite player) {}

	public void upgrade(PlayerSprite player) {}

	public void UpgradeChance(Pane playfieldLayer,double xpos, double ypos) {}

	public void hit() {
		this.getImageView().setEffect(hit);		
	}

	public void dies(PlayerSprite player, boolean labyrinth, SpawnRate spawnValues) {
		//Default die method for all sprites.
		//if Labyrinth = true, do additional functions specific for the game mode
	}

}
