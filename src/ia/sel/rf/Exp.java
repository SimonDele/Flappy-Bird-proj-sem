package ia.sel.rf;
/**
 * Exponential function, bounded by Integer.MAX ceiling, slowed down by a parameter.
 */
public class Exp implements RealFunction {
	/**
	 * Desperate attempt to slow down the exponential
	 */
	private double slower;
	
	/**
	 * Constructor that ignores the slower parameter
	 */
	public Exp() {
		this.slower = 1;
	}
	/**
	 * Constructor that sets the slower attribute value of this class.
	 * @param slower the value to set the slower attribute to
	 */
	public Exp(double slower) {
		this.slower = slower;
	}
	
	@Override
	public int applyTo(int x) {
		return (int) Math.min(Math.exp(slower*x),Integer.MAX_VALUE); // careful not to hit the int ceiling
	}
}
