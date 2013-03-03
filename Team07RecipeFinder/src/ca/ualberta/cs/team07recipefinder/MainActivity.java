package ca.ualberta.cs.team07recipefinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	        TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
	        tabHost.setup();

	        TabSpec spec1=tabHost.newTabSpec("tab_pantry");
	        spec1.setContent(R.id.tab1);
	        spec1.setIndicator("Pantry");

	        TabSpec spec2=tabHost.newTabSpec("tab_search");
	        spec2.setIndicator("Search");
	        spec2.setContent(R.id.tab2);

	        TabSpec spec3=tabHost.newTabSpec("tab_myrecipes");
	        spec3.setIndicator("MyRecipes");
	        spec3.setContent(R.id.tab3);

	        tabHost.addTab(spec1);
	        tabHost.addTab(spec2);
	        tabHost.addTab(spec3);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		//GC - MyRecipes Tab
		
	}
}
