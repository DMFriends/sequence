package application;
	
import java.util.Objects;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class Main extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		try
		{
			primaryStage.setTitle("Sequence");
	        
	        primaryStage.getIcons().add(
			    new Image(
			        Objects.requireNonNull(
			            getClass().getResourceAsStream("/resources/sequence.png")
			        )
			    )
			);
	        
	        primaryStage.setMaxWidth(Screen.getPrimary().getVisualBounds().getWidth());
	        primaryStage.setMaxHeight(Screen.getPrimary().getVisualBounds().getHeight());
	        primaryStage.setResizable(false);

	        StartScreen startScreen = new StartScreen(primaryStage);
	        primaryStage.setScene(startScreen.getScene());
	        primaryStage.show();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
