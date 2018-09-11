package com.californiadreamshostel.officetv.Weather.model;

import com.californiadreamshostel.officetv.Weather.model.Initializer;

import java.util.Set;


public interface Provider {
    Set<Initializer> fetchInitializers();
}
