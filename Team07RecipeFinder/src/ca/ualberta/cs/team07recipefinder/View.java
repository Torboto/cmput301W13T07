package ca.ualberta.cs.team07recipefinder;

/**
 * 
 * Generic View interface for the view classes to implement.
 * 
 * @param <M>
 * 
 * @author gcoomber
 */
public interface View<M> {
	/**
	 * 
	 * 
	 * @param model
	 */
	public void update(M model);
}
