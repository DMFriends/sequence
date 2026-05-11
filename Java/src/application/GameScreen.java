package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GameScreen
{
	private Stage primaryStage;
	private GameState gameState;
	private Scene scene;
	private GridPane boardGrid;
	private HBox handBox;
	private VBox scoreboard;
	private Label statusLabel;
	private Label lastMoveLabel;
	private Card selectedCard;
	
	public GameScreen(Stage primaryStage, GameState gameState)
	{
		this.primaryStage = primaryStage;
		this.gameState = gameState;
		this.scene = new Scene(buildUI(), 1000, 700);
	}
	
	public Scene getScene()
	{
		return scene;
	}

	public void showCurrentTurn()
	{
		selectedCard = null;
		refresh();
		primaryStage.setScene(scene);
	}
	
	private Parent buildUI()
	{
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: #f7f4ef;");

		statusLabel = new Label();
		statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
		lastMoveLabel = new Label();
		lastMoveLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #555555;");

		VBox topBox = new VBox(4, statusLabel, lastMoveLabel);
		topBox.setPadding(new Insets(16, 20, 8, 20));
		topBox.setAlignment(Pos.CENTER);
		root.setTop(topBox);

		boardGrid = new GridPane();
		boardGrid.setAlignment(Pos.CENTER);
		boardGrid.setHgap(4);
		boardGrid.setVgap(4);

		StackPane boardWrapper = new StackPane(boardGrid);
		boardWrapper.setStyle("-fx-background-color: #27221d; -fx-background-radius: 8;");
		boardWrapper.setPadding(new Insets(8));
		boardWrapper.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

		VBox centerBox = new VBox(14, boardWrapper, buildControls());
		centerBox.setAlignment(Pos.CENTER);
		centerBox.setPadding(new Insets(10));

		scoreboard = new VBox(14);
		scoreboard.setPadding(new Insets(18));
		scoreboard.setPrefWidth(220);
		scoreboard.setStyle("-fx-background-color: #fffaf3;");
		root.setRight(scoreboard);

		handBox = new HBox(10);
		handBox.setAlignment(Pos.CENTER);
		handBox.setPadding(new Insets(12, 0, 8, 0));

		VBox playArea = new VBox(8, centerBox, handBox);
		playArea.setAlignment(Pos.CENTER);
		playArea.setPadding(new Insets(0, 0, 12, 0));
		root.setCenter(playArea);

		refresh();
		return root;
	}
	
	private void refresh()
	{
		refreshStatus();
		refreshBoard();
		refreshScoreboard();
		refreshHand();
	}

	private void refreshStatus()
	{
		statusLabel.setText(gameState.getCurrentPlayer().getName() + "'s turn");
		statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #222222;");
		lastMoveLabel.setText(gameState.getLastMoveDescription());
	}

	private void refreshBoard()
	{
		boardGrid.getChildren().clear();

		for (int row = 0; row < Board.ROWS; row++)
		{
			for (int col = 0; col < Board.COLS; col++)
			{
				boardGrid.add(buildBoardCell(row, col), col, row);
			}
		}
	}

	private StackPane buildBoardCell(int row, int col)
	{
		StackPane cell = new StackPane();
		cell.setPrefSize(82, 72);
		cell.setMinSize(82, 72);
		cell.setMaxSize(82, 72);

		Player owner = gameState.getOwner(row, col);

		if (owner == null)
		{
			Label cardLabel = new Label(gameState.getBoardCard(row, col));
			cardLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;");
			cell.getChildren().add(cardLabel);
			cell.setStyle(openCellStyle(row, col));
			cell.setOnMouseClicked(_ -> onBoardCellClicked(row, col));
		}
		else
		{
			Label peg = new Label("X");
			peg.setStyle("-fx-font-size: 34px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
			cell.getChildren().add(peg);
			cell.setStyle("-fx-background-color: " + playerColor(owner)
					+ "; -fx-background-radius: 6; -fx-border-color: #2c2824; -fx-border-radius: 6;");
		}

		return cell;
	}

	private String openCellStyle(int row, int col)
	{
		String background = "#fffdf7";

		if (selectedCard != null)
		{
			if (selectedCard.getRank() == Card.JACK)
			{
				background = "#e8f4ff";
			}
			else if (gameState.getBoardCard(row, col).equals(selectedCard.toString()))
			{
				background = "#fff0a6";
			}
		}

		return "-fx-background-color: " + background
				+ "; -fx-background-radius: 6; -fx-border-color: #d4c7b8; -fx-border-radius: 6;";
	}

	private void refreshHand()
	{
		handBox.getChildren().clear();

		Label handLabel = new Label("Hand");
		handLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		handBox.getChildren().add(handLabel);

		for (Card card : gameState.getCurrentPlayer().getCards())
		{
			Button cardButton = new Button(card.toString());
			cardButton.setPrefSize(68, 54);
			cardButton.setStyle(cardButtonStyle(card));
			cardButton.setOnAction(_ ->
			{
				selectedCard = card;
				statusLabel.setText(card.getRank() == Card.JACK
						? "Wild card selected. Choose any open space."
						: "Selected " + card + ". Choose its matching space.");
				statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1f4d77;");
				refreshBoard();
				refreshHand();
			});
			handBox.getChildren().add(cardButton);
		}
	}

	private String cardButtonStyle(Card card)
	{
		String textColor = (card.getSuit() == Card.HEARTS || card.getSuit() == Card.DIAMONDS) ? "#b22222" : "#222222";
		String borderColor = selectedCard == card ? "#1f6fb2" : "#c9bba8";
		String borderWidth = selectedCard == card ? "3" : "1";

		return "-fx-background-color: #ffffff; -fx-background-radius: 6;"
				+ " -fx-border-color: " + borderColor + "; -fx-border-width: " + borderWidth + ";"
				+ " -fx-border-radius: 6; -fx-font-size: 16px; -fx-font-weight: bold;"
				+ " -fx-text-fill: " + textColor + ";";
	}

	private void refreshScoreboard()
	{
		scoreboard.getChildren().clear();

		Label title = new Label("Score");
		title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
		scoreboard.getChildren().add(title);

		for (Player player : gameState.getPlayers())
		{
			Label playerScore = new Label(player.getName() + "\n"
					+ formatSequenceCount(player.getSequenceCount()) + "\n"
					+ player.getCards().size() + " cards");
			playerScore.setMaxWidth(Double.MAX_VALUE);
			playerScore.setStyle("-fx-background-color: " + playerColor(player)
					+ "; -fx-background-radius: 6; -fx-padding: 10;"
					+ " -fx-text-fill: #ffffff; -fx-font-size: 14px; -fx-font-weight: bold;");
			scoreboard.getChildren().add(playerScore);
		}

		Region spacer = new Region();
		VBox.setVgrow(spacer, Priority.ALWAYS);

		Label deckCount = new Label("Deck: " + gameState.getDeckSize() + " cards");
		deckCount.setStyle("-fx-font-size: 14px;");

		scoreboard.getChildren().addAll(spacer, deckCount);
	}

	private HBox buildControls()
	{
		HBox controls = new HBox(10);
		controls.setAlignment(Pos.CENTER);

		Button endTurn = new Button("End Game");
		endTurn.setOnAction(_ -> onEndGame());

		Button exportBoard = buildExportBoardButton();
		controls.getChildren().addAll(exportBoard, endTurn);
		return controls;
	}

	private Button buildExportBoardButton()
	{
		Button exportBoard = new Button("Export Board as PNG");
		exportBoard.setOnAction(_ -> onExportBoardPng());
		return exportBoard;
	}

	private void onBoardCellClicked(int row, int col)
	{
		if (selectedCard == null)
		{
			statusLabel.setText("Choose a card from your hand first.");
			statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #9a3c1c;");
			return;
		}

		if (!gameState.playCard(selectedCard, row, col))
		{
			statusLabel.setText(gameState.getLastMoveError());
			statusLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #9a3c1c;");
			return;
		}

		selectedCard = null;

		if (gameState.isGameOver())
		{
			switchToEndScreen();
			return;
		}

		BufferScreen bufferScreen = new BufferScreen(primaryStage, gameState, this);
		primaryStage.setScene(bufferScreen.getScene());
	}
	
	private void onEndGame()
	{
		gameState.endGame();
		switchToEndScreen();
	}

	private void switchToEndScreen()
	{
		EndScreen endScreen = new EndScreen(primaryStage, gameState);
		primaryStage.setScene(endScreen.getScene());
	}
	
	private void onExportBoardPng()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Export Board as PNG");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG Files", "*.png"));
		fileChooser.setInitialFileName("sequence_board.png");
		
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file == null)
		{
			return;
		}
		
		file = ensurePngExtension(file);
		WritableImage image = boardGrid.snapshot(null, null);
		
		try
		{
			ImageIO.write(toBufferedImage(image), "png", file);
		}
		catch (IOException e)
		{
			Alert alert = new Alert(Alert.AlertType.ERROR, "Could not export the board image.");
			alert.showAndWait();
		}
	}
	
	private BufferedImage toBufferedImage(WritableImage image)
	{
		int width = (int) image.getWidth();
		int height = (int) image.getHeight();
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		PixelReader pixelReader = image.getPixelReader();
		
		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				bufferedImage.setRGB(x, y, pixelReader.getArgb(x, y));
			}
		}
		
		return bufferedImage;
	}
	
	private File ensurePngExtension(File file)
	{
		if (file.getName().toLowerCase().endsWith(".png"))
		{
			return file;
		}
		
		return new File(file.getParentFile(), file.getName() + ".png");
	}

	private String playerColor(Player player)
	{
		return gameState.getPlayers().indexOf(player) == 0 ? "#c23b3b" : "#2f65b8";
	}

	private String formatSequenceCount(int count)
	{
		return count + (count == 1 ? " sequence" : " sequences");
	}
}
