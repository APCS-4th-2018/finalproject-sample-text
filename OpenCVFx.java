
/**
 * Write a description of class GUI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ChoiceBox;
import javafx.geometry.Insets;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OpenCVFx extends Application
{
    private ImageView img;
    private static ImageProcess camera;
    private ScheduledExecutorService timer;
    
    @Override
    public void start(Stage stage)
    {
        // Create a GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(8);
        grid.setVgap(8);

        // Force column sizing
        ColumnConstraints cols = new ColumnConstraints();
        cols.setPercentWidth(10);

        RowConstraints rows = new RowConstraints();
        rows.setPercentHeight(12);

        for (int c = 0; c < 10; c++)
            grid.getColumnConstraints().add(cols);
        for (int r = 0; r < 8; r++)
            grid.getRowConstraints().add(rows);

        // Create the display image
        img = new ImageView();
        img.setX(50);
        img.setY(25);
        img.setFitHeight(455);
        img.setFitWidth(500);
        img.setPreserveRatio(true);

        grid.add(img, 2, 0, 8, 7);

        // Top buttons
        Button button1 = new Button("Clear window");
        button1.setOnAction(e -> System.out.println("Clear"));
        grid.add(button1, 0, 0, 2, 1);

        Button button2 = new Button("Upload image to window");
        button1.setOnAction(e -> System.out.println("Upload"));
        grid.add(button2, 0, 1, 2, 1);
        
        // Default images
        Label defaultopts = new Label("Default");
        grid.add(defaultopts, 0, 2, 1, 2);
        
        Label eyes = new Label("Eyes: ");
        grid.add(eyes, 0, 3);
        
        ChoiceBox<String> defaultEyes = new ChoiceBox<String>();
        defaultEyes.getItems().addAll("Googly Eyes", "Monocole", "Money", "Stars", "Hearts", "Tears");
        defaultEyes.getSelectionModel().selectedIndexProperty().addListener(this::chooseEye);
        grid.add(defaultEyes, 1, 3);
        
        Label head = new Label("Head: ");
        grid.add(head, 0, 4);
        
        ChoiceBox<String> defaultHead = new ChoiceBox<String>();
        defaultHead.getItems().addAll("Lantsberger", "Walrus", "Top Hat", "Stars", "Storm Clouds",
                "Duck Beak", "Arnold Schwarznegger", "Rage Face", "Me Gusta", "Harold", "Clippy");
        defaultHead.getSelectionModel().selectedIndexProperty().addListener(this::chooseHead);
        grid.add(defaultHead, 1, 4);
        
        // Custom images
        Label custom = new Label("Custom: ");
        grid.add(custom, 0, 5, 1, 2);
        
        Label eyes2 = new Label("Eyes: ");
        grid.add(eyes2, 0, 6);
        
        Button customEye = new Button("Custom");
        customEye.setOnAction(e -> System.out.println("Custom Eye"));
        grid.add(customEye, 1, 6);
        
        Label head2 = new Label("Head: ");
        grid.add(head2, 0, 7);
        
        Button customHead = new Button("Custom");
        customHead.setOnAction(e -> System.out.println("Custom Head"));
        grid.add(customHead, 1, 7);

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::capture, 100, 33, TimeUnit.MILLISECONDS);

        Scene scene = new Scene(grid, 600, 500);
        
        stage.setTitle("Budget Snapchat Filter");
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop()
    {
        camera.close();
        timer.shutdown();
    }
    
    private void capture()
    {
        img.setImage(camera.takeFrame());
    }

    private void chooseEye(ObservableValue ov, Number value, Number newValue)
    {
        System.out.println(newValue);
    }

    private void chooseHead(ObservableValue ov, Number value, Number newValue)
    {
        System.out.println(newValue);
    }

    public static void main(String[] args)
    {
        // Load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat eye, eyemask, face, facemask;
        Mat[] temp;
        
        temp = ImageProcess.loadImage("eyes.png");
        eye = temp[0];
        eyemask = temp[1];
        
        temp = ImageProcess.loadImage("face.png");
        face = temp[0];
        facemask = temp[1];
        camera = new ImageProcess(0, "haarcascade_frontalface_default.xml",
                                  "haarcascade_eye.xml",
                                  face, facemask, eye, eyemask);
        
        
        launch(args);
    }
    
}