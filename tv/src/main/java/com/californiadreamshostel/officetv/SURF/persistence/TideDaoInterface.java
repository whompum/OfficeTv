package com.californiadreamshostel.officetv.SURF.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.persistence.DaoCacheContract;
import com.californiadreamshostel.officetv.SURF.MODELS.Tide;

import java.util.List;

@Dao
public interface TideDaoInterface extends DaoCacheContract<Tide> {

    @Override
    @Insert
    void insert(@NonNull final Tide... tides);

    @Override
    @Insert
    void insert(@NonNull final Tide tide);

    @Override
    @Query("DELETE FROM Tide")
    void deleteAll();

    @Override
    @Query("SELECT * FROM Tide")
    LiveData<List<Tide>> fetchAll();
}
