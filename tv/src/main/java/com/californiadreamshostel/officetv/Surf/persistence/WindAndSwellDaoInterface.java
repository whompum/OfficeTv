package com.californiadreamshostel.officetv.Surf.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Surf.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.persistence.DaoCacheContract;

import java.util.List;

@Dao
public interface WindAndSwellDaoInterface extends DaoCacheContract<WindAndSwell>{

    @Override
    @Insert
    void insert(@NonNull final WindAndSwell windAndSwell);

    @Override
    @Insert
    void insert(@NonNull final WindAndSwell... t);

    @Override
    @Query("DELETE FROM WindAndSwell")
    void deleteAll();

    @Override
    @Query("SELECT * FROM WindAndSwell")
    LiveData<List<WindAndSwell>> fetchAll();
}
