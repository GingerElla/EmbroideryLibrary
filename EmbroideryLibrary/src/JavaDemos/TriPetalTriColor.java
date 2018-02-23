package JavaDemos;

import processing.core.*;
import embroidery.*;

// +----------------------------------+----------------------------------------
// | Example Code (tripetal-tricolor) |
// +----------------------------------+

/**
 * An example of a program that has its own custom design with  multiple
 * color changes, makes multiple of those, and then saves it.
 */

public class TriPetalTriColor extends PApplet {
    // Declare your Pattern and Pen here
	Pattern design = new Pattern(this);
	Pen needle = new Pen(this, 0, 0, design);

	// The name of the file does here
	public static void main(String[] args) {
		PApplet.main("JavaDemos.TriPetalTriColor");
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
		needle.go_to(150, 150);

		// Uncessary? You would think! But note that we used go_to() to move
        // our design to an easier place on the canvas for our visualization.
        // We don't actually want the machine to make this jump stitch.
        // So we clear it!
		design.clear();

		// Draw the thing!
		triPetal(needle, design);

		// Do a jump stitch! Then draw the thing!
		needle.go_to(300, 400);
		triPetal(needle, design);

		// Do another jump stitch! Change the color! Then do another thing!
		needle.go_to(490, 195);
		needle.setColor(design, 255, 0, 128);
		triPetal(needle, design);

		// Jump again! Color change! Another thing!
		needle.go_to(600, 500);
		needle.setColor(design, 0, 255, 0);
		triPetal(needle, design);

		// Save your design! Generally speaking, since this library does
        // not do any file management for you (aside from doing the
        // translation and writing it), leave this commented out until
        // you are SURE this is the design you want to stitch.
        // Alternatively, do you own file management to ensure that
        // the file indicated by this string is empty.
		design.saveDST(design, "EmbroideryLibrary/src/JavaDemos/dsts/tripetal-tricolor.dst");
	}

    /**
     * Makes a flower-type shapes with three different colors
     * by rotating your basic square shape.
     * @param n a {@link Pen} for drawing
     * @param p a {@link Pattern} for your design
     */
	public void triPetal(Pen n, Pattern p) {
		n.down(); // REMEMBER TO PUT THE PEN DOWN

		for (int i = 0; i < 3; i++) {
		    // Rotate the color
			switch (i) {
			case 0 :
				n.angle = 0;
				break;
			case 1 : 
				n.setColor(design, 255, 0, 0);
				n.angle = 20;
				break;
			case 2 :
				n.setColor(design, 255, 255, 255);
				n.angle = 40;
			}

			// Draw 1/3 of the flower
			for (int j = 0; j <= 6; j ++) {
				n.em_rect(100, 100);
				n.right(60);
			}
		}

		n.up(); // REMEMBER TO PUT THE PEN UP
	}
}