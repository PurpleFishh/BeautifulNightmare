package casm.SpriteUtils.Animation;

import casm.SpriteUtils.Sprite;

/**
 * It represents a frame of an animation.
 */
public class Frame {

    /**
     * The texture of the frame.
     */
    private final Sprite frameTexture;
    /**
     * How long the frame lasts.
     */
    private double frameTime = 0;

    /**
     * @param frameTexture The texture of the frame.
     * @param frameTime How long the frame lasts.
     */
    public Frame(Sprite frameTexture, double frameTime)
    {
        this.frameTexture = frameTexture;
        this.frameTime = frameTime;
    }

    /**
     * @return The texture of the frame.
     */
    public Sprite getFrameTexture() {
        return frameTexture;
    }

    /**
     * @return How long the frame lasts.
     */
    public double getFrameTime() {
        return frameTime;
    }
}
