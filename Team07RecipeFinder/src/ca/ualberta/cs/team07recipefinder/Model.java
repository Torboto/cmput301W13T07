package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

// GC - Generic Model abstract class for the model classes to implement.
// The class follows the format of the example in the class notes.

public abstract class Model<V extends View> {
	private ArrayList<V> views;
	
	protected Model() {
		views = new ArrayList<V>();
	}
	
	public void addView( V view ) {
		if (! views.contains( view )) {
			views.add( view );
		}
	}
	
	public void deleteView( V view ) {
		views.remove( view );
	}
	
	public void notifyViews() {
		for (V view : views) {
			view.update( this );
		}
	}
}
