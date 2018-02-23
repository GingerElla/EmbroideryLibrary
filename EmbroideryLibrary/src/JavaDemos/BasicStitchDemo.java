package JavaDemos;

import processing.core.*;
import embroidery.*;

public class BasicStitchDemo extends PApplet {
    // Declare your Pattern and Pen here
    Pattern design = new Pattern(this);
    Pen needle = new Pen(this, 0, 0, design);

    // The name of the file does here
    public static void main(String[] args) {
        PApplet.main("JavaDemos.BasicStitchDemo");
    }

    // Size of the canvas goes here, it's important to note that the size of
    // the canvas does not "scale" your design. It just gives you more canvas space
    // to visualize.
    public void settings() {
        size(1000, 1000);
    }

    // Create your blank canvas, with a default color for visualizing your thread
    public void setup() {
        // IMPORTANT! Draw runs in an infinite loop, if you do not specify this,
        // then your design may well be infinite, or at least very, very large.
        noLoop();

        // The background color is purely to help you visualize how your design
        // will look on different colored fabrics.
        background(255);

        // This sets the initial color of the thread for your visualization.
        // Note that we don't use setColor() here. Think about why: your embroidery
        // machine, when it starts, does not care what color thread is initially
        // loaded into it--it only wants to know when it needs to stop to allow
        // YOU to change the color, which is why we DO need to use setColor
        // inside the draw function.
        fill(0, 255, 0);
    }

    public void draw() {
        // Move to an initial position; it's important to note that the design
        // has no conception of a center, it simply preserves the relative
        // position of shapes to each other.
        needle.go_to(width/2, height/3);

        // Unnecessary? You would think! But note that we used go_to() to move
        // our design to an easier place on the canvas for our visualization.
        // We don't actually want the machine to make this jump stitch.
        // So we clear it!
        design.clear();

        // Make a fun design by rotating a circle.
        for (int i = 20; i < 360; i += 20) {
            needle.em_circle(100);
            needle.right(20);
        }

        // Save the design! Note that you should probably leave out this line
        // until you are sure this is the design you want to save, especially
        // since saveDST does not do any much file management for you--it translates
        // your design into byte code and saves it with Processing's inbuilt
        // saveBytes()--so it will create the file if it is not present, but it will
        // crudely overwrite that file every time you call it, which might get
        // you some interesting errors.
        design.saveDST(design, "EmbroideryLibrary/src/JavaDemos/dsts/BasicStitchDemo.dst");
    }
}
