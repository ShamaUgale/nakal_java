package com.nakal.ScreenExecutor;

import com.nakal.capturescreen.ScreenShooter;
import com.nakal.imageutil.ImageUtil;
import org.im4java.core.IM4JavaException;

import java.io.File;
import java.io.IOException;

/**
 * Created by saikrisv on 22/02/16.
 */
public class NakalExecutor extends ScreenShooter {

    ImageUtil imageUtil = new ImageUtil();
    public File file;
    /**
     * @param baseLineImageName
     * @return true if the images are similar else return false if not similar
     */
    public boolean nakalExecutorCompareScreen(String baseLineImageName) {
        String expectedImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/baseline_images/" + baseLineImageName + ".png";
        if (System.getenv("NAKAL_MODE").equalsIgnoreCase("build")) {
            screenCapture(baseLineImageName,expectedImage);
            return true;
        } else if (System.getenv("NAKAL_MODE").equalsIgnoreCase("compare")) {
            try {
                String actualImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/actual_images/actual_" + baseLineImageName + ".png";
                screenCapture("actual_" + baseLineImageName, actualImage);
                return imageUtil.compareImages(expectedImage, actualImage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IM4JavaException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(null);
    }

    /**
     * @param baseLineImageName
     * @return false if actual and expected images are not similar and generate a difference Image
     */
    public boolean nakalExecutorCompareScreenAndCreateDiffImage(String baseLineImageName) {
        String expectedImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/baseline_images/" + baseLineImageName + ".png";
        String maskImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/mask_images/" + System.getenv("MASKIMAGE") + ".png";
        String maskedExpectedImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/baseline_images/"+"masked_"+baseLineImageName + ".png";
        if (System.getenv("NAKAL_MODE").equalsIgnoreCase("build")) {
            screenCapture(baseLineImageName, expectedImage);
            try {
                //Cropped the notification bar and create a maskImage to compare
                imageUtil.maskImage(expectedImage,maskImage,maskedExpectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IM4JavaException e) {
                e.printStackTrace();
            }
            return true;
        } else if (System.getenv("NAKAL_MODE").equalsIgnoreCase("compare")) {
            try {
                String actualImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/actual_images/actual_" + baseLineImageName + ".png";
                String diffImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/actual_images/diff_" + baseLineImageName + ".png";
                String mergedDiffImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/actual_images/difference_" + baseLineImageName + ".png";
                String maskedActualImage = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/actual_images/"+"masked_"+baseLineImageName + ".png";
                screenCapture("actual_" + baseLineImageName, actualImage);
                try {
                    //Cropped the notification bar and create a maskImage to compare
                    imageUtil.maskImage(actualImage,maskImage,maskedActualImage);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IM4JavaException e) {
                    e.printStackTrace();
                }

                if(imageUtil.compareImages(maskedExpectedImage, maskedActualImage, diffImage) == true){
                    return true;
                }else{
                    imageUtil.mergeImagesHorizontally(maskedExpectedImage,maskedActualImage,diffImage,mergedDiffImage);
                    file= new File(diffImage);
                    file.delete();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IM4JavaException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(null);
    }


    /**
     * @param baseLineImageName
     * @param expectedImage
     * @param actualImage
     * @param pixelDifference
     * @return true of the pixel difference btw the actual and expected images are with the range
     */
    public boolean nakalExecutorCompareScreen(String baseLineImageName, String expectedImage, String actualImage, int pixelDifference) {
        if (System.getenv("NAKAL_MODE").equalsIgnoreCase("build")) {
            String imagePath = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/baseline_images/" + baseLineImageName + ".png";
            screenCapture(baseLineImageName, imagePath);
            return true;
        } else if (System.getenv("NAKAL_MODE").equalsIgnoreCase("compare")) {
            try {
                String imagePath = System.getProperty("user.dir") + "/" + System.getenv("PLATFORM") + "/actual_images/actual_" + baseLineImageName + ".png";
                screenCapture("actual_" + baseLineImageName, imagePath);
                return imageUtil.compareImagesWithPixelDifferenceInPercentage(expectedImage, actualImage, pixelDifference);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IM4JavaException e) {
                e.printStackTrace();
            }
        }
        return Boolean.parseBoolean(null);
    }
}
