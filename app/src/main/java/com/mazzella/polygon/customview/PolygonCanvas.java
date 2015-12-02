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
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mazzella.polygon.polygonzone.R;

import java.util.ArrayList;

public class PolygonCanvas extends View {

    private Point origPoint;
    private int w;
    private int h;
    private ArrayList<TouchPoint> touchPoints;
    private int pointId = 0;
    private Context context;

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

    private void init() {
        setFocusable(true);

        touchPoints = new ArrayList<>();

        int cornerIdsArray = 0;
        int edgeIdsArray = 1;

        Point point1 = new Point();
        point1.x = 50;
        point1.y = 50;
        touchPoints.add(0, new TouchPoint(context, R.drawable.circle_touchpoint, point1, 0));
        touchPoints.get(0).setOtherIds(cornerIdsArray, 1, 3);
        touchPoints.get(0).setOtherIds(edgeIdsArray, 4, 7);

        Point point2 = new Point();
        point2.x = this.w - 100;
        point2.y = 50;
        touchPoints.add(1, new TouchPoint(context, R.drawable.circle_touchpoint, point2, 1));
        touchPoints.get(1).setOtherIds(cornerIdsArray, 0, 3);
        touchPoints.get(1).setOtherIds(edgeIdsArray, 4, 5);

        Point point3 = new Point();
        point3.x = this.w - 100;
        point3.y = this.h - 100;
        touchPoints.add(2, new TouchPoint(context, R.drawable.circle_touchpoint, point3, 2));
        touchPoints.get(2).setOtherIds(cornerIdsArray, 1, 3);
        touchPoints.get(2).setOtherIds(edgeIdsArray, 5, 6);

        Point point4 = new Point();
        point4.x = 50;
        point4.y = this.h - 100;
        touchPoints.add(3, new TouchPoint(context, R.drawable.circle_touchpoint, point4, 3));
        touchPoints.get(3).setOtherIds(cornerIdsArray, 0, 2);
        touchPoints.get(3).setOtherIds(edgeIdsArray, 7, 6);

        Point point12 = new Point();
        point12.x = point2.x / 2;
        point12.y = point1.y;
        touchPoints.add(4, new TouchPoint(context, R.drawable.square_touchpoint, point12, 4));

        Point point23 = new Point();
        point23.x = point2.x;
        point23.y = this.h / 2 - point2.y;
        touchPoints.add(5, new TouchPoint(context, R.drawable.square_touchpoint, point23, 5));

        Point point34 = new Point();
        point34.x = this.w / 2;
        point34.y = point4.y;
        touchPoints.add(6, new TouchPoint(context, R.drawable.square_touchpoint, point34, 6));

        Point point14 = new Point();
        point14.x = point1.x;
        point14.y = this.h / 2 - point1.y;
        touchPoints.add(7, new TouchPoint(context, R.drawable.square_touchpoint, point14, 7));

        areaFill = new Paint();
        areaLine = new Path();

        bitmapColor = new Paint();
        bitmapColor.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(context, R.color.colorAccent), PorterDuff.Mode.SRC_IN));
    }

    private Paint areaFill;
    private Paint bitmapColor;
    private Path areaLine;

    @Override
    protected void onDraw(Canvas canvas) {
        areaFill.setColor(Color.parseColor("#55FF4081"));
        areaFill.setStyle(Paint.Style.FILL);

        areaLine.reset();
        areaLine.moveTo(touchPoints.get(0).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(0).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(4).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(4).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(1).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(1).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(5).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(5).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(2).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(2).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(6).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(6).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(3).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(3).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.lineTo(touchPoints.get(7).getX() + touchPoints.get(0).getWidthOfTouchPoint() / 2, touchPoints.get(7).getY() + touchPoints.get(0).getHeightOfTouchPoint() / 2);
        areaLine.close();
        canvas.drawPath(areaLine, areaFill);

        for (TouchPoint touchPoint : touchPoints) {
            canvas.drawBitmap(touchPoint.getBitmap(), touchPoint.getX(), touchPoint.getY(), bitmapColor);
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
                    int centerX = touchPoint.getX() + touchPoint.getWidthOfTouchPoint();
                    int centerY = touchPoint.getY() + touchPoint.getHeightOfTouchPoint();

                    double radCircle = Math.sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y) * (centerY - Y)));

                    if (radCircle < touchPoint.getWidthOfTouchPoint()) {
                        pointId = touchPoint.getId();
                        touchPoint.setIsLocked();
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (pointId > -1) {
                    if (X > 0 && X < this.w - touchPoints.get(pointId).getWidthOfTouchPoint()) {
                        touchPoints.get(pointId).setX(X);

                        if (pointId <= 3) {
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

                    if (Y > 0 && Y < this.h - touchPoints.get(pointId).getHeightOfTouchPoint()) {
                        touchPoints.get(pointId).setY(Y);

                        if (pointId <= 3) {
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
                            int getX = touchPoints.get(leftMostId).getX() + (touchPoints.get(leftMostId).getWidthOfTouchPoint() / 2);
                            if (getX > 0) {
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
                            int getX = touchPoints.get(rightMostId).getX() + (touchPoints.get(rightMostId).getWidthOfTouchPoint() / 2);
                            if (getX < this.w) {
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
                            int getY = touchPoints.get(topMostId).getY() + (touchPoints.get(topMostId).getHeightOfTouchPoint() / 2);
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
                            int getY = touchPoints.get(bottomMostId).getY() + touchPoints.get(bottomMostId).getHeightOfTouchPoint() / 2;
                            if (getY < this.h) {
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
                break;
        }
        invalidate();
        return true;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        this.w = w;
        this.h = h;

        init();
        super.onSizeChanged(w, h, oldW, oldH);
    }
}
