
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
        ImageView img = new ImageView();
        GridPane grid = new GridPane();
        Button button, button2;
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(61.25);
        grid.setVgap(65.55);
        
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
        button = new Button("Clear window");
        button.setOnAction(e -> System.out.println("Clear"));
        //replace print statement above with removing img from window
        button2 = new Button("Upload image to window");
        button.setOnAction(e -> System.out.println("Upload"));
        //replace print statement above with uploading img to window
        GridPane.setConstraints(button, 0, 0, 0, 1);
        GridPane.setConstraints(button2, 1, 0, 0, 1);
        
        //Image
        GridPane.setConstraints(img, 0, 2, 8, 7);
        
        //Default images
        TextField defaultopts = new TextField("Default");
        GridPane.setConstraints(defaultopts, 2, 0, 0, 2);
        
        TextField eyes = new TextField("Eyes: ");
        GridPane.setConstraints(eyes, 3, 0);
        
        ChoiceBox<Button> defaultEyes = new ChoiceBox<>();
        defaultEyes.getItems().addAll(googly, monocle, money,
                                      stars, hearts, tears);
        GridPane.setConstraints(defaultEyes, 3, 1);
        
        TextField head = new TextField("Head: ");
        GridPane.setConstraints(head, 4, 0);
        
        ChoiceBox<Button> defaultHead = new ChoiceBox<>();
        defaultHead.getItems().addAll(Lantsberger, walrus, tophat,
                                      stormCloud, duckBeak, 
                                      arnoldSchwarzenegger, rageFace,
                                      harold);
        GridPane.setConstraints(defaultHead, 4, 1);
        
        //Custom images
        TextField custom = new TextField("Custom: ");
        GridPane.setConstraints(custom, 5, 0, 0, 2);
        
        TextField eyes2 = new TextField("Eyes: ");
        GridPane.setConstraints(eyes2, 6, 0);
        
        ChoiceBox<Button> customEyes = new ChoiceBox<>();
        Button eyeOption = new Button("Custom");
        customEyes.setOnAction(e -> System.out.print("insert upload code"));
        customEyes.getItems().addAll(eyeOption);
        GridPane.setConstraints(customEyes, 6, 1);
        
        TextField head2 = new TextField("Head: ");
        GridPane.setConstraints(head2, 7, 0);
        
        ChoiceBox<Button> customHead = new ChoiceBox<>();
        Button headOption = new Button("Custom");
        customHead.setOnAction(e -> System.out.print("insert upload code"));
        customHead.getItems().addAll(headOption);
        GridPane.setConstraints(customHead, 7, 1);
        
        grid.getChildren().addAll(button, button2, img, defaultopts,
                                  eyes, defaultEyes, head, defaultHead,
                                  custom, eyes2, customEyes, head2,
                                  customHead);
        
        img.setX(50);
        img.setY(25);
        img.setFitHeight(455);
        img.setFitWidth(500);
        img.setPreserveRatio(true);

        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::capture, 0, 33, TimeUnit.MILLISECONDS);

        Group root = new Group(img);
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
        Mat eye, eyemask, face, facemask;
        Mat[] temp;
        
        temp = ImageProcess.loadImage("eyes.png");
        eye = temp[0];
        eyemask = temp[1];
        
        temp = ImageProcess.loadImage("face.png");
        face = temp[0];
        facemask = temp[1];
        camera = new ImageProcess(1, "haarcascade_frontalface_default.xml",
                                  "haarcascade_eye.xml",
                                  face, facemask, eye, eyemask);
        
        
        launch(args);
    }
    
}