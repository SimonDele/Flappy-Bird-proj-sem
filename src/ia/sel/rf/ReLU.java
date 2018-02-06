package ia.sel.rf;
/**
 * Usual ReLU function - outputs the positive part of its argument. Closest to the identity implicitly used in {@linkplain BasicSelection BasicSelection} (very few negative outputs)
 */
public class ReLU implements RealFunction {
	@Override
	public double applyTo(double x) {
		return Math.max(0, x);
	}
}
