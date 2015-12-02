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
package com.mazzella.polygon.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import com.mazzella.polygon.polygonzone.R;

import java.util.ArrayList;

public class TouchPoint {

    Bitmap bitmap;
    Context mContext;
    Point point;
    int id;
    Boolean isLocked = true;
    ArrayList<Integer> cornerIds = new ArrayList<>();
    ArrayList<Integer> edgeIds = new ArrayList<>();


    public TouchPoint(Context context, int resourceId, Point point, int id) {
        this.id = id;
        bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
        int drawableSize = 18;
        if (resourceId == R.drawable.circle_touchpoint) {
            drawableSize = 22;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (drawableSize * context.getResources().getDisplayMetrics().density), (int) (drawableSize * context.getResources().getDisplayMetrics().density), true);

        mContext = context;
        this.point = point;
    }

    public int getWidthOfTouchPoint() {
        return bitmap.getWidth();
    }

    public int getHeightOfTouchPoint() {
        return bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return point.x;
    }

    public int getY() {
        return point.y;
    }

    public int getId() {
        return id;
    }

    public void setX(int x) {
        point.x = x;
    }

    public void setY(int y) {
        point.y = y;
    }

    public void addY(int y) {
        point.y = point.y + y;
    }

    public void addX(int x) {
        point.x = point.x + x;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public ArrayList<Integer> getCornerIds() {
        return cornerIds;
    }

    public ArrayList<Integer> getEdgeIds() {
        return edgeIds;
    }

    public void setOtherIds(int theArray, int idOne, int idTwo) {
        if (theArray == 0) {
            cornerIds = new ArrayList<>();
            cornerIds.add(0, idOne);
            cornerIds.add(1, idTwo);
        } else {
            edgeIds = new ArrayList<>();
            edgeIds.add(0, idOne);
            edgeIds.add(1, idTwo);
        }
    }
}
