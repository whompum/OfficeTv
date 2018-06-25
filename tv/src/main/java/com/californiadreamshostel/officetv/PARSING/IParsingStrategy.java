package com.californiadreamshostel.officetv.PARSING;


import android.support.annotation.NonNull;

/**
 * Strategy Pattern used by implementors to define
 * how to handle data.
 */
public interface ParsingStrategy {
    void parseData(@NonNull final String data, @NonNull final String contentType);
}
