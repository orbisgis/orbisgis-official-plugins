package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Base renderer for GraphicalElement.
 * This abstract class contain a function applying to the bufferedImage the rotation angle of the GraphicalElement.
 * In fact, after the rotation, the bounding box of the image will be bigger. So if the original bounding box is kept, the image will be cut.
 * That's why a new image is created, with the new Bounding box and the original image is drawn inside. After the rotation the image won't be cut.
 */
public abstract class SimpleGERenderer implements GERenderer {

    protected BufferedImage applyRotationToBufferedImage(BufferedImage bi, GraphicalElement ge){
        //Calculate the size of the bufferedImage according to the rotation of the ge.
        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newWidth = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());

        //Create a new BufferedImage with the new size (size after rotation)
        BufferedImage bufferedImage = new BufferedImage((int)newWidth, (int)newHeight, bi.getType());
        Graphics2D graph = bufferedImage.createGraphics();

        //Draw the BufferedImage bi into the bigger BufferedImage
        graph.drawImage(bi, ((int)newWidth-ge.getWidth())/2, ((int)newHeight-ge.getHeight())/2,
                (int)newWidth-(((int)newWidth-ge.getWidth() )/ 2), (int)newHeight-(((int)newHeight-ge.getHeight() )/ 2),
                0, 0,
                ge.getWidth(), ge.getHeight(), null);
        graph.dispose();
        bi = bufferedImage;

        AffineTransform affineTransform;
        AffineTransformOp affineTransformOp;

        //Create the rotation transform fo the buffered image
        affineTransform= new AffineTransform();
        affineTransform.rotate(Math.toRadians(ge.getRotation()), newWidth / 2, newHeight / 2);

        //Apply the transform to the bufferedImage an return it
        affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        return affineTransformOp.filter(bi, null);
    }
}
