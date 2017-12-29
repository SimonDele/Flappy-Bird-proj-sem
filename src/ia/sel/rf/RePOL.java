package ia.sel.rf;
/**
 * "Rectified Polynomial" when using the same naming convention as ReLU. Sets the input to the power 'pow' given in Constructor. Bounded in interval [0,Integer.MAX].
 */
public class RePOL implements RealFunction {
	/**
	 * The power at which to set the input
	 */
	private int pow;
	
	/**
	 * Constructor initializating the power value
	 * @param pow the power value to set to this class' attribute.
	 */
	public RePOL(int pow) {
		this.pow = pow;
	}
	
	@Override
	public int applyTo(int x) {
		return (int) Math.min(Math.pow(Math.max(0, x), pow),Integer.MAX_VALUE); // careful not to hit the int ceiling
	}
}
