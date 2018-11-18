import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainProject extends Application{

	private static final int WIDTH = 600;
	private static final int HEIGHT = 400;
	private BackgroundImage bi;
	private Image image;
	private Button threadsButton;
	private Button sequenceButton;
	private SeminarJFX seminarJFX;
	private SeminarJFXMultiThreading seminarJFXMultiThreading;

	public void start(Stage primaryStage) throws Exception {
		image = new Image("file://"+MainProject.class.getClassLoader().getResource("entrance.jpeg").getPath());
		bi = new BackgroundImage(image, BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

		Text headLine=new Text();
		headLine.setText("Please choose which algorithem you want to run");
		headLine.setFont(Font.font ("Verdana",FontWeight.BOLD, 20));
		headLine.setFill(Color.BLACK);
		headLine.setCache(true);
		Reflection r = new Reflection();
		r.setFraction(0.7f);
		headLine.setEffect(r);

		HBox hbHeadLine = new HBox(headLine);
		hbHeadLine.setSpacing(5.0);
		hbHeadLine.setAlignment(Pos.CENTER);
		hbHeadLine.setPadding(new Insets(5, 0, 20, 0));

		threadsButton = new Button("Launch with MultiThreading");
		threadsButton.setPrefSize(250,50);

		sequenceButton = new Button("Launch without MultiThreading");
		sequenceButton.setPrefSize(250,50);

		HBox hb = new HBox();
		hb.setSpacing(5.0);
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(threadsButton,sequenceButton);

		BorderPane entrancePane = new BorderPane();
		entrancePane.setBackground(new Background(bi));
		entrancePane.setCenter(hb);
		entrancePane.setTop(hbHeadLine);

		Scene scene = new Scene(entrancePane,WIDTH,HEIGHT);
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.setTitle("Knight's Tour"); // Set the stage title
		primaryStage.show(); // Display the stage
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setResizable(false);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				System.out.println("Exit the program.");
				System.exit(0);
			}
		});
		setButtonsAction();
	}

	public void setButtonsAction(){
		threadsButton.setOnAction(e->{
			seminarJFXMultiThreading = new SeminarJFXMultiThreading(threadsButton, sequenceButton);
			seminarJFXMultiThreading.show();
			freezeButtons();
		});

		sequenceButton.setOnAction(e->{
			seminarJFX = new SeminarJFX(threadsButton, sequenceButton);
			seminarJFX.show();
			freezeButtons();
		});
	}

	public void freezeButtons(){
		threadsButton.setDisable(true);
		sequenceButton.setDisable(true);
	}

	public static void main(String[] args){
		launch(args);
	}
}
