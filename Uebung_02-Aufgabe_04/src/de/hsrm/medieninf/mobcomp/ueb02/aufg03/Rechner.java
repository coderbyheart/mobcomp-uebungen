package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class Rechner {

	Context ctx;

	public Rechner(Context context) {
		ctx = context;
	}

	public List<Waehrung> getDefaultCurrencies() {

		ArrayList<Waehrung> waehrungen = new ArrayList<Waehrung>();
		Waehrung euro = new Waehrung()
				.setSymbol("€")
				.setName(
						(String) ctx.getResources()
								.getString(R.string.conc_eur)).setRate(1.0);
		Waehrung usd = new Waehrung()
				.setSymbol("$")
				.setName(
						(String) ctx.getResources()
								.getString(R.string.conc_usd))
				.setRate(0.7183392);
		Waehrung grd = new Waehrung()
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
