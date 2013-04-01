package ca.ualberta.cs.team07recipefinder;

/**
 * @author gcoomber
 * 
 *         Generic View interface for the view classes to implement.
 * 
 * @param <M>
 */
public interface View<M> {
	/**
	 * 
	 * 
	 * @param model
	 */
	public void update(M model);
}
