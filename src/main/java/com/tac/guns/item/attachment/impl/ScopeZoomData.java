package com.tac.guns.item.attachment.impl;

public class ScopeZoomData
{
    private float fovZoom;
    private float drCropZoom;

    public float getFovZoom() {return fovZoom;}
    public float getDrCropZoom() {return drCropZoom;}
    public ScopeZoomData(float fovZoom, float drCropZoom)
    {
        this.fovZoom = fovZoom;
        this.drCropZoom = drCropZoom;
    }
}
