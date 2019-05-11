import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Replace regions of interest with an image.
 * Uses <code>OpenCV</code> vesion 4.1.0
 *
 * @author Alex Liu
 * @version 1.0
 */
public class Replacer
{
    private Mat replacement;
    private Mat mask;

    /**
     * Constructor with replacement image and mask.
     *
     * @param myReplacement Replacement image.
     * @param myMask Alpha mask of replacement image.
     */
    public Replacer(Mat myReplacement, Mat myMask)
    {
        // Force mask to grayscale
        if (myMask.channels() == 3)
            Imgproc.cvtColor(myMask, myMask, Imgproc.COLOR_BGR2GRAY);

        replacement = myReplacement;
        mask = myMask;
    }

    /**
     * Constructor with just replacement image. Mask is automatically generated.
     *
     * @param myReplacement Replacement image.
     */
    public Replacer(Mat myReplacement)
    {
        // Create a blank mask
        mask = Mat.ones(myReplacement.rows(), myReplacement.cols(), CvType.CV_8UC1);
        replacement = myReplacement;
    }

    /**
     * Replace a region of intrest with another image.
     *
     * @param frame Frame in question. Frame is modified.
     * @param roi Region to be replaced.
     */
    public void replace(Mat frame, Rect roi)
    {
        // Create a sub frame
        Mat submat = frame.submat(roi);

        // Create a modifiable image
        Mat img = replacement.clone();
        Mat alpha = mask.clone();

        // Resize the replacement
        Imgproc.resize(img, img, new Size(roi.width, roi.height));
        Imgproc.resize(alpha, alpha, new Size(roi.width, roi.height));

        // Copy with mask
        img.copyTo(submat, alpha);
    }
}
