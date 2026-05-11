package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class StartScreen
{
	private Stage primaryStage;
	private Scene scene;
	private List<TextField> nameFields;
	
	public StartScreen(Stage primaryStage)
	{
		this.primaryStage = primaryStage;
		this.nameFields = new ArrayList<>();
		this.scene = new Scene(buildUI(), 600, 500);
	}
	
	public Scene getScene()
	{
		return scene;
	}
	
	private Parent buildUI()
	{
		VBox root = new VBox(20);
	    root.setPadding(new Insets(40));
	    root.setAlignment(Pos.CENTER);

	    Label title = new Label("Sequence");
	    title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");
	    
	    String instrText = "This is a 2-player game.\nPlease enter your names\nbelow and read the rules by clicking\nthe \"Read Rules\" button below.";
	    Label instructions = new Label(instrText);
	    instructions.setTextAlignment(TextAlignment.CENTER);
	    instructions.setStyle("-fx-font-size: 20px;");
	    
	    VBox nameFieldsBox = new VBox(10);
	    buildNameFields(2, nameFieldsBox);

	    Button startButton = new Button("Start Game");
	    startButton.setOnAction(_ -> onStartClicked());
	    
	    Button readRules = new Button("Read Rules");
	    readRules.setOnAction(_ -> onReadRulesClicked());

	    root.getChildren().addAll(title, instructions, nameFieldsBox, startButton, readRules);
	    return root;
	}
	
	private void buildNameFields(int count, VBox container)
	{
		container.getChildren().clear();
		nameFields.clear();

		for (int i = 0; i < count; i++)
		{
			TextField field = new TextField();
			field.setPromptText("Player " + (i + 1) + " name: ");
			nameFields.add(field);
			container.getChildren().add(field);
		}
	}
	
	private void onStartClicked()
	{
		List<Player> players = new ArrayList<>();
	    
		for (int i = 0; i < nameFields.size(); i++)
		{
			String name = nameFields.get(i).getText().trim();
			if (name.isEmpty()) name = "Player " + (i + 1);
			players.add(new Player(name));
		}
	    
	    switchToGame(players);
	}
	
	private void onReadRulesClicked()
	{
		File file = new File("../rules.txt");
		try
		{
			new ProcessBuilder("powershell", "-command", "Invoke-Item -Path '" +
					file.getAbsolutePath() + "'").start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void switchToGame(List<Player> players)
	{
		GameState gameState = new GameState(players);
		GameScreen gameScreen = new GameScreen(primaryStage, gameState);
		BufferScreen bufferScreen = new BufferScreen(primaryStage, gameState, gameScreen);
		primaryStage.setScene(bufferScreen.getScene());
		primaryStage.setResizable(false);
	}
}
