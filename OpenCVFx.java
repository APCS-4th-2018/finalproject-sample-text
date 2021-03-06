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
import javafx.stage.FileChooser;
import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * GUI For the budget snapchat filter.
 *
 * @author Darrin Yun
 * @version 2.0
 */
public class OpenCVFx extends Application
{
    private static final String[] EYEIMGS = {"imgs/googly.png", "imgs/monocle.png"};        // default images for eyes
    private static final String[] FACEIMGS = {"imgs/lantsberger.png", "imgs/clippy.png"};   // default images for head
    private ImageView img;
    private static ImageProcess camera;
    private ScheduledExecutorService timer;
    
    @Override
    public void start(Stage stage)
    {
        // Create a GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));    // creates a border of 10
        //splits the grid by 8
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
        grid.add(img, 2, 0, 8, 7);  // add image window to grid

        // Default label
        Label defaultopts = new Label("Default");
        grid.add(defaultopts, 0, 2, 1, 2);

        // Default label for eyes
        Label eyes = new Label("Eyes: ");
        grid.add(eyes, 0, 3);

        // Create a dropdown menu for eyes
        ChoiceBox<String> defaultEyes = new ChoiceBox<String>();
        defaultEyes.getItems().addAll("Googly Eyes", "Monocle");
        defaultEyes.getSelectionModel().selectedIndexProperty().addListener(this::chooseEye);
        grid.add(defaultEyes, 1, 3);

        //Default label for head
        Label head = new Label("Head: ");
        grid.add(head, 0, 4);

        // Dropdown menu for head
        ChoiceBox<String> defaultHead = new ChoiceBox<String>();
        defaultHead.getItems().addAll("Lantsberger", "Clippy");
        defaultHead.getSelectionModel().selectedIndexProperty().addListener(this::chooseHead);
        grid.add(defaultHead, 1, 4);

        // Top buttons
        Button button1 = new Button("Clear window");
        button1.setOnAction(e -> {
            camera.setEyeReplacer();
            camera.setFaceReplacer();
            defaultEyes.getSelectionModel().clearSelection();
            defaultHead.getSelectionModel().clearSelection();
        });
        grid.add(button1, 0, 0, 2, 1);
        
        // Custom images
        FileChooser fileChooser = new FileChooser();    // file explorer
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Label for custom
        Label custom = new Label("Custom: ");
        grid.add(custom, 0, 5, 1, 2);

        // Allows user to navigate file explorer to upload an image.
        Button customEye = new Button("Eye");
        customEye.setOnAction(e ->
        {
            fileChooser.setTitle("View Pictures");
            File output = fileChooser.showOpenDialog(stage);

            if (output != null)
            {
                String customEyeDir = output.toString();     // store file directory into string

                //load image
                Mat[] eye = ImageProcess.loadImage(customEyeDir);
                camera.setEyeReplacer(eye[0], eye[1]);
            }
        });
        grid.add(customEye, 0, 6);

        // User adds an image to replace head with
        Button customHead = new Button("Head");
        customHead.setOnAction(e ->
        {
            fileChooser.setTitle("View Pictures");
            File output = fileChooser.showOpenDialog(stage);

            if (output != null)
            {
                String customHeadDir = output.toString();

                Mat[] face = ImageProcess.loadImage(customHeadDir);
                camera.setFaceReplacer(face[0], face[1]);
            }
        });
        grid.add(customHead, 0, 7);

        // Frame grabber
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
        if (newValue.intValue() != -1)
        {
            Mat[] eye = ImageProcess.loadImage(EYEIMGS[newValue.intValue()]);
            camera.setEyeReplacer(eye[0], eye[1]);
        }
    }

    private void chooseHead(ObservableValue ov, Number value, Number newValue)
    {
        if (newValue.intValue() != -1)
        {
            Mat[] face = ImageProcess.loadImage(FACEIMGS[newValue.intValue()]);
            camera.setFaceReplacer(face[0], face[1]);
        }
    }

    public static void main(String[] args)
    {
        // Load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Load Camera
        camera = new ImageProcess(1, "haarcascade_frontalface_default.xml", "haarcascade_eye.xml");
        
        launch(args);
    }
    
}