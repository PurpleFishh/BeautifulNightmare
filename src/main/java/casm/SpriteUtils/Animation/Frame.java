package casm.SpriteUtils.Animation;

import casm.SpriteUtils.Sprite;

public class Frame {

    private Sprite frameTexture;
    private double frameTime = 0;

    public Frame(Sprite frameTexture, double frameTime)
    {
        this.frameTexture = frameTexture;
        this.frameTime = frameTime;
    }

    public Sprite getFrameTexture() {
        return frameTexture;
    }
    public double getFrameTime() {
        return frameTime;
    }
}
