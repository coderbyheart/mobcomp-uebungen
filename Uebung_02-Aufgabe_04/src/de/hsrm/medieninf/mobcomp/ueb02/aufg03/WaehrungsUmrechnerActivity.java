package de.hsrm.medieninf.mobcomp.ueb02.aufg03;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WaehrungsUmrechnerActivity extends Activity {
	public static final String TAG = "WaehrungsUmrechnerActivity";
	public static final int DIALOG_CURRENCY = 1;

	private Currency waehrungA, waehrungB;
	private EditText valueA, valueB;
	private Button buttonA, buttonB;
	private InputWatcher watcherA, watcherB;
	private CurrencyField fieldA, fieldB;
	private Map<Integer, Currency> waehrungen = new HashMap<Integer, Currency>();
	private Button clickedButton;

	private class CurrencyField {
		private EditText field;
		private Currency currency;
		private String name;

		public CurrencyField(String name, EditText field) {
			setField(field);
			setName(name);
		}

		public EditText getField() {
			return field;
		}

		public void setField(EditText field) {
			this.field = field;
		}

		public Currency getCurrency() {
			return currency;
		}

		public void setCurrency(Currency currency) {
			this.currency = currency;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	private class InputWatcher implements TextWatcher {
		private CurrencyField watch, update;
		private InputWatcher updateWatcher;

		public InputWatcher(CurrencyField watch, CurrencyField update) {
			this.watch = watch;
			this.update = update;
		}

		@Override
		public void afterTextChanged(Editable s) {
			Log.v(TAG, "UPDATE: " + update.getName());
			update.getField().removeTextChangedListener(updateWatcher);
			try {
				update.getField().setText(
						formatCurrency(update.getCurrency().asCurrency(
								watch.getCurrency().asEuro(
										Double.parseDouble(watch.getField()
												.getText().toString())))));
			} catch (NumberFormatException e) {
				update.getField().setText("");
			}
			update.getField().addTextChangedListener(updateWatcher);
		}

		private String formatCurrency(BigDecimal d) {
			return String.format("%.02f", d);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		public void setUpdaterListener(InputWatcher updateWatcher) {
			this.updateWatcher = updateWatcher;
		}
	}

	private class SelectCurrencyListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			clickedButton = (Button) v;
			showDialog(DIALOG_CURRENCY);
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Rechner rechner = new Rechner(this);
		List<Currency> defaultWaehrungen = rechner.getDefaultCurrencies();
		
		waehrungen.put(0, defaultWaehrungen.get(0));
		waehrungen.put(1, defaultWaehrungen.get(1));
		waehrungen.put(2, defaultWaehrungen.get(2));

		valueA = (EditText) findViewById(R.id.inputA);
		valueB = (EditText) findViewById(R.id.inputB);
		fieldA = new CurrencyField("A", valueA);
		fieldB = new CurrencyField("B", valueB);
		watcherA = new InputWatcher(fieldA, fieldB);
		watcherB = new InputWatcher(fieldB, fieldA);
		watcherA.setUpdaterListener(watcherB);
		watcherB.setUpdaterListener(watcherA);
		valueA.addTextChangedListener(watcherA);
		valueB.addTextChangedListener(watcherB);
		buttonA = (Button) findViewById(R.id.buttonA);
		buttonB = (Button) findViewById(R.id.buttonB);
		SelectCurrencyListener scl = new SelectCurrencyListener();
		buttonA.setOnClickListener(scl);
		buttonB.setOnClickListener(scl);

		setWaehrungA(defaultWaehrungen.get(0));
		setWaehrungB(defaultWaehrungen.get(1));
	}

	public Currency getWaehrungA() {
		return waehrungA;
	}

	public void setWaehrungA(Currency waehrungA) {
		this.waehrungA = waehrungA;
		buttonA.setText(getWaehrungA().getSymbol());
		fieldA.setCurrency(this.waehrungA);
	}

	public Currency getWaehrungB() {
		return waehrungB;
	}

	public void setWaehrungB(Currency waehrungB) {
		this.waehrungB = waehrungB;
		buttonB.setText(getWaehrungB().getSymbol());
		fieldB.setCurrency(this.waehrungB);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_CURRENCY:
			ArrayList<CharSequence> waehrungenNames = new ArrayList<CharSequence>();
			for (Currency w : waehrungen.values()) {
				waehrungenNames.add(w.getName());
			}

			return new AlertDialog.Builder(WaehrungsUmrechnerActivity.this)
					.setTitle(R.string.currency_select_dialog_title)
					.setItems(
							waehrungenNames.toArray(new CharSequence[waehrungenNames
									.size()]),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									if (clickedButton.equals(buttonA)) {
										setWaehrungA(waehrungen.get(which));
										watcherA.afterTextChanged(fieldA
												.getField().getEditableText());
									} else {
										setWaehrungB(waehrungen.get(which));
										watcherB.afterTextChanged(fieldB
												.getField().getEditableText());
									}
								}
							}).create();
		}
		return null;
	}

	/**
	 * Menü erzeugen
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * Wenn ein Menüpunkt ausgewählt wurde ...
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_config_currencies:
			startActivity(new Intent(WaehrungsUmrechnerActivity.this,
					CurrencySelectActivity.class));
			return true;
		case R.id.menu_quit:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}