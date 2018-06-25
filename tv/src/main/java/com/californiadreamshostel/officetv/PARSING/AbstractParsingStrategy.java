package com.californiadreamshostel.officetv.PARSING;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


/**
 * Base strategy class for parsing strategies.
 * Subclasses are responsible for setting the generic type of this class
 * @param <T> The object type we're working with
 */
public abstract class AbstractParsingStrategy<T> implements IParsingStrategy {

    private T data;
    private IParsingObserver observer;

    public AbstractParsingStrategy(){

    }

    public AbstractParsingStrategy(@NonNull final IParsingObserver observer){
        this.observer = observer;
    }

    public abstract void parseData(@NonNull String response, @NonNull String contentType);

    public void setObserver(@NonNull IParsingObserver IParsingObserver) {
        this.observer = IParsingObserver;
    }

    protected void setData(@Nullable final T t){
        this.data = t;
        if(t != null)
            notifyObserver();
    }

    protected void notifyObserver(){
        if(observer != null)
            observer.onDataParsed(getData());
    }

    @Nullable
    public T getData() {
        return data;
    }
}
