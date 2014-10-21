package com.example.quietwaiting_2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class InfoServicesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_services);
		Intent i = this.getIntent();
		String service = i.getStringExtra("name_service");
		int id = i.getIntExtra("id_service", 0);
		setTitle(service);
		TextView show_service = (TextView) findViewById(R.id.show_service);
		show_service.setText(service);
		
		//Demande du numero du ticket via JSON
		String number_ticket = String.valueOf(id+1);
		TextView  show_number = (TextView)findViewById(R.id.show_ticket);
		show_number.setText(number_ticket);
	}
}
