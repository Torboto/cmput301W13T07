package ca.ualberta.cs.team07recipefinder;

import java.util.Collection;

/**
 * Gets and returns a collection of hits from elastic search server.
 * 
 * @param <T>
 *            type
 * 
 * @author Torboto
 */
public class Hits<T> {
	/**
	 * Total number of hits.
	 */
	int total;
	/**
     * 
     */
	double max_score;
	/**
	 * Collection of hit data.
	 */
	Collection<ElasticSearchResponse<T>> hits;

	/**
	 * Returns a collection of hits for the search
	 * 
	 * @return All hits for the query from elasticsearch
	 */
	public Collection<ElasticSearchResponse<T>> getHits() {
		return hits;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return (super.toString() + "," + total + "," + max_score + "," + hits);
	}
}