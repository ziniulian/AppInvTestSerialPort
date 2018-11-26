package com.invengo.basetmp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Ma extends Activity {
//	private Rd rd = new Rd("/dev/ttyS4", 115200);
	private Rd rd = new Rd("/dev/ttyMT1", 115200);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ma);

		Button b = (Button) findViewById(R.id.send);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rd.open();
				rd.send(new byte[] {0x55, 0x00, 0x02, (byte)0xD2, 0x00, (byte)0xEC, 0x24});
			}
		});

		b = (Button) findViewById(R.id.stop);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				rd.close();
			}
		});
	}
}
