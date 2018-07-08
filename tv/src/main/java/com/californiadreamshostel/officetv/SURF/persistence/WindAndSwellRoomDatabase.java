package com.californiadreamshostel.officetv.SURF.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.californiadreamshostel.officetv.SURF.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.SURF.persistence.WindAndSwellDaoInterface;

@Database(entities = {WindAndSwell.class}, version = 1, exportSchema = false)
public abstract class WindAndSwellRoomDatabase extends RoomDatabase{
    public abstract WindAndSwellDaoInterface getDao();
}
