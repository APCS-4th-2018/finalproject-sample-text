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
}