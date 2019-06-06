import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GraphicInterface extends Application
{
    private static ImageProcess camera;
    private ScheduledExecutorService timer;
    private ImageView display;

    @Override
    public void start(Stage stage)
    {
        // Create image view and position it
        display = new ImageView();

        display.setX(50);
        display.setY(25);
        display.setFitHeight(455);
        display.setFitWidth(500);
        display.setPreserveRatio(true);

        // Start frame capture
        timer = Executors.newSingleThreadScheduledExecutor();
        timer.scheduleAtFixedRate(this::capture, 0, 33, TimeUnit.MILLISECONDS);

        // Simple GUI organization
        Group root = new Group(display);

        // Create scene
        Scene scene = new Scene(root, 600, 500);

        stage.setTitle("Graphic Interface");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop()
    {
        // Gracefully close the camera
        camera.close();
        timer.shutdown();
    }

    private void capture()
    {
        display.setImage(camera.takeFrame());
    }

    public static void main(String[] args)
    {
        // Load OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // Construct camera
        Mat eye, eyemask, face, facemask;
        Mat[] temp;

        temp = ImageProcess.loadImage("eyes.png");
        eye = temp[0];
        eyemask = temp[1];

        temp = ImageProcess.loadImage("face.png");
        face = temp[0];
        facemask = temp[1];

        camera = new ImageProcess(1,
                "haarcascade_frontalface_default.xml",
                "haarcascade_eye.xml",
                face, facemask, eye, eyemask);

        launch(args);
    }
}
