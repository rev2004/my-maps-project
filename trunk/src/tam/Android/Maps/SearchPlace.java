package tam.Android.Maps;

import java.util.ArrayList;

import tam.Android.Database.MapDatabase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SearchPlace extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchplace);

		ImageView image = (ImageView) findViewById(R.id.imageA);
		Bitmap bMap = BitmapFactory.decodeResource(getResources(),
				R.drawable.maps);
		Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, 50, 50, true);
		image.setImageBitmap(bMapScaled);

		ImageButton btnSearch = (ImageButton) findViewById(R.id.btnSearch);
		
		final AutoCompleteTextView placeToSearch = (AutoCompleteTextView) findViewById(R.id.placeToSearch); //<< xai` cai nay` de autocomplete
		MapDatabase db = new MapDatabase(getBaseContext());
		final ArrayList<String> dbList = db.getAllLocations();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.mylistview, dbList);
		placeToSearch.setAdapter(adapter);
		//placeToSearch.setText("273 An Duong Vuong, Quan 5, Tp. HCM");
		placeToSearch.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//Dung thread o day cho no' dung` cham. textbox
				/*
				AutoCompleteAPI api = new AutoCompleteAPI(s.toString());
				ArrayList<String> list = api.getResult();
				list.addAll(dbList);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.mylistview, list);
				placeToSearch.setAdapter(adapter);
				*/
			}
		});
		btnSearch.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//AutoCompleteTextView placeToSearch = (AutoCompleteTextView) findViewById(R.id.placeToSearch);
				Intent in = new Intent();
				in.putExtra("placeToSearch", placeToSearch.getText().toString());
				setResult(1, in);
				finish();
			}
		});
	}
}
