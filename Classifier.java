import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Classifer class to find regions of intrest in images based on haar cacades.
 * Uses <code>OpenCV</code> vesion 4.1.0
 *
 * @author Alex Liu
 * @version 1.0
 */
public class Classifier
{
    private CascadeClassifier cascade;

    /**
     * <code>Classifier</code> Constructor with path to a cascade as a parameter.
     *
     * @param haarCascade Path to a cascade to use.
     */
    public Classifier(String haarCascade)
    {
        cascade = new CascadeClassifier(haarCascade);
    }

    /**
     * Find all the matches in a frame.
     *
     * @param frame Image to search in.
     * @param scaleFactor How much image size is reduced at each image scale.
     * @param minNeighbors How many neighbors each candidate rectangle should have
     * @return Array of <code>Rect</code> containing all matches.
     */
    public Rect[] findAll(Mat frame, double scaleFactor, int minNeighbors)
    {
        // Create a b/w version if applicable
        Mat gray = frame.clone();
        if (gray.channels() == 3)
            Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);

        // Maximize contrast for easier detection
        Imgproc.equalizeHist(gray, gray);

        // Find the matches
        MatOfRect matches = new MatOfRect();
        cascade.detectMultiScale(gray, matches, scaleFactor, minNeighbors);

        // Return the array
        return matches.toArray();
    }

    /**
     * Find all the matches in a frame. Default settings at scaleFactor=1.3, minNeighbors=5.
     *
     * @param frame Image to search in.
     * @return Array of <code>Rect</code> containing all matches.
     */
    public Rect[] findAll(Mat frame)
    {
        return this.findAll(frame, 1.3, 5);
    }
}