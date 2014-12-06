package com.azra.cryptography;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cryptography.R;

public class MainActivity extends Activity implements OnClickListener {

	Button buttonEncrypt;
	EditText editTextOriginal;
	private Button buttonDecrypt, buttonClear;
	String encryptedString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editTextOriginal = (EditText) findViewById(R.id.txtOriginalText);

		buttonEncrypt = (Button) findViewById(R.id.btnEncrypt);
		buttonDecrypt = (Button) findViewById(R.id.btnDecrypt);
		buttonClear = (Button) findViewById(R.id.buttonClear);

		buttonEncrypt.setOnClickListener(this);
		buttonDecrypt.setOnClickListener(this);
		buttonClear.setOnClickListener(this);
	}

	private void showDialog(String title, String message) {
		final Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(title);
		dialog.setMessage(message).setNegativeButton("OK", null);
		dialog.show();

	}

	@Override
	public void onClick(View v) {
		hideKeyboard(MainActivity.this);
		switch (v.getId()) {
		case R.id.btnEncrypt:

			if (editTextOriginal.getText().toString().trim().length() > 0) {
				encryptedString = SymmetricAlgorithmAES
						.encryptString(editTextOriginal.getText().toString()
								.trim());
				showDialog("Encrypted Text", encryptedString);
			} else {
				Toast.makeText(getApplicationContext(),
						"Enter Value to encrypt", Toast.LENGTH_LONG).show();
			}

			break;

		case R.id.btnDecrypt:
			if (encryptedString == null) {
				Toast.makeText(getApplicationContext(), "Encrypt first !",
						Toast.LENGTH_LONG).show();
			} else {
				showDialog("Decrypted Text",
						SymmetricAlgorithmAES.decryptString(encryptedString));
			}

			break;
		case R.id.buttonClear:
			Prefs.clear(getApplicationContext());
			encryptedString = null;
			Toast.makeText(getApplicationContext(),
					"Preference data is cleared !!", Toast.LENGTH_LONG).show();

			break;
		}

	}

	public void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View f = activity.getCurrentFocus();
		if (null != f && null != f.getWindowToken()
				&& EditText.class.isAssignableFrom(f.getClass()))
			imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
		else
			activity.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}
}
