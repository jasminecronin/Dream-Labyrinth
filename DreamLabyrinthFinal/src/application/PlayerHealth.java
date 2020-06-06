package application;
import application.Sprite;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class PlayerHealth extends Sprite implements Universals{
	
	private Image image = new Image("/Assets/Health_Indicator.png");
	private ImageView health = new ImageView(image);

	public PlayerHealth(Pane Layer, int pos) { //Constructor for playerHealth hearts
		this.setImageView(health);
		this.getImageView().setScaleX(0.5);
		this.getImageView().setScaleY(0.5);
		this.setXpos(pos);
		this.setYpos(SCRN_HEIGHT - 60);
		Layer.getChildren().add(health);
		health.relocate(xpos, ypos);
	}
}
