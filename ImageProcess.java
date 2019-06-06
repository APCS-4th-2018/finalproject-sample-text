import java.util.Vector;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import org.opencv.core.*;
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
     * Construct a new <code>ImageProcess</code> without replacements.
     *
     * @param vidIndex Index of video capture device.
     * @param face Face cascade classifier.
     * @param eye Eye cascade classifier.
     */
    public ImageProcess(int vidIndex, String face, String eye)
    {
        cap = new VideoCapture(vidIndex);
        faceClassifier = new Classifier(face);
        eyeClassifier = new Classifier(eye);
        faceReplacer = null;
        eyeReplacer = null;
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

    /**
     * Take a frame, process it, and return it as an <code>Image</code>
     *
     * @return <code>Image</code> object with the output frame.
     */
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
                try { faceReplacer.replace(frame, face);} catch (Exception e){ }

        return faces;
    }

    private void processEye(Mat frame, Rect[] faces)
    {
        Rect[] eyes = eyeClassifier.findAll(frame, 2.0, 1);

        if (eyeReplacer != null)
            for (Rect eye: eyes)
                try { eyeReplacer.replace(frame, eye);} catch (Exception e){ }
    }

    /**
     * Image load helper.
     *
     * @param filename Filename of image to load.
     * @return Array of 2 <code>Mat</code>'s. Image and alpha mask.
     */
    public static Mat[] loadImage(String filename)
    {
        Mat[] output = new Mat[2];

        // Load the image
        Mat img = Imgcodecs.imread(filename, Imgcodecs.IMREAD_UNCHANGED);

        // Generate the mask
        Mat mask;
        if (img.channels() == 4)
        {
            // split the image
            Vector<Mat> rgba = new Vector<Mat>();
            Core.split(img, rgba);

            // get the alpha channel
            mask = rgba.remove(3);

            // merge the image
            Core.merge(rgba, img);
        }
        else
            mask = Mat.ones(img.rows(), img.cols(), CvType.CV_8UC1);

        output[0] = img;
        output[1] = mask;

        return output;
    }
}
