package com.californiadreamshostel.officetv.Controllers.weather$surf;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.californiadreamshostel.officetv.SURF.MODELS.WindAndSwell;
import com.californiadreamshostel.officetv.SURF.persistence.WindAndSwellDaoInterface;
import com.californiadreamshostel.officetv.SURF.persistence.WindAndSwellRoomDatabase;

import java.util.List;

public class WindSwellRepo {

    private static WindSwellRepo instance;

    private WindAndSwellDaoInterface accessor;

    private LiveData<List<WindAndSwell>> data;

    private WindSwellRepo(@NonNull final Context c){

        accessor = Room.databaseBuilder(c.getApplicationContext(), WindAndSwellRoomDatabase.class, "wind_swell.db")
                .build().getDao();

        data = accessor.fetchAll();

    }

    public static WindSwellRepo obtain(@NonNull final Context c){
        if(instance == null)
            instance = new WindSwellRepo(c);

        return instance;
    }

    public LiveData<List<WindAndSwell>> getData(){
        return data;
    }

    public void renewCache(@NonNull final WindAndSwell... data){

        new Thread(){
            @Override
            public void run() {

                final boolean singleInsert = data.length == 1;

                accessor.deleteAll();

                if(singleInsert)
                    accessor.insert(data[0]);
                else
                    accessor.insert(data);

            }
        }.start();


    }


}













