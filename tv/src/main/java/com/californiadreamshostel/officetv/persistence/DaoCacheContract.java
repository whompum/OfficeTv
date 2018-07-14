package com.californiadreamshostel.officetv.persistence;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * For data to be stored for temporary caching purposes
 * the DAO of that data will inherit from this interface
 * @param <T> The datatype we're working with
 */
public interface DaoCacheContract<T> {

    void insert(@NonNull final T t);
    void insert(final T... t);
    void deleteAll();
    LiveData<List<T>> fetchAll();
}
