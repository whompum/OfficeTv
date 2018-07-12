/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.californiadreamshostel.officetv.CONTROLLERS;

import android.app.Activity;
import android.os.Bundle;

import com.californiadreamshostel.officetv.CONTROLLERS.weather$surf.WeatherSurfController;
import com.californiadreamshostel.officetv.R;

/******
 *MainActivity class that loads {@link RentalSlideFragment} and {@link ShelfFragment}.
 ******/
public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_root_wireframe);

        getFragmentManager().beginTransaction()
                .add(R.id.id_shelf_container, ShelfFragment.newInstance(null)).commit();

        getFragmentManager().beginTransaction()
                .add(R.id.id_slides_container, RentalSlideFragment.newInstance()).commit();

        //Initialize the Weather Surf job service
        WeatherSurfController.start(this);

    }

}


