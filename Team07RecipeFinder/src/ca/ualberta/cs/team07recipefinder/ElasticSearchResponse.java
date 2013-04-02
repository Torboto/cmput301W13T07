package ca.ualberta.cs.team07recipefinder;

/**
 * Elastic Search response type used for getting data back from the internet.
 * 
 * @param <T>
 *            type
 * 
 * @author Torboto
 */
public class ElasticSearchResponse<T> {
	String _index;
	String _type;
	String _id;
	int _version;
	boolean exists;
	T _source;
	double max_score;

	/**
	 * Returns url of hit.
	 * 
	 * @return Source of hit.
	 */
	public T getSource() {
		return _source;
	}
}