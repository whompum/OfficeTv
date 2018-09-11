package com.californiadreamshostel.officetv.SURF.persistence;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.californiadreamshostel.officetv.SURF.MODELS.Tide;
import com.californiadreamshostel.officetv.SURF.persistence.TideDaoInterface;

@Database(entities = {Tide.class}, version = 2, exportSchema = false)
public abstract class TideRoomDatabase extends RoomDatabase {
    public abstract TideDaoInterface getDao();
}
