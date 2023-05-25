package casm.StateMachine.AnimationStateMachine;

import casm.SpriteUtils.Animation.Frame;
import casm.SpriteUtils.Sprite;
import casm.StateMachine.AfterStateEndsNotify;
import casm.StateMachine.UpdatableState;
import casm.Utils.Settings.Setting;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to store an animation state.
 * Used in {@link AnimationStateMachine}.
 */
public class AnimationState implements UpdatableState {

    /**
     * The name of the animation state.
     */
    private String name;
    /**
     * The frames of the animation.
     */
    private List<Frame> animationFrames = new ArrayList<>();
    /**
     * <b>time</b> - the timer for every frame until it changes to the next one.
     * <b>speed</b> - the speed that the animation is playing.
     */
    private double time = 0.0, speed = 1;
    /**
     * The index of the current sprite played in the animation.
     */
    private int currentSprite = 0;
    /**
     * <b>doseLoop</b> - if the animation should loop.
     * <b>endedPlaying</b> - if the animation ended playing.
     */
    private boolean doseLoop = false, endedPlaying = false;

    /**
     * @param name The name of the animation state.
     */
    public AnimationState(String name) {
        this.name = name;
    }

    /**
     * @param sprite    The sprite of the frame.
     * @param frameTime How long the frame lasts.
     */
    public void addFrame(Sprite sprite, double frameTime) {
        animationFrames.add(new Frame(sprite, frameTime));
    }

    /**
     * @param sprite    The sprite of the frame.
     * @param frameTime How long the frame lasts.
     * @param speed     The speed that the animation is playing.
     */
    public void addFrame(Sprite sprite, double frameTime, double speed) {
        animationFrames.add(new Frame(sprite, frameTime));
        this.speed = speed;
    }

    /**
     * @param sprites The sprites of the frames.
     */
    public void addFrames(List<Sprite> sprites) {
        sprites.forEach(sprite -> animationFrames.add(new Frame(sprite, 1)));
    }

    /**
     * @param sprites   The sprites of the frames.
     * @param frameTime How long the frame lasts.
     */
    public void addFrames(List<Sprite> sprites, double frameTime) {
        sprites.forEach(sprite -> animationFrames.add(new Frame(sprite, frameTime)));
    }

    /**
     * @param sprites   The sprites of the frames.
     * @param frameTime How long the frame lasts.
     * @param speed     The speed that the animation is playing.
     */
    public void addFrames(List<Sprite> sprites, double frameTime, double speed) {
        sprites.forEach(sprite -> animationFrames.add(new Frame(sprite, frameTime)));
        this.speed = speed;
    }

    /**
     * @param sprites   The sprites of the frames.
     * @param frameTime How long the frame lasts.
     */
    public void addFrames(List<Sprite> sprites, List<Double> frameTime) {
        for (int i = 0; i < sprites.size(); ++i)
            animationFrames.add(new Frame(sprites.get(i), frameTime.get(i)));
    }

    /**
     * @param sprites   The sprites of the frames.
     * @param frameTime How long the frame lasts.
     * @param speed     The speed that the animation is playing.
     */
    public void addFrames(List<Sprite> sprites, List<Double> frameTime, double speed) {
        for (int i = 0; i < sprites.size(); ++i)
            animationFrames.add(new Frame(sprites.get(i), frameTime.get(i)));
        this.speed = speed;
    }


    /**
     * @param doseLoop If the animation should loop.
     */
    public void setLoop(boolean doseLoop) {
        this.doseLoop = doseLoop;
    }

    /**
     * Used to update the animation
     *
     * @param notifier The notifier that notifies when the animation ends playing.
     */
    public void update(AfterStateEndsNotify notifier) {
        if (currentSprite < animationFrames.size()) {
            time -= speed / (Setting.DELTA_TIME / 2);
            if (time <= 0) {
                if (currentSprite == animationFrames.size() - 1) {
                    endedPlaying = true;
                    if (notifier != null)
                        notifier.afterStateEndsNotify();
                }
                if (!(currentSprite == animationFrames.size() - 1 && !doseLoop)) {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                    endedPlaying = false;
                }
                time = animationFrames.get(currentSprite).getFrameTime();
            }
        }
    }

    /**
     * @return The current sprite of the animation.
     */
    public Sprite getCurrentSprite() {
        if (currentSprite < animationFrames.size())
            return animationFrames.get(currentSprite).getFrameTexture();
        return null;
    }

    /**
     * Resets the animation to the first frame.
     */
    public void resetToFirstFrame() {
        currentSprite = 0;
        endedPlaying = false;
        time = animationFrames.get(currentSprite).getFrameTime();
    }

    /**
     * @param speed The speed that the animation is playing.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @return The name of the animation state.
     */
    public String getName() {
        return name;
    }

    /**
     * @return If the animation is looping.
     */
    public boolean getDoseLoop() {
        return doseLoop;
    }

    /**
     * @return If the animation ended playing.
     */
    public boolean isEndedPlaying() {
        return endedPlaying;
    }
}
