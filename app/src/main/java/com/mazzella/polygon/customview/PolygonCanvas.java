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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.JsonArray;
import com.mazzella.polygon.polygonzone.R;

import java.util.ArrayList;

public class PolygonCanvas extends View {

    private Point origPoint;
    private int w;
    private int h;
    private ArrayList<TouchPoint> touchPoints;
    private int pointId = 0;
    private Context context;
    private String colorCode;


    public PolygonCanvas(Context context) {
        super(context);
        this.context = context;
    }

    public PolygonCanvas(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PolygonCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void init(String colorCode, JsonArray zoneDefPoints) {
        setFocusable(true);

        this.colorCode = colorCode;

        touchPoints = new ArrayList<>();

        int cornerIdsArray = 0;
        int edgeIdsArray = 1;

        int halfCircle = (int) ((22 * context.getResources().getDisplayMetrics().density) / 2);
        int halfSquare = (int) ((18 * context.getResources().getDisplayMetrics().density) / 2);

        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();
        Point point4 = new Point();
        Point point12 = new Point();
        Point point23 = new Point();
        Point point14 = new Point();
        Point point34 = new Point();

        if (zoneDefPoints.size() > 0) {
            point1.x = getPositionFromPercent(zoneDefPoints.get(0).getAsJsonArray().get(0).getAsInt(), 0) - halfCircle;
            point1.y = getPositionFromPercent(zoneDefPoints.get(0).getAsJsonArray().get(1).getAsInt(), 1) - halfCircle;
            point2.x = getPositionFromPercent(zoneDefPoints.get(2).getAsJsonArray().get(0).getAsInt(), 0) - halfCircle;
            point2.y = getPositionFromPercent(zoneDefPoints.get(2).getAsJsonArray().get(1).getAsInt(), 1) - halfCircle;
            point3.x = getPositionFromPercent(zoneDefPoints.get(4).getAsJsonArray().get(0).getAsInt(), 0) - halfCircle;
            point3.y = getPositionFromPercent(zoneDefPoints.get(4).getAsJsonArray().get(1).getAsInt(), 1) - halfCircle;
            point4.x = getPositionFromPercent(zoneDefPoints.get(6).getAsJsonArray().get(0).getAsInt(), 0) - halfCircle;
            point4.y = getPositionFromPercent(zoneDefPoints.get(6).getAsJsonArray().get(1).getAsInt(), 1) - halfCircle;
            point12.x = getPositionFromPercent(zoneDefPoints.get(1).getAsJsonArray().get(0).getAsInt(), 0) - halfSquare;
            point12.y = getPositionFromPercent(zoneDefPoints.get(1).getAsJsonArray().get(1).getAsInt(), 1) - halfSquare;
            point23.x = getPositionFromPercent(zoneDefPoints.get(3).getAsJsonArray().get(0).getAsInt(), 0) - halfSquare;
            point23.y = getPositionFromPercent(zoneDefPoints.get(3).getAsJsonArray().get(1).getAsInt(), 1) - halfSquare;
            point34.x = getPositionFromPercent(zoneDefPoints.get(5).getAsJsonArray().get(0).getAsInt(), 0) - halfSquare;
            point34.y = getPositionFromPercent(zoneDefPoints.get(5).getAsJsonArray().get(1).getAsInt(), 1) - halfSquare;
            point14.x = getPositionFromPercent(zoneDefPoints.get(7).getAsJsonArray().get(0).getAsInt(), 0) - halfSquare;
            point14.y = getPositionFromPercent(zoneDefPoints.get(7).getAsJsonArray().get(1).getAsInt(), 1) - halfSquare;
        } else {
            point1.x = 50;
            point1.y = 50;
            point2.x = this.w - 100;
            point2.y = 50;
            point3.x = this.w - 100;
            point3.y = this.h - 100;
            point4.x = 50;
            point4.y = this.h - 100;
            point12.x = (point1.x + point2.x) / 2;
            point12.y = point1.y;
            point23.x = point2.x;
            point23.y = (point2.y + point3.y) / 2;
            point34.x = (point3.x + point4.x) / 2;
            point34.y = point4.y;
            point14.x = point1.x;
            point14.y = (point1.y + point4.y) / 2;
        }

        touchPoints.add(0, new TouchPoint(context, R.drawable.circle_touchpoint, point1, 0));
        touchPoints.add(1, new TouchPoint(context, R.drawable.square_touchpoint, point12, 1));
        touchPoints.add(2, new TouchPoint(context, R.drawable.circle_touchpoint, point2, 2));
        touchPoints.add(3, new TouchPoint(context, R.drawable.square_touchpoint, point23, 3));
        touchPoints.add(4, new TouchPoint(context, R.drawable.circle_touchpoint, point3, 4));
        touchPoints.add(5, new TouchPoint(context, R.drawable.square_touchpoint, point34, 5));
        touchPoints.add(6, new TouchPoint(context, R.drawable.circle_touchpoint, point4, 6));
        touchPoints.add(7, new TouchPoint(context, R.drawable.square_touchpoint, point14, 7));

        touchPoints.get(0).setOtherIds(cornerIdsArray, 2, 6);
        touchPoints.get(0).setOtherIds(edgeIdsArray, 1, 7);
        touchPoints.get(2).setOtherIds(cornerIdsArray, 0, 4);
        touchPoints.get(2).setOtherIds(edgeIdsArray, 1, 3);
        touchPoints.get(4).setOtherIds(cornerIdsArray, 2, 6);
        touchPoints.get(4).setOtherIds(edgeIdsArray, 3, 5);
        touchPoints.get(6).setOtherIds(cornerIdsArray, 0, 4);
        touchPoints.get(6).setOtherIds(edgeIdsArray, 7, 5);

        if (zoneDefPoints.size() > 0) {
            for (int x = 0; x <= 7; x++){
                if (!touchPoints.get(x).getIsCornerPoint()) {
                    touchPoints.get(x).setIsLocked();
                }
            }
        }

        areaFill = new Paint();
        areaLine = new Path();

        bitmapColor = new Paint();
        bitmapColor.setColorFilter(new PorterDuffColorFilter(Color.parseColor(colorCode), PorterDuff.Mode.SRC_IN));

        invalidate();
    }

    public JsonArray getPointCoordinates() {
        JsonArray allPoints = new JsonArray();

        Boolean anyModified = false;

        for (TouchPoint touchPoint : touchPoints) {
            if (touchPoint.getIsModified()) {
                anyModified = true;
            }

            JsonArray singlePoints = new JsonArray();
            singlePoints.add(getPercentFromPosition(touchPoint.getMidX(), 0));
            singlePoints.add(getPercentFromPosition(touchPoint.getMidY(), 1));

            allPoints.add(singlePoints);
        }

        if (anyModified) {
            return allPoints;
        } else {
            return new JsonArray();
        }
    }

    private int getPositionFromPercent(int position, int XorY) {
        if (XorY == 0) {
            //Then it's X
            return (position * this.w) / 10000;
        } else {
            //Then it's Y
            return (position * this.h) / 10000;
        }
    }

    private int getPercentFromPosition(int position, int XorY) {
        if (XorY == 0) {
            return (position * 10000) / this.w;
        } else {
            return (position * 10000) / this.h;
        }
    }

    public void clearTouchPoints() {
        touchPoints = new ArrayList<>();
        areaFill = null;
        invalidate();
    }

    private Paint areaFill;
    private Paint bitmapColor;
    private Path areaLine;

    @Override
    protected void onDraw(Canvas canvas) {
        if (areaFill != null) {
            areaFill.setColor(Color.parseColor("#55" + colorCode.substring(1)));
            areaFill.setStyle(Paint.Style.FILL);

            areaLine.reset();
            areaLine.moveTo(touchPoints.get(0).getMidX(), touchPoints.get(0).getMidY());
            areaLine.lineTo(touchPoints.get(1).getMidX(), touchPoints.get(1).getMidY());
            areaLine.lineTo(touchPoints.get(2).getMidX(), touchPoints.get(2).getMidY());
            areaLine.lineTo(touchPoints.get(3).getMidX(), touchPoints.get(3).getMidY());
            areaLine.lineTo(touchPoints.get(4).getMidX(), touchPoints.get(4).getMidY());
            areaLine.lineTo(touchPoints.get(5).getMidX(), touchPoints.get(5).getMidY());
            areaLine.lineTo(touchPoints.get(6).getMidX(), touchPoints.get(6).getMidY());
            areaLine.lineTo(touchPoints.get(7).getMidX(), touchPoints.get(7).getMidY());
            areaLine.close();
            canvas.drawPath(areaLine, areaFill);

            for (TouchPoint touchPoint : touchPoints) {
                canvas.drawBitmap(touchPoint.getBitmap(), touchPoint.getX(), touchPoint.getY(), bitmapColor);
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();

        int X = (int) event.getX();
        int Y = (int) event.getY();

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                pointId = -1;
                origPoint = new Point(X, Y);
                for (TouchPoint touchPoint : touchPoints) {
                    int centerX = touchPoint.getX();// + touchPoint.getWidthOfTouchPoint();
                    int centerY = touchPoint.getY();// + touchPoint.getHeightOfTouchPoint();

                    double radCircle = Math.sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));

                    if (radCircle < touchPoint.getWidthOfTouchPoint()) {
                        pointId = touchPoint.getId();
                        touchPoint.setIsLocked();
                        touchPoint.setIsModified();
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (pointId > -1) {
                    if (X + (touchPoints.get(pointId).getWidthOfTouchPoint()/2) > 0 && X < this.w - (touchPoints.get(pointId).getWidthOfTouchPoint()/2)) {
                        touchPoints.get(pointId).setX(X);

                        if (touchPoints.get(pointId).getIsCornerPoint()) {
                            ArrayList<Integer> edgeIds = touchPoints.get(pointId).getEdgeIds();
                            ArrayList<Integer> cornerIds = touchPoints.get(pointId).getCornerIds();

                            for (int incrementer = 0; incrementer < edgeIds.size(); incrementer++) {
                                if (touchPoints.get(edgeIds.get(incrementer)).getIsLocked()) {
                                    int cornerX = touchPoints.get(cornerIds.get(incrementer)).getX();
                                    touchPoints.get(edgeIds.get(incrementer)).setX((X + cornerX) / 2);
                                }
                            }
                        }
                    }

                    Log.d("Poly", "Y: " + Y + " and height: " + touchPoints.get(pointId).getHeightOfTouchPoint() + " and y: " + this.h);
                    if (Y + (touchPoints.get(pointId).getHeightOfTouchPoint()/2) > 0 && Y < this.h - (touchPoints.get(pointId).getHeightOfTouchPoint()/2)) {
                        touchPoints.get(pointId).setY(Y);

                        if (touchPoints.get(pointId).getIsCornerPoint()) {
                            ArrayList<Integer> edgeIds = touchPoints.get(pointId).getEdgeIds();
                            ArrayList<Integer> cornerIds = touchPoints.get(pointId).getCornerIds();

                            for (int incrementer = 0; incrementer < edgeIds.size(); incrementer++) {
                                if (touchPoints.get(edgeIds.get(incrementer)).getIsLocked()) {
                                    int cornerY = touchPoints.get(cornerIds.get(incrementer)).getY();
                                    touchPoints.get(edgeIds.get(incrementer)).setY((Y + cornerY) / 2);
                                }
                            }
                        }
                    }
                } else {
                    if (touchPoints.size() > 0) {
                        if (origPoint != null) {
                            int diffX = X - origPoint.x;
                            int diffY = Y - origPoint.y;
                            origPoint.x = X;
                            origPoint.y = Y;

                            int leftMost = 0;
                            int rightMost = 0;
                            int topMost = 0;
                            int bottomMost = 0;

                            int leftMostId = 0, rightMostId = 0, topMostId = 0, bottomMostId = 0;

                            for (TouchPoint touchPoint : touchPoints) {
                                if (touchPoint.getX() < leftMost) {
                                    leftMost = touchPoint.getX();
                                    leftMostId = touchPoint.getId();
                                }
                                if (touchPoint.getX() > rightMost) {
                                    rightMost = touchPoint.getX();
                                    rightMostId = touchPoint.getId();
                                }
                                if (touchPoint.getY() < topMost) {
                                    topMost = touchPoint.getY();
                                    topMostId = touchPoint.getId();
                                }
                                if (touchPoint.getY() > bottomMost) {
                                    bottomMost = touchPoint.getY();
                                    bottomMostId = touchPoint.getId();
                                }
                            }

                            if (diffX < 0) {
                                int getX = touchPoints.get(leftMostId).getMidX();
                                if (getX > 0) {
                                    touchPoints.get(leftMostId).setIsModified();

                                    int tempDiffX = diffX;
                                    if (getX + diffX < 0) {
                                        tempDiffX = getX * -1;
                                    }

                                    touchPoints.get(0).addX(tempDiffX);
                                    touchPoints.get(1).addX(tempDiffX);
                                    touchPoints.get(2).addX(tempDiffX);
                                    touchPoints.get(3).addX(tempDiffX);
                                    touchPoints.get(4).addX(tempDiffX);
                                    touchPoints.get(5).addX(tempDiffX);
                                    touchPoints.get(6).addX(tempDiffX);
                                    touchPoints.get(7).addX(tempDiffX);
                                }
                            } else {
                                int getX = touchPoints.get(rightMostId).getMidX();
                                if (getX < this.w) {
                                    touchPoints.get(rightMostId).setIsModified();

                                    int tempDiffX = diffX;
                                    if (getX + diffX > this.w) {
                                        tempDiffX = this.w - getX;
                                    }

                                    touchPoints.get(0).addX(tempDiffX);
                                    touchPoints.get(1).addX(tempDiffX);
                                    touchPoints.get(2).addX(tempDiffX);
                                    touchPoints.get(3).addX(tempDiffX);
                                    touchPoints.get(4).addX(tempDiffX);
                                    touchPoints.get(5).addX(tempDiffX);
                                    touchPoints.get(6).addX(tempDiffX);
                                    touchPoints.get(7).addX(tempDiffX);
                                }
                            }

                            if (diffY < 0) {
                                int getY = touchPoints.get(topMostId).getMidY();

                                touchPoints.get(topMostId).setIsModified();

                                if (getY > 0) {
                                    int tempDiffY = diffY;
                                    if (getY + tempDiffY < 0) {
                                        tempDiffY = getY * -1;
                                    }

                                    touchPoints.get(0).addY(tempDiffY);
                                    touchPoints.get(1).addY(tempDiffY);
                                    touchPoints.get(2).addY(tempDiffY);
                                    touchPoints.get(3).addY(tempDiffY);
                                    touchPoints.get(4).addY(tempDiffY);
                                    touchPoints.get(5).addY(tempDiffY);
                                    touchPoints.get(6).addY(tempDiffY);
                                    touchPoints.get(7).addY(tempDiffY);
                                }
                            } else {
                                int getY = touchPoints.get(bottomMostId).getMidY();
                                if (getY < this.h) {

                                    touchPoints.get(bottomMostId).setIsModified();

                                    int tempDiffY = diffY;
                                    if (getY + diffY > this.h) {
                                        tempDiffY = this.h - getY;
                                    }

                                    touchPoints.get(0).addY(tempDiffY);
                                    touchPoints.get(1).addY(tempDiffY);
                                    touchPoints.get(2).addY(tempDiffY);
                                    touchPoints.get(3).addY(tempDiffY);
                                    touchPoints.get(4).addY(tempDiffY);
                                    touchPoints.get(5).addY(tempDiffY);
                                    touchPoints.get(6).addY(tempDiffY);
                                    touchPoints.get(7).addY(tempDiffY);
                                }
                            }
                        }
                    }
                }
                break;
        }
        invalidate();
        return true;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.w = w;
        this.h = h;

        init("#FFFFFF", new JsonArray());
        super.onSizeChanged(w, h, oldW, oldH);
    }
}
