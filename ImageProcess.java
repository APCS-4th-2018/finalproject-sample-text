import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

/**
 * Whole process of processing images.
 * Uses <code>OpenCV</code> vesion 4.1.0
 *
 * @author Alex Liu
 * @version 1.0
 */
public class ImageProcess
{
    private VideoCapture cap;
    private Classifier faceClassifier;
    private Classifier eyeClassifier;
    private Replacer faceReplacer;
    private Replacer eyeReplacer;

    /**
     * Construct a new <code>ImageProcess</code>
     *
     * @param vidIndex Index of video capture device.
     * @param face Face cascade classifier.
     * @param eye Eye cascade classifier.
     * @param faceImg Face image replacement.
     * @param faceImgMask Face image replacement mask.
     * @param eyeImg Eye image replacement.
     * @param eyeImgMask Eye image replacement mask.
     */
    public ImageProcess(int vidIndex, String face, String eye, Mat faceImg, Mat faceImgMask, Mat eyeImg, Mat eyeImgMask)
    {
        cap = new VideoCapture(vidIndex);
        faceClassifier = new Classifier(face);
        eyeClassifier = new Classifier(eye);
        faceReplacer = new Replacer(faceImg, faceImgMask);
        eyeReplacer = new Replacer(eyeImg, eyeImgMask);
    }

    /**
     * Change the vidCap index.
     *
     * @param vidIndex New index to change vidCap to.
     */
    public void changeVidCap(int vidIndex)
    {
        // Release if open
        if (cap.isOpened())
            cap.release();

        // Reopen with `vidIndex`
        cap.open(vidIndex);
    }

    /**
     * Change the face replacer.
     *
     * @param face New image to replace with.
     * @param faceMask Alpha mask.
     */
    public void setFaceReplacer(Mat face, Mat faceMask)
    {
        faceReplacer = new Replacer(face, faceMask);
    }

    /**
     * Disable the face replacer.
     */
    public void setFaceReplacer()
    {
        faceReplacer = null;
    }

    /**
     * Change the eye replacer.
     *
     * @param eye New image to replace with.
     * @param eyeMask Alpha mask.
     */
    public void setEyeReplacer(Mat eye, Mat eyeMask)
    {
        eyeReplacer = new Replacer(eye, eyeMask);
    }

    /**
     * Disable the eye replacer.
     */
    public void setEyeReplacer()
    {
        eyeReplacer = null;
    }

    /**
     * Exit gracefully.
     */
    public void close()
    {
        cap.release();
    }

    public Image takeFrame()
    {
        Image output = null;

        if (cap.isOpened())
        {
            // take a picture
            Mat frame = new Mat();
            cap.read(frame);

            // process face
            Rect[] faces = processFace(frame);

            // process eye
            processEye(frame, faces);

            // Convert to `Image`
            MatOfByte buffer = new MatOfByte();
            Imgcodecs.imencode(".png", frame, buffer);
            output = new Image(new ByteArrayInputStream(buffer.toArray()));
        }

        return output;
    }

    private Rect[] processFace(Mat frame)
    {
        Rect[] faces = faceClassifier.findAll(frame, 1.3, 5);

        if (faceReplacer != null)
            for (Rect face: faces)
                faceReplacer.replace(frame, face);

        return faces;
    }

    private void processEye(Mat frame, Rect[] faces)
    {
        Rect[] eyes = eyeClassifier.findAll(frame, 2.0, 1);

        if (eyeReplacer != null)
            for (Rect eye: eyes)
                eyeReplacer.replace(frame, eye);
    }
}
