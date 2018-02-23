package embroidery;
import processing.core.*;

// +------------------+----------------------------------------------
// | Turtle Interface |
// +------------------+
/**
 * The following functions provide commands for an object called Pen.
 * We can think of a Pen like a turtle object, which operates on the
 * very basic ideas of forward, right, left--and, since embroidery
 * allows to do jump stitches--teleport. We also provide several
 * functions which implement basic shapes.
 *
 * In this class you will see multiple uses of p's methods, this is not gratuitous,
 * but because they are necessary for making the code work in the Processing environment.
 */

/*--------------------Helper Functions-------------------- */
public class Pen {
    // The Processing applet we use for visualization
	PApplet p;

    /**
     * Performs the distance equation on the given values.
     * @param x a float, the initial x-value
     * @param y a float, the initial y-value
     * @param newX a float, the new x-value
     * @param newY a float, the new y-value
     * @return distance, a float representing the distance between the two
     *         points (x,y) and (newX, newY)
     */
	float eq_distance(float x, float y, float newX, float newY) {
		return (float) p.sqrt(p.sq(newX - x) + p.sq(newY - y));
	}

    /**
     * Determines whether a number is evenly divided by its divisor
     * @param dividend a float, the number to divide
     * @param divisor a float, the number to divide by
     * @return a boolean, true if dividend is evenly divided by the divisor,
     *          otherwise false
     */
	boolean divides_evenly(float dividend, float divisor) {
		return ((dividend % divisor) == 0);
	}

    /**
     * Determines whether either dx or dy is greater than the maximum units
     * the embroidery machine can hangle
     * @param dx    a float, the change in distance on the x-axis
     * @param dy    a float, the change in distance on the x-axis
     * @return a boolean, true if the change in x or y is greater than the limit,
     *          otherwise false
     */
	boolean at_limit(float dx, float dy) {
		int limit = 121; // max units the embroidery machine can handle
		return (p.abs(dx) > limit || p.abs(dy) > limit);
	}

	/*--------------------Pen (Turtle) Code-------------------- */

	// We were inspired by the following:
	// http://j4mie.org/blog/simple-turtle-for-processing/
	// We changed the code to reflect the functionality of a needle
	// on an embroidery machine.

	public Pattern design;
	public float x, y; // Current position of the turtle
	public float angle = 0; // Current heading of the turtle
	public float stitch_size = 9;
	public int thread = 0; // thread color, as int
	public boolean penDown = true; // Is pen down?

    private static final int DOT_SIZE = 4;

    /**
     * Given the various values, creates an initialized Pen
     * @param parent    a {@link PApplet} for visualization
     * @param xin       a float, the initial x-position
     * @param yin       a float, the initial y-position
     * @param design    a {@link Pattern}, note that the design and this
     *                  {@link Pen} must have the same {@link PApplet}
     */
	public Pen (PApplet parent, float xin, float yin, Pattern design) {
		this.p = parent;
		this.x = xin;
		this.y = yin;
		this.design = design;
	}

    /**
     * Creates a new Pen object with default values
     * @param parent a {@link PApplet} for visualization
     * @param design a {@link Pattern}, note that the design and this
     *               {@link Pen} must have the same {@link PApplet}
     */
	public Pen (PApplet parent, Pattern design) {
		new Pen (parent, 0, 0, design);
	}

    /**
     * Draws the visualization of stitches onto the {@link PApplet}, also
     * adds those stitches into the {@link Pattern} (i.e. HAS SIDEFFECTS!!!)
     * @param newX a float, the x-position of where we are going
     * @param newY a float, the y-position of where we are going
     * @param size a float, the current size of the stitch
     * @return
     */
	private int draw_points(float newX, float newY, float size) {
	    // Determine the length of the line, the change in in x and y,
        // the number stitches needed to make the line, and therefore
        // the respective change in x factor and change in y factor
        // for every stitch.
		float length = eq_distance(this.x, this.y, newX, newY);
		float dx = newX - this.x;
		float dy = newY - this.y;
		int n = p.round(length / size); // n = number of stitches
		float xFactor = dx / n;
		float yFactor = dy / n;

		// If the pen is down, i.e. we are making normal stitches
		if (penDown) {
			for (int i = 0; i < n; i++) {
			    // Get the coordinate of the next stitch
				float tempX = x + xFactor;
				float tempY = y + yFactor;

				// Draw the visual representation, a line and a dot
				p.line(this.x, this.y, tempX, tempY);
				p.ellipse(tempX, tempY, DOT_SIZE, DOT_SIZE);

				// Add the stitch to the design
				design.addStitch(xFactor, yFactor, 'n');

				// Set the current position to the next position
				this.x = tempX;
				this.y = tempY;
			}
		} else { // This is a jump stitch
            // Add the stitch to the design
			design.addStitch(newX - this.x, newY - this.y, 'j');

			// Draw the visualization (use red line to signify)
			p.stroke(255, 0, 0);
			p.line(this.x, this.y, newX, newY);
			p.noStroke();

			// Set the current position to the new position
			this.x = newX;
			this.y = newY;
		}
		return design.size;
	}

    /**
     * Moves the turtle forward by distance
     * @param distance a float, the forward distance to travel
     */
	public void forward(float distance) {
	    // Determine the target new point
		float xtarget = this.x + p.cos(p.radians(angle)) * distance;
		float ytarget = this.y + p.sin(p.radians(angle)) * distance;

		// Ensure the line and dot will be the correct color and size
		p.stroke(thread);
		p.strokeWeight(1);

		// Draw a Stitch at our current location
		p.ellipse(x, y, DOT_SIZE, DOT_SIZE);

		// Draw the other stitches (if necessary)
		draw_points(xtarget, ytarget, stitch_size);
	}

    /**
     * Turn left by given angle
     * @param turnangle a float, the angle (in degrees!) to turn by
     */
	public void left(float turnangle) {
		if (this.angle - turnangle < 0) {
			this.angle = this.angle - turnangle + 360;
		} else {
			this.angle -= turnangle;
		}
	}

    /**
     * Turn right by given angle
     * @param turnangle a float, the angle (in degrees!) to turn by
     */
	public void right(float turnangle) {
		if (this.angle + turnangle > 360) {
			this.angle = this.angle + turnangle - 360;
		} else {
			this.angle += turnangle;
		}
	}

    /**
     * Set the pen to be up
     */
	public void up() {
		this.penDown = false;
	}

    /**
     * Set the pen to be down
     */
	public void down() {
		this.penDown = true;
	}

    /**
     * Stops the machine to allow a change of threads.
     * In the {@link PApplet} it will only show a color change, and this
     * physical stop will not be apparent.
     * @param design the {@link Pattern} to add the color change stitch to
     * @param red   the int red value of an RGB color
     * @param green the int green value of an RGB color
     * @param blue  the int blue value of an RGB color
     */
	public void setColor(Pattern design, int red, int green, int blue) {
	    // Change the visualization
		p.fill(red, green, blue);
		p.stroke(red, green, blue);

		// Add the color-change stitch
		design.addStitch(0, 0, 'c');
	}

    /**
     * Our equivalent of teleport
     * @param x a float, the x-value of the point to go to
     * @param y a float, the y-value of the point to go to
     */
	public void go_to(float x, float y) {
	    // If the pen is down actually stitch the line
		if (penDown) {
			draw_points(x, y, stitch_size);
		} else { // Otherwise add a jump stitch
			design.addStitch(x - this.x, y - this.y, 'j');
			p.stroke(255, 0, 0);
			p.line(this.x, this.y, x, y);
			p.stroke(0);
			this.x = x;
			this.y = y;
		}
	}
	
	/*--------------------User Shapes-------------------- */

    /**
     * Draw a line to the point (targetX, targetY)
     * @param targetX   a float, the x-value to end at
     * @param targetY   a float, the y-value to end at
     */
	public void em_line(float targetX, float targetY) {
		go_to(targetX, targetY);
	}

    /**
     * Draws a basic rectangle of width and height, with its top-left corner
     * at the Pen's starting position, at the angle the Pen is currently facing.
     *
     * After the rectangle is drawn the turtle should be facing the same
     * direction it started in.
     * @param width     a float, the width of the rectangle
     * @param height    a float, the height of the ractangle
     */
	public void em_rect(float width, float height) {
		for (int i = 0; i < 2; i++) {
			forward(width);
			right(90);
			forward(height);
			right(90);
		}
	}

    /**
     * Draws a basic square of sidelength, with its top-left corner
     * at the Pen's starting position, at the angle the Pen is currently facing
     *
     * After the rectangle is drawn the turtle should be facing the same
     * direction it started in.
     *
     * Side note: em_square is not implemented with em_rectangle, and em_circle
     * is not implemented em_ellipse, in order to give more examples of how one
     * might use the various functions.
     *
     * @param sidelength    a float, the length of each side
     */
	public void em_square(float sidelength) {
		for (int i = 0; i < 4; i++) {
			forward(sidelength);
			right(90);
		}
	}

    /**
     * Draw a basic circle with given radius. The circle is created in a
     * clock-wise manner and at the end the turtle should be facing the
     * same direction it started in.
     *
     * Side note: em_square is not implemented with em_rectangle, and em_circle
     * is not implemented em_ellipse, in order to give more examples of how one
     * might use the various functions.
     *
     * @param radius    a float, the radius of the circle to draw
     */
	public void em_circle(float radius) {
		float circumference = 2 * p.PI * radius;

		// We determine the turn angle by diving the circumference
        // into stitch_size lines, and determining how much we
        // should turn before making each small line
		float turn_angle = 360 / (circumference / stitch_size);
		for (int i = 0; i < 360 / turn_angle; i++) {
			forward(stitch_size);
			right(turn_angle);
		}
	}

    /**
     * Draws a regular star of given size
     * @param size  an int, the size of the star
     */
	public void em_star(int size) {
		forward(size);
		left(72);
		forward(size);
		for (int i = 0; i < 4; i++) {
			left(180 + 36);
			forward(size);
			left(72);
			forward(size);
		}
	}

    /**
     * Draw a regular polygon with n sides of given side_length
     * @param n an int, the number of sides in the polygon
     * @param side_length   an int, the side length of each side
     */
	public void em_polygon(int n, int side_length) {

		float exterior_angle = 360 / n;

		for (int i = 0; i < n; i++) {
			forward(side_length);
			right(exterior_angle);
		}
	}

    /**
     * Draws an ellipse centered at (xcenter, ycenter) with height a and width b.
     * The ellipse is drawn clock-wise around the center point and at the end
     * the turtle position and orientation should be the same as it started.
     *
     * @param xcenter   a float, the x-coordinate of the center
     * @param ycenter   a float, the y-coordinate of the center
     * @param a     a float, the height of the ellipse
     * @param b     b float, the width of the ellipse
     */
	public void em_ellipse (float xcenter, float ycenter, float a, float b) {

	    // Get the initial angle the turtle faced
		float initangle = angle;

		// Figure out the starting coordinates for drawing
		float x = xcenter + (a * p.cos(0) * p.cos(initangle)) + 
				(b * p.sin(0) * -p.sin(initangle));
		float y = ycenter + (a * p.cos(0) * p.sin(initangle)) + 
				(b * p.sin(0) * p.cos(initangle));

		// Jump-stitch to the center
		up();
		go_to(x, y);
		down();

		// Draw the ellipse
		for (float i = 0; i <= 360; i += 5) {
			float angle = (float) (i / 180.0 * p.PI);
			x = xcenter + (a * p.cos(angle) * p.cos(initangle)) + 
					(b * p.sin(angle) * -p.sin(initangle));
			y = ycenter + (a * p.cos(angle) * p.sin(initangle)) + 
					(b * p.sin(angle) * p.cos(initangle));
			go_to(x, y);
		}

		// math.stackexchange.com/questions/426150/what-is-the-general-equation-of-the-ellipse-that-is-not-in-the-origin-and-rotate
		// x = (xcenter, ycenter) + (a*cos(theta))*(cos(pen.angle),
		// sin(pen.angle))+
		// (b*sin(theta))*(-1*sin(pen.angle), cos(pen.angle))
	}

    /**
     * Draw an ellipse from the Pen's current position with height a and width b
     * @param a     a float, the height of the ellipse
     * @param b     b float, the width of the ellipse
     */
	public void em_ellipse2 (float a, float b) {

		float initx = this.x;
		float inity = this.y;
		float initangle = this.angle;
		float x = initx + (a * p.cos(0) * p.cos(initangle)) + (b * p.sin(0) * -p.sin(initangle));
		float y = inity + (a * p.cos(0) * p.sin(initangle)) + (b * p.sin(0) * p.cos(initangle));

		up();
		go_to(x, y);
		down();

		for (float i = 0; i <= 360; i += 5) {
			float angle = (float) (i / 180.0 * p.PI);
			x = initx + (a * p.cos(angle) * p.cos(initangle)) + 
					(b * p.sin(angle) * -p.sin(initangle));
			y = inity + (a * p.cos(angle) * p.sin(initangle)) + 
					(b * p.sin(angle) * p.cos(initangle));
			go_to(x, y);
		}

	}

    /**
     * Draw an arc with the given a and b
     * @param a a float
     * @param b a float
     * @param arcAngle  a float, angle to point at (in the beginning)
     */
	public void em_arc(float a, float b, float arcAngle) {

		float initx = this.x;
		float inity = this.y;
		float initangle = this.angle;
		float x = initx + (a * p.cos(0) * p.cos(initangle)) + 
				(b * p.sin(0) * -p.sin(initangle));
		float y = inity + (a * p.cos(0) * p.sin(initangle)) + 
				(b * p.sin(0) * p.cos(initangle));

		up();
		go_to(x, y);
		down();

		for (float i = 0; i <= arcAngle; i += 5) {
			float angle = (float) (i / 180.0 * p.PI);
			x = initx + (a * p.cos(angle) * p.cos(initangle)) + 
					(b * p.sin(angle) * -p.sin(initangle));
			y = inity + (a * p.cos(angle) * p.sin(initangle)) + 
					(b * p.sin(angle) * p.cos(initangle));
			go_to(x, y);
		}

	}

    /**
     * Draw a triangle using the current starting position, and two other
     * points given by (x1,y1) and (x2,y2)
     * @param x1 an int, the x-position of point 1
     * @param y1 an int, the y-position of point 1
     * @param x2 an int, the x-position of point 2
     * @param y2 an int, the y-position of point 2
     */
	void em_triangle(int x1, int y1, int x2, int y2) {

		float initx = this.x;
		float inity = this.y;

		go_to(x1, y1);
		go_to(x2, y2);
		go_to(initx, inity);
	}

    /**
     * Makes a rhombus the the given side_length at the given degree
     * @param side_length   a float, the side_length
     * @param degree    a float, the angle to point at (in DEGREES!!!)
     */
	public void em_rhombus(float side_length, float degree) {

		float angle2 = 180 - degree;

		for (int i = 0; i < 4; i++) {
			forward(side_length);
			right(degree);
			forward(side_length);
			right(angle2);
		}
	}
}