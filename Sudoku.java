package application;

import java.util.ArrayList;
import java.util.Collections;

public class Sudoku {
	private static final int SIZE = 9;
	public int[][] board = new int[SIZE][SIZE];
	private int[][] solution = new int[SIZE][SIZE];

	public void generate() {
		// Board leeren für saubere neu generierung
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				board[i][j] = 0;
			}
		}

		if (solveBoard()) {
			for (int i = 0; i < SIZE; i++) {
				System.arraycopy(board[i], 0, solution[i], 0, SIZE);
			}

			removeCells();
		}
	}

	private boolean solveBoard() {
		return solve(0, 0);
	}

	private boolean solve(int row, int col) {
		if (row == SIZE)
			return true; // Wenn alle Zeilen bearbeitet, dann Sudoku gelöst
		if (col == SIZE)
			return solve(row + 1, 0); // Wenn alle Spalten bearbeitet, dann gehe zur nächsten Zeile
		if (board[row][col] != 0)
			return solve(row, col + 1); // Wenn aktuelles Feld belegt, dann gehe zum nächsten

		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (int i = 1; i <= SIZE; i++) {
			numbers.add(i);
		}
		Collections.shuffle(numbers);

		for (int num : numbers) {
			if (isValid(num, row, col)) {
				board[row][col] = num;
				if (solve(row, col + 1))
					return true; // Weiter mit dem nächsten Feld
				board[row][col] = 0; // Zurücksetzen, wenn die Zahl nicht funktioniert
			}
		}
		return false; // Wenn nicht lösbar
	}

	private boolean isValid(int num, int row, int col) {
		// Vertikale und Horizontale auf gleiche Zahlen prüfen
		for (int i = 0; i < SIZE; i++) {
			if (board[row][i] == num || board[i][col] == num)
				return false;
		}

//		3x3 Box auf gleiche Zahl prüfen
		int startRow = (row / 3) * 3;
		int startCol = (col / 3) * 3;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[startRow + i][startCol + j] == num)
					return false;
			}
		}
		return true;
	}

//	Entfernt Zahlen im Board
	private void removeCells() {
		int cellsToRemove = 50;
		while (cellsToRemove > 0) {
			int row = (int) (Math.random() * SIZE);
			int col = (int) (Math.random() * SIZE);

			if (board[row][col] != 0) {
				board[row][col] = 0;
				cellsToRemove--;
			}
		}
	}

//	Prüft ob aktuelles Sudoku Board gülltig ist bei einem Fehler wird direkt false zurückgegeben
	public boolean check() {
		for (int row = 0; row < SIZE; row++) {
			for (int col = 0; col < SIZE; col++) {
				int value = board[row][col];
				if (value != 0) {
					board[row][col] = 0; // Temporär leeren
					if (!isValid(value, row, col)) {
						board[row][col] = value; // Zurücksetzen
						return false;
					}
					board[row][col] = value; // Zurücksetzen
				}
			}
		}
		return true;
	}

//	Board mit Lösung ersetzen
	public void solve() {
		for (int i = 0; i < SIZE; i++) {
			System.arraycopy(solution[i], 0, board[i], 0, SIZE); // Kopiert komplette Zeile in solution
		}
	}
}
