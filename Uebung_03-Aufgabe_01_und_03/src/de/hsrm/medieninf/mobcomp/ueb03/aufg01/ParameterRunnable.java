package de.hsrm.medieninf.mobcomp.ueb03.aufg01;

/**
 * Ein Runnable mit einem Parameter, der von außen gesetzt werden kann
 * @author Markus Tacker <m@coderbyheart.de>
 *
 * @param <T>
 */
abstract public class ParameterRunnable<T> implements Runnable {
	protected T parameter;

	public T getParameter() {
		return parameter;
	}
	
	public void setParameter(T parameter) {
		this.parameter = parameter;
	}
}
