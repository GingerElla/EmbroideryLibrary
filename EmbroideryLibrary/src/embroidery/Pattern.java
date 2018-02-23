package embroidery;
import processing.core.*;

/**
 * The class Pattern
 * In this class you will see multiple uses of p's methods, this is not gratuitous,
 * but because they are necessary for making the code work in the Processing environment.
 */
public class Pattern {
	private PApplet p; // required for Processing
	public Stitch[] stitches = new Stitch[2]; // an array of stitches
	public int size = 0;

	// Represents the largest stitch the dst format supports
	private static final int MAX_CHANGE = 121;

    /**
     * Pattern holds an array of stitches, the size of the pattern (i.e. the
     * number of stitches, and a PApplet that runs the visual representation).
     * @param parent    a {@link PApplet} that will run the visual representation
     */
	public Pattern (PApplet parent) {
		p = parent;
	}

    /**
     * Adds a {@link Stitch} to the {@link Pattern}, given the parameters breaks up
     * stitches that are too large into multiple stitches
     * @param tempChangex   an {@link int} indicating the change in distance on the x-axis
     * @param tempChangey   an {@link int} indicating the change in distance on the y-axis
     * @param tempType      a {@link char} indicating the type of stitch,
     *                      i.e. 'j' = jump sitich, 'c' = color change stitch,
     *                           'n' = normal stitch.
     */
	void addStitch(float tempChangex, float tempChangey, char tempType) {

	    // If the size of either change is too large...
		if (p.abs(tempChangex) > MAX_CHANGE || p.abs(tempChangey) > MAX_CHANGE) {
		    // Determine by what factor each change is too large
			int xcounter = p.ceil(p.abs(tempChangex) / MAX_CHANGE);
			int ycounter = p.ceil(p.abs(tempChangey) / MAX_CHANGE);
			int counter = 0;

			// Use whichever counter is bigger as the "real" counter
			if (xcounter >= ycounter) {
				counter = xcounter;
			} else if (ycounter >= xcounter) {
				counter = ycounter;
			}

			// Break up the stitch as many times as necessary, and add
            // each stitch to the this pattern; note that we make sure to divide both
            // x and y by the counter in order to maintain the angle of the stitch
			for (int i = 0; i < counter; i++) {
				if (this.size == 0) {
					this.stitches[this.size++] = new Stitch(p.round(tempChangex / counter), 
							p.round(tempChangey / counter), tempType);
				} else {
				    // Note that we expand every time and leave one spot at the end of the
                    // array free; this is to allow easy insertion of the endian byte
					this.stitches = (Stitch[]) p.expand(this.stitches, this.size + 1);
					this.stitches[this.size++] = new Stitch(p.round(tempChangex / counter), 
							p.round(tempChangey / counter), tempType);
				}
			}
		} else { // If the stitch is within the MAX_CHANGE, simply add the stitch
			if (this.size == 0) {
				this.stitches[this.size++] = new Stitch(p.round(tempChangex), p.round(tempChangey), tempType);
			} else {
				this.stitches = (Stitch[]) p.expand(this.stitches, this.size + 1);
				this.stitches[this.size++] = new Stitch(p.round(tempChangex), p.round(tempChangey), tempType);
			}
		}
	}

    /**
     * Prints the {@link Pattern} as one {@link Stitch} per line
     */
	public void read() {
		if (this.size == 0) {
			p.println("Empty Pattern.");
		}
		for (int i = 0; i < this.size; i++) {
			p.println(this.stitches[i].changex + " " + this.stitches[i].changey + " " + this.stitches[i].type);
		}
	}

    /**
     * Shrinks the array of stitches until none are left,
     * resets the background of the {@link PApplet} to white
     */
	public void clear() {
		if (this.size > 0) {
			while (this.size > 1) {
				this.stitches = (Stitch[]) p.shorten(this.stitches);
				this.size--;
			}
		}
		p.background(255);
	}
	
	// +--------------------+---------------------------------------------
	// | Saving as DST file |
	// +--------------------+
	//
	//Citations: The algorithm for converting into dst format was taken from
	//stitchcode: https://github.com/backface/stitchcode/blob/master/stitchcode.py

	/*--------------------Constants-------------------- */
	
	private final static byte BIT7 = (byte) 128;
	private final static byte BIT6 = (byte) 64;
	private final static byte BIT5 = (byte) 32;
	private final static byte BIT4 = (byte) 16;
	private final static byte BIT3 = (byte) 8;
	private final static byte BIT2 = (byte) 4;
	private final static byte BIT1 = (byte) 2;
	private final static byte BIT0 = (byte) 1;

    /**
     * Saves a given {@link Pattern} to the file specified by the {@link String};
     * does not manage file dependencies of errors, merely does the translation
     * from each {@link Stitch} to its .dst ternary format
     * @param pat   a {@link Pattern} to save
     * @param file  a {@link String} filename of the file to save to
     */
	public void saveDST(Pattern pat, String file) {
		if (pat.size == 0) {
			p.println("Error: Pattern is empty.");
			return;
		}

		// Adding 512 bytes of 0 for the header.
        // TODO: actually implement the header information
		byte[] header = new byte[512];

		for (int i = 0; i < 512; i++) {
			header[i] = 0;
		}

		// create a byte array 3 times the size of pat, plus 3 for the final 3
		// bytes
		byte[] arr = new byte[(3 * pat.size) + 3];

		// Recurse over pattern and translate stitches to bytes, which are
		// stored in arr;
		int i = 0;
		int n = 0;

		while (n < pat.size) {
			byte b1 = (byte) 0;
			byte b2 = (byte) 0;
			byte b3 = (byte) 0;

			int x = pat.stitches[n].changex;
			int y = pat.stitches[n].changey;
			char stitch = pat.stitches[n].type;

			// setting the x-y change bits.
			if (x > 40) {
				b3 |= BIT2;
				x -= 81;
			}
			if (x < -40) {
				b3 |= BIT3;
				x += 81;
			}
			if (y > 40) {
				b3 |= BIT5;
				y -= 81;
			}
			if (y < -40) {
				b3 |= BIT4;
				y += 81;
			}
			if (x > 13) {
				b2 |= BIT2;
				x -= 27;
			}
			if (x < -13) {
				b2 |= BIT3;
				x += 27;
			}
			if (y > 13) {
				b2 |= BIT5;
				y -= 27;
			}
			if (y < -13) {
				b2 |= BIT4;
				y += 27;
			}
			if (x > 4) {
				b1 |= BIT2;
				x -= 9;
			}
			if (x < -4) {
				b1 |= BIT3;
				x += 9;
			}
			if (y > 4) {
				b1 |= BIT5;
				y -= 9;
			}
			if (y < -4) {
				b1 |= BIT4;
				y += 9;
			}
			if (x > 1) {
				b2 |= BIT0;
				x -= 3;
			}
			if (x < -1) {
				b2 |= BIT1;
				x += 3;
			}
			if (y > 1) {
				b2 |= BIT7;
				y -= 3;
			}
			if (y < -1) {
				b2 |= BIT6;
				y += 3;
			}
			if (x > 0) {
				b1 |= BIT0;
				x -= 1;
			}
			if (x < 0) {
				b1 |= BIT1;
				x += 1;
			}
			if (y > 0) {
				b1 |= BIT7;
				y -= 1;
			}
			if (y < 0) {
				b1 |= BIT6;
				y += 1;
			}

			// setting the jump/color change bits.
			if (stitch == 'c') {
				b3 |= BIT6;
				b3 |= BIT7;
			}
			if (stitch == 'j') {
				b3 |= BIT7;
			}

			b3 |= BIT0; // setting the 'set' bits.
			b3 |= BIT1;

			arr[i] = b1;
			i++;
			arr[i] = b2;
			i++;
			arr[i] = b3;
			i++;
			n++;
		}

		// Add the ending to arr and write arr to file.
		byte b = 0; // Adds the final 3 bits to arr.
		arr[i] = b;
		i++;
		arr[i] = b;
		i++;
		b |= BIT7 | BIT6 | BIT5 | BIT4 | BIT1 | BIT0;
		arr[i] = b;

		byte[] contents = p.concat(header, arr);
		p.saveBytes(file, contents);
	}
}
