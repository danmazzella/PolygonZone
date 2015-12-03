/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.mazzella.polygon.polygonzone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mazzella.polygon.customview.PolygonCanvas;

public class PolygonZone extends AppCompatActivity {

    PolygonCanvas polygonCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_zone);

        polygonCanvas = (PolygonCanvas) findViewById(R.id.polygon_canvas);

//        polygonCanvas.getLayoutParams().width = getResources().getDisplayMetrics().widthPixels;
//        polygonCanvas.getLayoutParams().height = (int) (getResources().getDisplayMetrics().widthPixels * .56429330);

        JsonArray jsonArray = new JsonArray();
        polygonCanvas.init("#FFFFFF", jsonArray);

        findViewById(R.id.init).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                JsonParser jsonParser = new JsonParser();
//                JsonObject jsonObject = jsonParser.parse("{\"points\":[[2286,3053],[6000,1781],[9714,509],[9714,5000],[9714,9491],[4929,5293],[286,9491],[1286,6272]]}").getAsJsonObject();
//                JsonArray jsonArray = jsonObject.getAsJsonArray("points");
                JsonArray jsonArray = new JsonArray();
                polygonCanvas.init("#FFFFFF", jsonArray);
            }
        });

        findViewById(R.id.predefined).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse("{\"points\":[[2286,3053],[6000,1781],[9714,509],[9714,5000],[9714,9491],[4929,5293],[286,9491],[1286,6272]]}").getAsJsonObject();
                JsonArray jsonArray = jsonObject.getAsJsonArray("points");
                polygonCanvas.init("#FFFFFF", jsonArray);
            }
        });

        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                polygonCanvas.clearTouchPoints();
            }
        });
    }
}
