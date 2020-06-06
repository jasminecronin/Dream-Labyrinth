package application;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

class HighScoreTest {
	Label fill = new Label();
	@Test
	void test_readFile1() {
		HighScore hs = new HighScore();
		hs.readFile("test1.txt");
		String expected = "Guest: 30";
		String res = hs.getScores().get(4);
		assertEquals("Strings don't match", expected, res);
	}
	
	@Test
	void test_readFile2() {
		HighScore hs = new HighScore();
		hs.readFile("test2.txt");
		String expected = "Guest: 1100";
		String res = hs.getScores().get(0);
		assertEquals("Strings don't match", expected, res);
	}

}
