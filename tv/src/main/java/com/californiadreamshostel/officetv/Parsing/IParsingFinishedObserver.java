package com.californiadreamshostel.officetv.Parsing;

/**
 * Interface callback, called when the data coming in from the network
 * has been parsed into model objects, and is ready to be used by the client.
 */
public interface IParsingFinishedObserver {
    void onDataParsed(final Object data);
}
