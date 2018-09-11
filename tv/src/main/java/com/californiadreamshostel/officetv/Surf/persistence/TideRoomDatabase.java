package com.californiadreamshostel.officetv.Surf.persistence;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.californiadreamshostel.officetv.Surf.MODELS.Tide;
import com.californiadreamshostel.officetv.Surf.persistence.TideDaoInterface;

@Database(entities = {Tide.class}, version = 2, exportSchema = false)
public abstract class TideRoomDatabase extends RoomDatabase {
    public abstract TideDaoInterface getDao();
}
