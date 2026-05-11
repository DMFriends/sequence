package application;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EndScreen
{
	private Stage primaryStage;
	private GameState gameState;
	private Scene scene;

	public EndScreen(Stage primaryStage, GameState gameState)
	{
		this.primaryStage = primaryStage;
		this.gameState = gameState;
		this.scene = new Scene(buildUI(), 600, 500);
	}

	public Scene getScene()
	{
		return scene;
	}

	private Parent buildUI()
	{
		VBox root = new VBox(22);
		root.setAlignment(Pos.CENTER);
		root.setPadding(new Insets(40));
		root.setStyle("-fx-background-color: #f7f4ef;");

		Label title = new Label("Game Over");
		title.setStyle("-fx-font-size: 44px; -fx-font-weight: bold;");

		Label winner = new Label(gameState.getEndMessage());
		winner.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

		VBox results = new VBox(10);
		results.setAlignment(Pos.CENTER);

		for (Player player : gameState.getPlayers())
		{
			Label row = new Label(player.getName() + ": " + formatSequenceCount(player.getSequenceCount()));
			row.setStyle("-fx-font-size: 18px;");
			results.getChildren().add(row);
		}

		Button playAgain = new Button("Play Again");
		playAgain.setOnAction(_ ->
		{
			StartScreen startScreen = new StartScreen(primaryStage);
			primaryStage.setScene(startScreen.getScene());
		});

		Button exit = new Button("Exit");
		exit.setOnAction(_ -> Platform.exit());

		HBox buttons = new HBox(12, playAgain, exit);
		buttons.setAlignment(Pos.CENTER);

		root.getChildren().addAll(title, winner, results, buttons);
		return root;
	}

	private String formatSequenceCount(int count)
	{
		return count + (count == 1 ? " sequence" : " sequences");
	}
}
