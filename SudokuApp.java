package application;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SudokuApp extends Application {
	private static final int SIZE = 9;
	private static final int CELL_SIZE = 40;

	private TextField[][] cells = new TextField[SIZE][SIZE];
	private Sudoku sudoku = new Sudoku();

	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();

		// Felder erzeugen und in grid einfügen
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				TextField field = new TextField();
				field.setPrefSize(CELL_SIZE, CELL_SIZE);
				field.setFont(Font.font(18));
				field.setAlignment(Pos.CENTER);
				cells[row][col] = field;

				grid.add(field, col, row); // Platziert Text Feld in aktuelle Position row und col
			}
		}

		// Container für Linien
		Pane overlay = new Pane();
		overlay.setPickOnBounds(false); // Mausereignisse werden durchgelassen

		// Dicke Linie zeichnen alle 3 Felder
		for (int i = 0; i <= SIZE; i++) {
			int thickness = (i % 3 == 0) ? 4 : 1; // Wenn 3tes Feld, dann thickness 4, sonst auf 1
			int offset = i * CELL_SIZE; // x-Wert für hLine

			// Vertikale Linie
			Line hLine = new Line(offset, 0, offset, SIZE * CELL_SIZE); // Zeichnet Linie von Punkt x1, y1 zu x2, y2
			hLine.setStrokeWidth(thickness);
			hLine.setStroke(Color.BLACK);
			hLine.setMouseTransparent(true);
			overlay.getChildren().add(hLine);

			// Horizontale Linie
			Line vLine = new Line(0, offset, SIZE * CELL_SIZE, offset); // Zeichnet Linie von Punkt x1, y1 zu x2, y2
			vLine.setStrokeWidth(thickness);
			vLine.setStroke(Color.BLACK);
			vLine.setMouseTransparent(true);
			overlay.getChildren().add(vLine);
		}

//		Grid und Overlay übereinander
		StackPane sudokuLayer = new StackPane();
		sudokuLayer.getChildren().addAll(grid, overlay);

		Button checkButton = new Button("Check");
		Button solveButton = new Button("Solve");
		Button newGameButton = new Button("New Game");

		checkButton.setOnAction(e -> {
			readInputToBoard();
			System.out.println("Check Button was pressed:");
			printBoardToConsole();

			if (!allCellsFilled()) {
				showAlert("Incomplete", "Please fill in all cells before checking.");
			} else if (sudoku.check()) {
				showAlert("Success", "The Sudoku is valid!");
			} else {
				showAlert("Error", "The Sudoku contains errors.");
			}
		});

		solveButton.setOnAction(e -> {
			sudoku.solve();
			updateBoardFromSudoku();
			System.out.println("Solve Button was pressed:");
			printBoardToConsole();
		});

		newGameButton.setOnAction(e -> {
			sudoku.generate();
			updateBoardFromSudoku();
			System.out.println("New Game Button was pressed:");
			printBoardToConsole();
			System.out.println("Solution:");
			sudoku.solve();
			printBoardToConsole();
		});

//		Buttons in einer HBox
		HBox buttonBox = new HBox(10, checkButton, solveButton, newGameButton);
		buttonBox.setAlignment(Pos.CENTER);

		// SudokuLayer in eine HBox und die dann zentriert
		HBox centerWrapper = new HBox(sudokuLayer);
		centerWrapper.setAlignment(Pos.CENTER);

		// Ohne trennung in VBox lassen sich nicht alle Felder mit der Maus bedienen
		VBox root = new VBox(10, centerWrapper, buttonBox);
		root.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root, SIZE * CELL_SIZE, SIZE * CELL_SIZE + CELL_SIZE + 4); // Fenster Abmaße
		primaryStage.setTitle("Sudoku ~ made by Giuseppe");
		primaryStage.setScene(scene);
		primaryStage.show();

		sudoku.generate();
		updateBoardFromSudoku();
	}

//	Zahlen im Feld setzen
	private void updateBoardFromSudoku() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				int value = sudoku.board[row][col];
				TextField field = cells[row][col];
				if (value != 0) {
					field.setText(String.valueOf(value));
					field.setDisable(true);
				} else {
					field.clear();
					field.setDisable(false);
				}
			}
		}
	}

	private void readInputToBoard() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				TextField field = cells[row][col];
				if (!field.isDisabled()) {
					String text = field.getText();
					if (text.matches("[1-9]")) {
						sudoku.board[row][col] = Integer.parseInt(text);
					} else {
						sudoku.board[row][col] = 0;
					}
				}
			}
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private boolean allCellsFilled() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				if (sudoku.board[row][col] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	private void printBoardToConsole() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				System.out.print(sudoku.board[row][col] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		launch(args);
	}
}