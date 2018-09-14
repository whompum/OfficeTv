package com.californiadreamshostel.officetv.Controllers.WeatherSurf;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.Surf.MODELS.Tide;
import com.californiadreamshostel.officetv.Surf.persistence.TideDaoInterface;
import com.californiadreamshostel.officetv.Surf.persistence.TideRoomDatabase;

import java.util.List;

public class TideRepo {

    private static TideRepo instance;

    private TideDaoInterface accessor;
    private LiveData<List<Tide>> data;

    private TideRepo(@NonNull final Context c){

        this.accessor = Room.databaseBuilder(c, TideRoomDatabase.class, "tide.db")
                .build()
                .getDao();

        this.data = accessor.fetchAll();
    }

    public static TideRepo obtain(@NonNull final Context context){
        if(instance == null)
            instance = new TideRepo(context.getApplicationContext());

    return instance;
    }

    public LiveData<List<Tide>> getData(){
        return data;
    }

    public synchronized void renewCache(@NonNull final Tide... tides){

        final boolean singleInsert = tides.length == 1;

        new Thread(){
            @Override
            public void run() {

                accessor.deleteAll();

                if(singleInsert)
                    accessor.insert(tides[0]);

                else
                    accessor.insert(tides);

            }
        }.start();

    }

}
