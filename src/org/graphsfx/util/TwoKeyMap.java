package org.graphsfx.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Urs on 9/30/2017.
 */
public class TwoKeyMap <K, V> implements Cloneable{

    @Override
    public Object clone(){
        TwoKeyMap<K, V> clone;
        try{
            clone = (TwoKeyMap<K, V>)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }

        // Deep copy of the Two Key Map
        for(K key : myMap.keySet()){
            clone.myMap.put(key, (HashMap<K,V>)myMap.get(key).clone());
        }

        return clone;
    }

    /**
     * Returns whether the key pair exists in map
     * @param key1 first key to look up object
     * @param key2 second key to look up object
     * @return 'true' if found, 'false' otherwise
     */
    public boolean containsKeyPair(K key1, K key2){
        return get(key1, key2) != null;
    }

    /**
     * Returns whether the map is empty
     */
    public boolean isEmpty(){
        return this.myMap.isEmpty();
    }

    // Getters & Setters ===============================================================================================

    /**
     * Returns the value mapped to the pair of key1 and key2. Order of the pairs does not matter
     * @param key1 first key to look up object
     * @param key2 second key to look up object
     * @return value mapped to keys, null otherwise
     */
    public V get(K key1, K key2){
        V value = null;

        // Check key1 -> key2 pair
        if(myMap.containsKey(key1)){
            if(myMap.get(key1).containsKey(key2)){
                value = myMap.get(key1).get(key2);
            }
        }
        // Check key2 -> key1 pair
        else if(myMap.containsKey(key2)){
            if(myMap.get(key2).containsKey(key1)){
                value = myMap.get(key2).get(key1);
            }
        }

        return value;
    }

    /**
     * Adds two entries to the map where both key1 and key2 are used for both dimensions of the map
     * @param key1 first key of the combination
     * @param key2 second key of the combination
     * @param value value to be added
     */
    public void putBidirectional(K key1, K key2, V value){
        putUnidirectional(key1,key2, value);
        putUnidirectional(key2, key1, value);
    }

    /**
     * Adds a single entry to map where the key to the first dimension of the map is key1 and the second dimension is
     * key2.
     * @param key1 key for the first dimension
     * @param key2 key for the second dimension
     * @param value value to be added
     */
    public void putUnidirectional(K key1, K key2, V value){

        if(myMap.containsKey(key1)){
            myMap.get(key1).put(key2, value);
        }
        else{
            HashMap<K, V> secondMap = new HashMap<K, V>();
            secondMap.put(key2, value);
            myMap.put(key1, secondMap);
        }
    }

    /**
     * Removes entries from the map where both key1 and key2 are used for both dimensions of the map
     * @param key1 first key of the combination
     * @param key2 second key of the combination
     * @return value removed, null if not found
     */
    public V removeBidirectional(K key1, K key2){
        V value1 = null;
        V value2 = null;

        value1 = removeUnidirectional(key1, key2);
        value2 = removeUnidirectional(key2, key1);

        if(value1 != null ){
            return value1;
        }
        else{
            return value2;
        }
    }

    /**
     * Removes a key value pair only where key1 is used for dimension 1 and key2 for dimension 2.
     * @param key1 key for the first dimension
     * @param key2 key for the second dimension
     * @return value removed from the map. null if not found
     */
    public V removeUnidirectional(K key1, K key2){
        V value = null;
        if(this.myMap.containsKey(key1)){
            if(this.myMap.get(key1).containsKey(key2)){
                value = this.myMap.get(key1).remove(key2);
                if(this.myMap.get(key1).isEmpty()){
                    this.myMap.remove(key1);
                }
            }
        }

        return value;
    }

    public int size(){
        return this.myMap.size();
    }

    /**
     *
     * @return all values in the map
     */
    public Collection<V> values(){
        Collection<V> values = new HashSet<V>();
        for(K key1 : this.myMap.keySet()){
            for(K key2 : this.myMap.get(key1).keySet()){
                values.add(this.myMap.get(key1).get(key2));
            }
        }

        return values;
    }


    // Private Fields ==================================================================================================

    private HashMap<K, HashMap<K,V>>  myMap = new HashMap();
}
