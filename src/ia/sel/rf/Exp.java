package ia.sel.rf;
/**
 * Exponential function, bounded by Integer.MAX ceiling, slowed down by a parameter.
 */
public class Exp implements RealFunction {
	/**
	 * Steepens the exponential
	 */
	private double steeper;
	
	/**
	 * Constructor that sets an empirically good value to 'steeper' for N=1000
	 */
	public Exp() {
		this.steeper = 7;
	}
	/**
	 * Constructor that sets the steeper attribute value of this class.
	 * @param steeper the value to set the steeper attribute to
	 */
	public Exp(double steeper) {
		this.steeper = steeper;
	}
	
	@Override
	public double applyTo(double x) {
		
		return Math.exp(steeper*(x-1)); // careful not to hit the int ceiling
	}
}
