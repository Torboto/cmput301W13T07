package ca.ualberta.cs.team07recipefinder;

/**
 * @author Torboto
 * Elastic Search response type used for getting data back from the internet.
 * @param <T> type
 */
public class ElasticSearchResponse<T> {
    String _index;
    String _type;
    String _id;
    int _version;
    boolean exists;
    T _source;
    double max_score;
    public T getSource() {
        return _source;
    }
}