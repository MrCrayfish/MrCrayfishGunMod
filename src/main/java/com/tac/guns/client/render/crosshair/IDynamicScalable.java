package com.tac.guns.client.render.crosshair;

public interface IDynamicScalable {
    void scale(float value);
    float getInitialScale();
    float getHorizontalMovementScale();
    float getVerticalMovementScale();
}
