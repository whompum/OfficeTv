package com.californiadreamshostel.officetv.CONTROLLERS;


import android.support.annotation.NonNull;
import android.util.Log;

import com.californiadreamshostel.firebaseclient.DataSchema;
import com.californiadreamshostel.firebaseclient.FirebaseController;
import com.californiadreamshostel.firebaseclient.ReferenceItem;
import com.californiadreamshostel.firebaseclient.SimpleEventListener;

import java.util.ArrayList;
import java.util.List;

public class RentalsDataController implements RemoteDataReceiver{

    private static RentalsDataController instance;

    private FirebaseController controller;

    private List<ReferenceItem> data = new ArrayList<>();

    private RemoteDataReceiver client;

    private boolean onFirstLoad = true;

    private RentalsDataController(){
        controller = new FirebaseController(DataSchema.RENTALS, ((referenceItem, integer) -> {
            onDataReceived(referenceItem, integer);
            return null;
        }));
    }

    public static RentalsDataController obtain(){
        if(instance == null)
            instance = new RentalsDataController();


        return instance;
    }

    @Override
    public void onDataReceived(ReferenceItem item, int changeType) {
        //Store in List
        if(changeType == SimpleEventListener.CHANGE){


            //Find the item that changed in our rentals
            for(ReferenceItem a: data)
                if(a.getReference().equals(item.getReference())) //Now find the differences between children
                    for(ReferenceItem child: a.getChildren()){
                        final ReferenceItem changedChild = item.findChildBy(child.getReference());
                        if(changedChild == null) continue;
                        child.setValue(changedChild.getValue());
                    }
            if(client != null)
                client.onDataReceived(item, changeType);
        }


        boolean dataExists = false;

        for(ReferenceItem items: data)
            if(items.getReference().equals(item.getReference()))
                dataExists = true;

        if(changeType == SimpleEventListener.ADDED && !dataExists) {
            data.add(item);
            if(client != null)
                client.onDataReceived(item, changeType);
        }

        if(changeType == SimpleEventListener.REMOVED && dataExists) {

            ReferenceItem itemToRemove = null;

            for(ReferenceItem items: data)
                if(items.getReference().equals(item.getReference()))
                    itemToRemove = items;

            if(itemToRemove != null) data.remove(itemToRemove);

            if(client != null)
                client.onDataReceived(item, changeType);
        }


    }

    public List<ReferenceItem> fetchData(){
        return data;
    }


    public void subscribe(@NonNull RemoteDataReceiver client){
        this.client = client;
    }

    public void unsubscribe(){
        this.client = null;
    }

    public void register(){
        controller.register();
    }

    public void unregister(){
        controller.unRegister();
    }

    public interface OnDataReady{
        void onDataReady();
    }

}
