package embroidery;

/**
 * The object Stitch
 */
public class Stitch {
	public int changex;
	public int changey;
	public char type;

    /**
     * Declaration for a new stitch
     * @param tempChangex an {@link int} indicating the change in distance on the x-axis
     * @param tempChangey an {@link int} indicating the change in distance on the y-axis,
     *                    expects the change to be expressed as if the origin is
     *                    in the top-left corner and negates it to reflect a
     *                    centered origin
     * @param tempType    a {@link char} indicating the type of stitch,
     *                    i.e. 'j' = jump sitich, 'c' = color change stitch,
     *                         'n' = normal stitch.
     */
	Stitch(int tempChangex, int tempChangey, char tempType) {
		changex = tempChangex;
		changey = -(tempChangey);
		type = tempType;
	}
}
