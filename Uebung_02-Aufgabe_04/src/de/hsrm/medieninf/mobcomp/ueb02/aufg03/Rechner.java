package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Rechner {

	Context ctx;

	public Rechner(Context context) {
		ctx = context;
	}

	public List<Currency> getDefaultCurrencies() {

		ArrayList<Currency> waehrungen = new ArrayList<Currency>();
		Currency euro = new Currency()
				.setSymbol("€")
				.setName(
						(String) ctx.getResources()
								.getString(R.string.conc_eur)).setRate(1.0);
		Currency usd = new Currency()
				.setSymbol("$")
				.setName(
						(String) ctx.getResources()
								.getString(R.string.conc_usd))
				.setRate(0.7183392);
		Currency grd = new Currency()
				.setSymbol("₯")
				.setName(
						(String) ctx.getResources()
								.getString(R.string.conc_grd))
				.setRate(2.93470286e-6);
		waehrungen.add(euro);
		waehrungen.add(usd);
		waehrungen.add(grd);
		return waehrungen;
	}

}
