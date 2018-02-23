package JavaDemos;
import processing.core.*;

/**
 * This is a simple file that demonstrates using Processing Applets inside
 * of your own IDE (Eclipse or IntelliJ). This project already includes
 * Processing's core.jar, so you do not need to add that yourself, but
 * if you're interested in how to make Processing run outside of its
 * custom IDE, they have a helpful tutorial on their website. There is also
 * this super helpful resource, which I used in making this:
 * {@linkliteral http://gigl.scs.carleton.ca/node/48}
 */
public class ProcessingCheck extends PApplet {

	public static void main(String[] args) {
		// Uncommenting "--present" will let the applet run in full screen
		// The form of the second string should be: thisPackageName.thisClassName
        // (if the file is in any package other than the default)
		PApplet.main(new String[] { /*"--present",*/ "JavaDemos.ProcessingCheck" });

	}

	// The size of the canvas, which should not change over the course of
    // running, should be declared here
	public void settings() {
		size(400, 400);
	}

	// These values may change during the running, but this allows for some
    // initial setup
	public void setup() {
		background(255);
	}

	// Dynamic values, note that in Processing this function runs in an
    // infinite loop.
    // This draws a simple line.
	public void draw() {
		fill(0);
		line((float) 2, (float) 2, (float) 20, (float) 20);
		rect(100, 100, 200, 200);
	}

}
