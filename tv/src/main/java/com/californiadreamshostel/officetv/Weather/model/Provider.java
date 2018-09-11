package com.californiadreamshostel.officetv.WEATHER.model;

import com.californiadreamshostel.officetv.WEATHER.model.Initializer;

import java.util.Set;


public interface Provider {
    Set<Initializer> fetchInitializers();
}
