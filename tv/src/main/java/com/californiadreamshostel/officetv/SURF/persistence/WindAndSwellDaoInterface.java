package com.californiadreamshostel.officetv.SURF.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.SURF.MODELS.WindAndSwell;
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
    List<WindAndSwell> fetchAll();
}
