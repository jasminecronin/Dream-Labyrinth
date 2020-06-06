package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HighScore implements Universals {
	private String highscore;
	private String name = "Guest";
	private ArrayList<String> scores = new ArrayList<String>();
	private Text scoreText = new Text(SCRN_WIDTH - 100, SCRN_HEIGHT - 10, "Score: 0");

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHighscore() {
		return highscore;
	}

	public void setHighscore(String highscore) {
		this.highscore = highscore;
	}
	
	public ArrayList<String> getScores() {
		return scores;
	}

	/**
	 * Read highscores from highscore.txt
	 * Store all scores from file into an ArrayList
	 * 
	 * @param highscore.txt
	 */
	public void readFile(String scorefile) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(scorefile));
			String s;
			while ((s = reader.readLine()) != null) { 
				scores.add(s); //Add all entries to an arraylist
			}
			this.highscore = scores.get(0); //The first entry is the highest score
			reader.close();
		} catch (Exception e) {
			System.out.println("Cannot open file");
		}

	}

	/**
	 * After player dies, check to see if a highscore was achieved. If it was,
	 * insert the new highscore and write back to the file
	 * 
	 * @param highscore.txt
	 */
	public void checkHighscore(PlayerSprite player, String scorefile) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(scorefile)));
		} catch (IOException e) {
			System.out.println("Cannot open file");
		}
		int index = 0;
		while (index < 10) { // Check all 10 entries
			if (player.getScore() > Integer.parseInt(scores.get(index).split(" ")[1])) {
				highscore = name + ": " + player.getScore();
				scores.add(index, highscore); // If the current score was greater, insert the current player
				scores.remove(10); // Remove the last entry to maintain 10 highscores
				index = 10;

			}
			index++;

		}
		// Write all scores back to the file
		for (int i = 0; i < scores.size(); i++) {
			writer.println(scores.get(i));
		}

		writer.close();
	}
	
	public void displayTopTen(Pane topTen) {
		readFile("highscore.txt");
		int top = 100;
		int middle = (SCRN_WIDTH / 2) - 15;
		Text title = new Text(middle - 27, 40, "Top Ten Players");
		title.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
		topTen.getChildren().add(title);
		for (int i = 0; i < 10; i++) {
			Text playerI = new Text(middle, top, scores.get(i));
			playerI.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
			topTen.getChildren().add(playerI);
			top += 40;
		}
	}

	/**
	 * Removes all current highscores and replaces them with Guest: 0 
	 * 
	 * @param showscore
	 * @param highscore.txt
	 */

	public void clearHighscore(Text showscore, String scorefile) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(scorefile)));
		} catch (IOException e) {
			System.out.println("Cannot open file");
		}
		for (int i = 0; i < 10; i++) {
			writer.println("Guest: 0"); // Write Guest: 0 for all 10 places in highscore file
		}
		writer.close();

		highscore = "Guest: 0";
		showscore.setText("Highscore  " + highscore);
	}

	/**
	 * Displays a text with player's current score
	 * 
	 * @param UILayer to display text on
	 * @param player
	 */
	public void displayScore(Pane layer, PlayerSprite player) {
		layer.getChildren().remove(scoreText);
		scoreText.setText("Score: " + player.getScore());
		scoreText.setFont(Font.font("Serif", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 20));
		layer.getChildren().add(scoreText);
	}
}