
package com.suslovila.cybersus.client.clientProcess.processes.shadowGates;

import com.suslovila.cybersus.utils.SusVec3;

import java.lang.Math;

class ShadowTail {
    public SusVec3 homePos;
    public int tailSpeed;
    public double maxRadius;
    public double radiusChangePerFrame;
    public int timer;
    public SusVec3 aimVec3;

    double radius;
    SusVec3 m;
    SusVec3 k;
    double particleSize;

    public ShadowTail(SusVec3 homePos, int tailSpeed, double maxRadius, double radiusChangePerFrame, SusVec3 aimVec3) {
        this.homePos = homePos;
        this.tailSpeed = tailSpeed;
        this.maxRadius = maxRadius;
        this.radiusChangePerFrame = radiusChangePerFrame;
        this.timer = 0;
        this.aimVec3 = aimVec3;
    }

    public ShadowTail(SusVec3 homePos,
                      double radius,
                      SusVec3 m,
                      SusVec3 k,
                      double particleSize,
                      int timer) {
        this.homePos = homePos;
        this.radius =radius;
        this.m = m;
        this.k = k;
        this.particleSize = particleSize;
        this.timer = timer;
    }

    public double getActualRadius() {
        return Math.min(timer * radiusChangePerFrame, maxRadius);
    }
}

