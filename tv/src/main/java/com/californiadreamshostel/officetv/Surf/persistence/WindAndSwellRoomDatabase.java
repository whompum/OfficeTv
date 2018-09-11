package com.californiadreamshostel.officetv.Surf.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.californiadreamshostel.officetv.Surf.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.Surf.persistence.WindAndSwellDaoInterface;

@Database(entities = {WindAndSwell.class}, version = 1, exportSchema = false)
public abstract class WindAndSwellRoomDatabase extends RoomDatabase{
    public abstract WindAndSwellDaoInterface getDao();
}
