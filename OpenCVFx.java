
/**
 * Write a description of class GUI here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private ImageProcess imgs;
    private ImageView img;
    private static ImageProcess camera;
    private ScheduledExecutorService timer;
    
    @Override
    public void start(Stage stage)
    {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(8);
        grid.setVgap(8);

        ImageView img = new ImageView();
        img.setX(50);
        img.setY(25);
        img.setFitHeight(455);
        img.setFitWidth(500);
        img.setPreserveRatio(true);
        
        //Default eye buttons (replace prints with uploading image to window
        Button googly = new Button("Googly Eyes");
        googly.setOnAction(e -> System.out.println("googly"));
        Button monocle = new Button("Monocle");
        monocle.setOnAction(e -> System.out.println("mono"));
        Button money = new Button ("Money");
        money.setOnAction(e -> System.out.println("money"));
        Button stars = new Button ("Stars");
        stars.setOnAction(e -> System.out.println("stars"));
        Button hearts = new Button ("Hearts");
        hearts.setOnAction(e -> System.out.println("hearts"));
        Button tears = new Button ("Tears");
        tears.setOnAction(e -> System.out.println("tears"));
        
        //Default head buttons
        Button Lantsberger = new Button("Lantsberger");
        Lantsberger.setOnAction(e -> System.out.println("teach"));
        Button walrus = new Button("Walrus");
        walrus.setOnAction(e -> System.out.println("walrus"));
        Button tophat = new Button("Top Hat");
        tophat.setOnAction(e -> System.out.println("hat"));
        Button stormCloud = new Button("Storm Cloud");
        stormCloud.setOnAction(e -> System.out.println("storm cloud"));
        Button duckBeak = new Button("Duck beak");
        duckBeak.setOnAction(e -> System.out.println("quack"));
        Button arnoldSchwarzenegger = new Button("Arnold Schwarzenegger");
        arnoldSchwarzenegger.setOnAction(e -> System.out.println("arnold"));
        Button rageFace = new Button("Rage face");
        rageFace.setOnAction(e -> System.out.println("rage"));
        Button harold = new Button("Harold");
        harold.setOnAction(e -> System.out.println("harold"));

        //Top buttons
        Button button1 = new Button("Clear window");
        button1.setOnAction(e -> System.out.println("Clear"));
        grid.add(button1, 0, 0, 2, 1);

        Button button2 = new Button("Upload image to window");
        button1.setOnAction(e -> System.out.println("Upload"));
        grid.add(button2, 0, 1, 2, 1);
        
        //Image
        grid.add(img, 2, 0, 8, 7);
        
        //Default images
        Label defaultopts = new Label("Default");
        grid.add(defaultopts, 0, 2, 1, 2);
        
        Label eyes = new Label("Eyes: ");
        grid.add(eyes, 0, 3);
        
        ChoiceBox<Button> defaultEyes = new ChoiceBox<>();
        defaultEyes.getItems().addAll(googly, monocle, money,
                                      stars, hearts, tears);
        grid.add(defaultEyes, 1, 3);
        
        Label head = new Label("Head: ");
        grid.add(head, 0, 4);
        
        ChoiceBox<Button> defaultHead = new ChoiceBox<>();
        defaultHead.getItems().addAll(Lantsberger, walrus, tophat,
                                      stormCloud, duckBeak, 
                                      arnoldSchwarzenegger, rageFace,
                                      harold);
        grid.add(defaultHead, 1, 4);
        
        //Custom images
        Label custom = new Label("Custom: ");
        grid.add(custom, 0, 5, 1, 2);
        
        Label eyes2 = new Label("Eyes: ");
        grid.add(eyes2, 0, 6);
        
        ChoiceBox<Button> customEyes = new ChoiceBox<>();
        Button eyeOption = new Button("Custom");
        customEyes.setOnAction(e -> System.out.print("insert upload code"));
        customEyes.getItems().addAll(eyeOption);
        grid.add(customEyes, 1, 6);
        
        Label head2 = new Label("Head: ");
        grid.add(head2, 0, 7);
        
        ChoiceBox<Button> customHead = new ChoiceBox<>();
        Button headOption = new Button("Custom");
        customHead.setOnAction(e -> System.out.print("insert upload code"));
        customHead.getItems().addAll(headOption);
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
        imgs.close();
        timer.shutdown();
    }
    
    private void capture()
    {
        img.setImage(camera.takeFrame());
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