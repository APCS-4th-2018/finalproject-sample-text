import org.opencv.core.CvType;
import org.opencv.core.Mat;
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
}
