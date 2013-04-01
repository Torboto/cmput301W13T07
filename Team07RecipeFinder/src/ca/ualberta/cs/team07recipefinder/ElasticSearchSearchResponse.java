package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Torboto
 * Elastic Search response type used for getting data back from the internet.
 * @param <T> type
 */
public class ElasticSearchSearchResponse<T> {
    int took;
    boolean timed_out;
    transient Object _shards;
    Hits<T> hits;
    boolean exists;    
    
    /**
     * Gives back hit data from search.
     * @return hits from search.
     */
    public Collection<ElasticSearchResponse<T>> getHits() {
        return hits.getHits();        
    }
    
    /**
     * Returns the object for a hit.
     * @return Sources for all the hits from elasticsearch
     */
    public Collection<T> getSources() {
        Collection<T> out = new ArrayList<T>();
        for (ElasticSearchResponse<T> essrt : getHits()) {
            out.add( essrt.getSource() );
        }
        return out;
    }
    
    public String toString() {
        return (super.toString() + ":" + took + "," + _shards + "," + exists + ","  + hits);     
    }
}