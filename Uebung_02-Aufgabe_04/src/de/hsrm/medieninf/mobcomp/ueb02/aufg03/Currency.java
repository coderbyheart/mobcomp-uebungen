package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import java.math.BigDecimal;
import java.security.InvalidParameterException;

public class Currency {
	private String symbol;
	private double rate;
	private String name;
	private int id;

	public String getSymbol() {
		return symbol;
	}

	public Currency setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public double getRate() {
		return rate;
	}

	/**
	 * Setzt den Umrechnungskurs in Relation zur Basiswährung
	 * 
	 * @param rate
	 */
	public Currency setRate(double rate) {
		if (rate > 1)
			throw new InvalidParameterException(
					"Rate muss zwischen 0 und 1 liegen.");
		this.rate = rate;
		return this;
	}
	
	public String getName() {
		return name;
	}

	public Currency setName(String name) {
		this.name = name;
		return this;
	}
	
	public BigDecimal asEuro(double v) {
		return asEuro(BigDecimal.valueOf(v));
	}

	public BigDecimal asEuro(BigDecimal v) {
		return v.multiply(BigDecimal.valueOf(rate));
	}

	public BigDecimal asCurrency(Double v) {
		return asCurrency(BigDecimal.valueOf(v));
	}

	public BigDecimal asCurrency(BigDecimal v) {
		return v.multiply(BigDecimal.valueOf(1.0 / rate));
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
