import java.awt.Point;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class SeminarJFXMultiThreading extends Stage {
	private static final int SIZE = 8;
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 750;
	private int startX = 0;
	private int startY = 0;
	private ArrayList<Point> moveHistoryRec = new ArrayList<>();
	private ArrayList<Point> moveHistoryIter = new ArrayList<>();
	private ChessBoard chessBoardRec = new ChessBoard(Color.BLACK,Color.LIGHTGRAY);
	private ChessBoard chessBoardIter = new ChessBoard(Color.BLACK,Color.LIGHTGRAY);		
	//Runnable classes (Tasks)
	private IterativeTask iter=new IterativeTask();
	private RecursiveTask rec=new RecursiveTask();	
	private Button btn = new Button("Solve");
	private boolean recLock = false;
	private boolean iterLock = false;
	private ComboBox<String> xComboBox;
	private ComboBox<String> yComboBox;
	private String[] xCoordinate = {"0","1","2","3","4","5","6","7"};
	private String[] yCoordinate = {"0","1","2","3","4","5","6","7"};


	public SeminarJFXMultiThreading(Button threadsButton, Button sequenceButton) { 
		threadsButton.setDisable(true);
		sequenceButton.setDisable(true);
		//** Stage object automatically created by the JVM when the application is launched **//
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10, 10, 10, 10));
		mainPane.setStyle("-fx-border-color: black; -fx-border-width: 5");

		// Headline text
		Text headLine=new Text();
		headLine.setText("Knight's Tour - Warnsdroff Algorithm");
		headLine.setFont(Font.font ("Verdana",FontWeight.BOLD, 35));
		headLine.setFill(Color.DARKSLATEBLUE);
		headLine.setCache(true);
		Reflection r = new Reflection();
		r.setFraction(0.7f);
		headLine.setEffect(r);

		HBox hb = new HBox(headLine);
		hb.setSpacing(5.0);
		hb.setAlignment(Pos.CENTER);
		hb.setPadding(new Insets(5, 0, 20, 0));
		mainPane.setTop(hb);

		Text xComboText = new Text("X start Postion");
		ObservableList<String> xItems = FXCollections.observableArrayList(xCoordinate);
		xComboBox = new ComboBox<>();
		xComboBox.getItems().addAll(xItems);
		xComboBox.setValue("0");
		xComboBox.setOnAction(e -> startX = xItems.indexOf(xComboBox.getValue()));

		Text yComboText = new Text("Y start Postion");
		ObservableList<String> yItems = FXCollections.observableArrayList(yCoordinate);
		yComboBox = new ComboBox<>();
		yComboBox.getItems().addAll(yItems);
		yComboBox.setValue("0");
		yComboBox.setOnAction(e -> startY = yItems.indexOf(yComboBox.getValue()));

		VBox vb = new VBox(xComboText,xComboBox,yComboText,yComboBox);
		vb.setAlignment(Pos.CENTER);
		mainPane.setCenter(vb);

		//////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////     RecursiveJFX      ///////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////

		// Recursive route headline text
		Text recHeadLine=new Text();
		recHeadLine.setFont(Font.font ("Verdana",FontWeight.BOLD, 20));
		recHeadLine.setFill(Color.CADETBLUE);
		recHeadLine.setText("Recursive route");
		recHeadLine.setCache(true);
		recHeadLine.setEffect(r);

		// Timeout recursive route  text
		Text timeoutRec=new Text();
		timeoutRec.setFont(Font.font ("Verdana",FontWeight.BOLD, 20));
		timeoutRec.setFill(Color.CADETBLUE);
		timeoutRec.setCache(true);
		timeoutRec.setEffect(r);

		// Chess board recursive
		HBox hbRec = new HBox(chessBoardRec);
		hbRec.setSpacing(2.0);
		hbRec.setAlignment(Pos.CENTER);
		hbRec.setPadding(new Insets(10, 10, 10, 10));

		// Recursive route V box
		VBox v1=new VBox();
		v1.setSpacing(5.0);
		v1.setAlignment(Pos.CENTER);
		v1.setPadding(new Insets(10,10,10,10));
		v1.getChildren().addAll(recHeadLine,hbRec, timeoutRec);
		mainPane.setLeft(v1);

		//////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////     IterativeJFX      ///////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////////////////

		// Iterative route headline text
		Text iterHeadLine=new Text();
		iterHeadLine.setFont(Font.font ("Verdana",FontWeight.BOLD, 20));
		iterHeadLine.setFill(Color.CADETBLUE);
		iterHeadLine.setText("Iterative route");
		iterHeadLine.setCache(true);
		iterHeadLine.setEffect(r);

		// Timeout iterative route text
		Text timeoutIter=new Text();
		timeoutIter.setFont(Font.font ("Verdana",FontWeight.BOLD, 20));
		timeoutIter.setFill(Color.CADETBLUE);
		timeoutIter.setCache(true);
		timeoutIter.setEffect(r);

		// Chess board iterative
		HBox hbIter = new HBox(chessBoardIter);
		hbIter.setSpacing(2.0);
		hbIter.setAlignment(Pos.CENTER);
		hbIter.setPadding(new Insets(10, 10, 10, 10));

		// Iterative route V box
		VBox v2=new VBox();
		v2.setSpacing(5.0);
		v2.setAlignment(Pos.CENTER);
		v2.setPadding(new Insets(10,10,10,10));
		v2.getChildren().addAll(iterHeadLine, hbIter, timeoutIter);
		mainPane.setRight(v2);

		btn.setFont(new Font(30));
		btn.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				freezeButtons();
				Thread iterThread = new Thread(iter);
				Thread recThread = new Thread(rec);

				//Fire threads who handles the tasks.
				iterThread.start();
				recThread.start();


				while (iterThread.isAlive() || recThread.isAlive());

				timeoutRec.setText("Recursive: "+rec.getTime()+" millisec");
				timeoutIter.setText( "Iterative: "+ iter.getTime() +" millisec");		
			};
		});

		HBox hbb = new HBox(btn);
		hbb.setSpacing(5.0);
		hbb.setAlignment(Pos.CENTER);
		mainPane.setBottom(hbb);

		Scene scene = new Scene(mainPane, WIDTH, HEIGHT);
		this.setTitle("Knight's Tour Animation MultiThreading");
		this.setScene(scene);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				threadsButton.setDisable(false);
				sequenceButton.setDisable(false);
				System.out.println("Exit the program.");
			}
		});
	}


	public synchronized void enableButtons(){
		if(!recLock && !iterLock) {
			btn.setDisable(false);
			yComboBox.setDisable(false);
			xComboBox.setDisable(false);
		}
	}

	public synchronized void freezeButtons(){
		btn.setDisable(true);
		yComboBox.setDisable(true);
		xComboBox.setDisable(true);
	}


	class IterativeTask implements Runnable {

		private double before, after;

		@Override
		public void run() {
			//System.out.println("2"); Shows that the tasks are implements as thread (remove 345 also)
			iterLock = true;
			IterativeAlgorithem iter=new IterativeAlgorithem();
			//reset the array list moves history
			iter.resetMoveHistory();
			//add the first move
			iter.addMoveHistory(startX, startY);
			//solve the problem
			this.before=System.currentTimeMillis();
			iter.solvePuzzleIter(startX,startY);
			this.after=System.currentTimeMillis();
			//set the move history to the animation
			chessBoardIter.setMoveHistory(moveHistoryIter);

			//start the animation for the board
			Platform.runLater(new Runnable(){
				public void run() {
					chessBoardIter.startAnimation();
					chessBoardIter.cleanBoard();
					chessBoardIter.animation.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent event) {
							iterLock = false;
							enableButtons();
						}
					});
				}
			});
		}

		private double getTime() {
			return (this.after-this.before);
		}
	}


	/*** This class found the knight's tour in iterative way using Warnsdorff's heuristic algorithm
	 check every square if this square is available and in range.
	 every cell has a 8 max moves by the knight's rules.
	 found the route on the board and draw on the JavaFX board ***/
	class IterativeAlgorithem {
		// Max possible moves for square 
		private final int NUM_OF_MOVES = 8;
		// The all possible moves for square 
		private final int[][] MOVES ={{2,1},{2,-1},{1,2},{1,-2},{-1,2},{-1,-2},{-2,1},{-2,-1}};
		private boolean[][] board;

		public IterativeAlgorithem(){
			board = new boolean[SIZE][SIZE];
		}

		/**
		 * solving this problem using heuristic algorithm
		 */
		private void solvePuzzleIter(int startX, int startY) {
			// filling up the chess board matrix with -1's to know which cell is available and empty
			int[] position = initialPosition(startX, startY);
			// determine the next position for each cell
			for (int i=1; i< SIZE *SIZE; i++){
				//get the points x,y to the next move
				position = nextMove(position);
				//add the moves to the array list moves for JFX animation
				addMoveHistory(position[0], position[1]);
			}
		}

		/**
		 *  Checks whether a square is valid and empty or not
		 * @param x coordinate
		 * @param y coordinate
		 * @return True if the knight didn't pass on the coordinates
		 */
		private boolean isEmpty(int x, int y) {
			return ( x < SIZE  && x >= 0 && y < SIZE   && y >=0  && board[x][y] == false );
		}

		/**
		 *  Returns the number of empty squares adjacent to (x, y) -> the number of onward moves
		 *  @param x - x coordinate
		 * 	@param y - y coordinate
		 */
		private int getAccessibilitySquares(int x, int y) {
			int accessibilitySquares = 0;
			for(int i=0; i < NUM_OF_MOVES ; i++){
				if ( isEmpty(x + MOVES[i][0], y + MOVES[i][1] ))
					accessibilitySquares++;
			}
			return accessibilitySquares;
		}


		/** Set the first position start
		 *
		 * @param startX - x coordinate
		 * @param startY - y coordinate
		 * @return Array with x,y coordinates
		 */
		private int[] initialPosition(int startX, int startY){
			int[] pos = new int[2];
			pos[0] = startX;
			pos[1] = startY;
			board[pos[0]][pos[1]] = true;
			return pos;
		}

		/** Found the next point (the point with the fewest onward moves) 
		 * and returns false if it is not possible to pick next point.
		 */
		private int[] nextMove(int[] pos) {
			int xPos = pos[0];
			int yPos = pos[1];
			int accessibility = NUM_OF_MOVES;

			for (int i=0 ; i< NUM_OF_MOVES ; i++){
				int newX = xPos + MOVES[i][0];
				int newY = yPos + MOVES[i][1];
				int newAccessibility = getAccessibilitySquares(newX, newY);

				// Check if the square is empty and the point is on the range
				if( isEmpty(newX, newY) && newAccessibility < accessibility ){
					pos[0] = newX;
					pos[1] = newY;
					accessibility = newAccessibility;
				}
			}

			board[pos[0]][pos[1]] = true;
			return pos;
		}


		/** 
		 * Reset the history array
		 */
		public void resetMoveHistory() {
			moveHistoryIter = new ArrayList<Point>(63);
		}

		/** Add move to the history array
		 * 
		 * @param x coordinate
		 * @param y coordinate
		 */
		public void addMoveHistory(int x, int y) {
			moveHistoryIter.add(new Point(x, y));
		}
	}

	class RecursiveTask implements Runnable {
		private double before, after;

		@Override
		public void run() {
			//System.out.println("1"); Shows that the tasks are implements as thread (remove 192 also)
			recLock = true;
			RecursiveAlgorithem rec=new RecursiveAlgorithem();
			boolean[][] moves = new boolean[SIZE][SIZE];
			moves[startX][startY] = true;
			//reset the array list moves history
			rec.resetMoveHistory();
			//add the first move
			rec.addMoveHistory(startX, startY);
			//solve the problem
			this.before=System.currentTimeMillis();
			rec.solvePuzzleRec(moves, 1, startX, startY);
			this.after=System.currentTimeMillis();	

			//set the move history to the animation
			chessBoardRec.setMoveHistory(moveHistoryRec);
			//start the animation for the board
			Platform.runLater(new Runnable(){
				public void run() {
					chessBoardRec.cleanBoard();
					chessBoardRec.startAnimation();	
					chessBoardRec.animation.setOnFinished(new EventHandler<ActionEvent>(){
						public void handle(ActionEvent event) {
							recLock = false;
							enableButtons();
						}
					});
				}
			});
		}

		private double getTime() {
			return (this.after-this.before);
		}

	}

	/*** 
	Uses Warnsdorf's heuristic discovered in 1823 that says the best move is the one with the fewest next moves. 
	I found it necessary to back up in only one case (3,0) and choose to try the second best move which worked well. ***/
	class RecursiveAlgorithem {

		private boolean solvePuzzleRec(boolean[][] moves, int numMoves, int x, int y) {
			int nextX = 0;
			int nextY = 0;
			int bestMoveX = 0;
			int bestMoveY = 0;
			int bestMoveX2 = 0;
			int bestMoveY2 = 0;
			int minMoveCount = 8; // Knight has max of 8 moves
			int moveCount = 0;

			for (int i = 2; i >= -2; i += -4) {
				for (int j = 1; j >= -1; j += -2) {
					nextX = x + i;
					nextY = y + j;
					if (nextX >= 0 && nextX <= SIZE - 1 && nextY >= 0 && nextY <= SIZE - 1 && !moves[nextX][nextY]) {
						moveCount = lookAheadCount(moves, nextX, nextY);
						if (moveCount <= minMoveCount) {
							minMoveCount = moveCount;
							bestMoveX2 = bestMoveX;
							bestMoveY2 = bestMoveY;
							bestMoveX = nextX;
							bestMoveY = nextY;
						}
					}

					nextX = x + j;
					nextY = y + i;
					if (nextX >= 0 && nextX <= SIZE - 1 && nextY >= 0 && nextY <= SIZE - 1 && !moves[nextX][nextY]) {
						moveCount = lookAheadCount(moves, nextX, nextY);
						if (moveCount <= minMoveCount) {
							minMoveCount = moveCount;
							bestMoveX2 = bestMoveX;
							bestMoveY2 = bestMoveY;
							bestMoveX = nextX;
							bestMoveY = nextY;
						}
					}
				}
			}
			moves[bestMoveX][bestMoveY] = true;
			addMoveHistory(bestMoveX, bestMoveY);
			numMoves++;
			if (numMoves == (SIZE * SIZE))
				return true;
			if (moveCount > 0 && solvePuzzleRec(moves, numMoves, bestMoveX, bestMoveY)) {
				return true;
			}
			moves[bestMoveX][bestMoveY] = false;
			moves[bestMoveX2][bestMoveY2] = true;
			removeLastMoveHistory();
			addMoveHistory(bestMoveX2, bestMoveY2);
			if (moveCount > 1 && solvePuzzleRec(moves, numMoves, bestMoveX2, bestMoveY2)) {
				return true;
			}
			moves[bestMoveX2][bestMoveY2] = false;
			removeLastMoveHistory();
			numMoves--;
			return false;
		}

		/**
		 * *  
		 * @param moves - Boolean array that indicates which squares the knights passed
		 * @param x = x coordinate
		 * @param y = y coordinate
		 * @return Returns the number of empty squares adjacent to (x, y) -> the number of onward moves
		 */
		private int lookAheadCount(boolean[][] moves, int x, int y) {
			int maxCount = 0;
			int nextX = 0;
			int nextY = 0;
			for (int i = -2; i <= 2; i += 4) {
				for (int j = -1; j <= 1; j += 2) {
					nextX = x + i;
					nextY = y + j;
					if (nextX >= 0 && nextX <= SIZE - 1 && nextY >= 0 && nextY <= SIZE - 1 && !moves[nextX][nextY]) {
						maxCount++;
					}

					nextX = x + j;
					nextY = y + i;
					if (nextX >= 0 && nextX <= SIZE - 1 && nextY >= 0 && nextY <= SIZE - 1 && !moves[nextX][nextY]) {
						maxCount++;
					}
				}
			}
			return maxCount;
		}

		/**
		 * Reset the history array
		 */
		public void resetMoveHistory() {
			moveHistoryRec = new ArrayList<Point>(63);
		}

		/**
		 *  Add move to the history array
		 * @param x coordinate
		 * @param y coordinate
		 */
		public void addMoveHistory(int x, int y) {
			moveHistoryRec.add(new Point(x, y));
		}

		/**
		 * Remove the last move history from array
		 */
		public void removeLastMoveHistory() {
			moveHistoryRec.remove(moveHistoryRec.size() - 1);
		}
	}

	class ChessBoard extends StackPane {
		private int index;
		private BoardPane theBoard;
		private LinesPane lines;
		private Timeline animation ;
		private ArrayList<Point> array;
		private Color lineColor,squareColor;
		private Image image = new Image("file://"+SeminarJFX.class.getClassLoader().getResource("chess.jpg").getPath(),50,50,false,false);
		private ImageView imv = new ImageView();

		/**
		 * @param lineColor - color of the line
		 * @param squareColor - color of the visited squares
		 */
		public ChessBoard(Color lineColor, Color squareColor) {
			this.lineColor=lineColor;
			this.squareColor = squareColor;
			imv.setImage(image);
			//"Bottom pane"
			theBoard = new BoardPane();
			//"Top" pane
			lines = new LinesPane();
			//Animation object which fires event every 1000 milliseconds. the paintEventHandelr is handle the event each second.
			animation = new Timeline(new KeyFrame(Duration.millis(1000), paintEventHandler));
			this.getChildren().addAll(theBoard, lines);
		}

		//This Event handler is getting activate each second in the time line animation object.
		private EventHandler<ActionEvent> paintEventHandler = new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (array != null && array.size() > 0) {
					if (index == 0){
						theBoard.squareArr[startX][startY].setImageView();
						Point p1 = (Point) array.get(0);
						theBoard.squareArr[(int) p1.getX()][(int) p1.getY()].getRect().setFill(Color.rgb(82, 86, 88));
						index++;
					}
					else{
						Point p1 = (Point) array.get(index - 1);
						Point p2 = (Point) array.get(index);
						lines.addLine((p1.getY() * (getWidth() / SIZE)) + (getWidth() / SIZE / 2),
								(p1.getX() * (getHeight() / SIZE)) + (getHeight() / SIZE / 2),
								(p2.getY() * (getWidth() / SIZE)) + (getWidth() / SIZE / 2),
								(p2.getX() * (getHeight() / SIZE)) + (getHeight() / SIZE / 2), 
								lineColor);
						if(index != ((SIZE*SIZE)-1)){
							theBoard.squareArr[(int) p2.getX()][(int) p2.getY()].getRect().setFill(squareColor);
						}
						else{
							theBoard.squareArr[(int) p2.getX()][(int) p2.getY()].getRect().setFill(Color.rgb(82, 86, 88)); 
							theBoard.setLastSquare((int) p2.getX(),(int) p2.getY());
						}
						theBoard.squareArr[(int) p2.getX()][(int) p2.getY()].removeImage();
						theBoard.squareArr[(int) p2.getX()][(int) p2.getY()].setImageView();
						index++;
					}	
				}
			}
		};


		public void cleanBoard() {
			lines.clear();
			theBoard.cleanSquares();
		}

		/**Initialize the moves of the knight on the board
		 * 
		 * @param arr - Array of Points
		 */
		public void setMoveHistory(ArrayList<Point> arr) {
			array=new ArrayList<>();
			array.addAll(arr);
		}

		/**
		 * start painting the moves of the knight on the board
		 */
		public void startAnimation() {
			if (animation.getStatus() == Animation.Status.RUNNING)
				animation.stop();
			index = 0;
			//Makes the animations to activate SIZE*SIZE times
			animation.setCycleCount(SIZE * SIZE);
			animation.playFromStart();
		}

		/***
		 * The "bottom" pane -> responsible for drawing the chess board
		 */
		class BoardPane extends GridPane {
			private BoxStack[][] squareArr = new BoxStack[SIZE][SIZE];
			private int lastX,lastY;


			public BoardPane() {
				for (int i = 0; i < SIZE; i++) {
					for (int j = 0; j < SIZE; j++) {
						if ((i+j)%2==0) {
							squareArr[i][j] = new BoxStack(Color.ANTIQUEWHITE);
						}
						else {
							squareArr[i][j] = new BoxStack(Color.rgb(115, 77, 61));
						}		
						this.add(squareArr[i][j], j, i);
					}
				}
			}

			public void setLastSquare(int x, int y){
				lastX = x;
				lastY = y;
			}

			public void cleanSquares(){
				for (int i = 0; i < SIZE; i++) {
					for (int j = 0; j < SIZE; j++) {
						if ((i+j)%2==0) {
							squareArr[i][j].getRect().setFill(Color.ANTIQUEWHITE);
						}
						else {
							squareArr[i][j].getRect().setFill(Color.rgb(115, 77, 61));
						}	
					}
				}
				squareArr[lastX][lastY].removeImage();
			}

			class BoxStack extends StackPane{
				private Rectangle rect;

				public BoxStack(Color color){
					rect = new Rectangle((WIDTH * 0.4) / SIZE, (HEIGHT * 0.6) / SIZE);
					rect.setFill(color);
					rect.setStroke(Color.BLACK);
					this.getChildren().add(rect);
				}	

				public Rectangle getRect(){
					return rect;
				}

				public void setImageView(){
					this.getChildren().add(imv);
				}

				public void removeImage(){
					this.getChildren().remove(imv);
				}
			}
		}

		/***
		 * The "Top" pane -> draw lines (the moves of the knight) above the "bottom" pane, chess board
		 */
		class LinesPane extends Pane {

			//drawing a line on the chess board
			public void addLine(double xStart, double yStart, double xEnd, double yEnd, Color c) {
				Line currentLine = new Line(xStart, yStart, xEnd, yEnd);
				currentLine.setStroke(c);
				currentLine.setStrokeWidth(3);
				this.getChildren().add(currentLine);
			}

			public void clear() {
				//the clear method has been call from not java FX thread ===>
				//Platform.runlater in use because the program can't to update the GUI in not JFX thread.
				Platform.runLater(new Runnable(){
					public void run() {
						getChildren().clear();
					}
				});
			}
		}
	}
}
