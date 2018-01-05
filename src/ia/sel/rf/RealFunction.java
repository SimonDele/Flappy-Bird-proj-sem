package ia.sel.rf;

/**
 * Real function for the FunctionalSelection. The function should be positive, increasing and convex in order to have interesting results.
 */
public interface RealFunction {
	/**
	 * Apply the function to the given input.
	 * @param x the input
	 * @return the output of the function, for this input
	 */
	public double applyTo(double x);
}
