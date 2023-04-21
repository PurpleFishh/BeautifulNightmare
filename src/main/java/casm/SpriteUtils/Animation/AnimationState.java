package casm.SpriteUtils.Animation;

import casm.SpriteUtils.Sprite;
import casm.Utils.Setting;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {

    private String name;
    private List<Frame> animationFrames = new ArrayList<>();
    private double time = 0.0, speed = 1;
    private int currentSprite = 0;
    private boolean doseLoop = false, endedPlaying = false;

    public AnimationState(String name) {
        this.name = name;
    }

    public void addFrame(Sprite sprite, double frameTime) {
        animationFrames.add(new Frame(sprite, frameTime));
    }

    public void addFrame(Sprite sprite, double frameTime, double speed) {
        animationFrames.add(new Frame(sprite, frameTime));
        this.speed = speed;
    }

    public void addFrames(List<Sprite> sprites) {
        sprites.forEach(sprite -> animationFrames.add(new Frame(sprite, 1)));
    }

    public void addFrames(List<Sprite> sprites, double frameTime) {
        sprites.forEach(sprite -> animationFrames.add(new Frame(sprite, frameTime)));
    }

    public void addFrames(List<Sprite> sprites, double frameTime, double speed) {
        sprites.forEach(sprite -> animationFrames.add(new Frame(sprite, frameTime)));
        this.speed = speed;
    }

    public void addFrames(List<Sprite> sprites, List<Double> frameTime) {
        for (int i = 0; i < sprites.size(); ++i)
            animationFrames.add(new Frame(sprites.get(i), frameTime.get(i)));
    }

    public void addFrames(List<Sprite> sprites, List<Double> frameTime, double speed) {
        for (int i = 0; i < sprites.size(); ++i)
            animationFrames.add(new Frame(sprites.get(i), frameTime.get(i)));
        this.speed = speed;
    }


    public void setLoop(boolean doseLoop) {
        this.doseLoop = doseLoop;
    }

    public void update(AnimationEndNotify notifier) {
        if (currentSprite < animationFrames.size()) {
            time -= speed / Setting.DELTA_TIME;
            if (time <= 0) {
                if (currentSprite == animationFrames.size() - 1) {
                    endedPlaying = true;
                    if (notifier != null)
                        notifier.animationEndNotify();
                }
                if (!(currentSprite == animationFrames.size() - 1 && !doseLoop)) {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                    endedPlaying = false;
                }
                time = animationFrames.get(currentSprite).getFrameTime();
            }
        }
    }

    public Sprite getCurrentSprite() {
        if (currentSprite < animationFrames.size())
            return animationFrames.get(currentSprite).getFrameTexture();
        return null;
    }

    public void resetToFirstFrame() {
        currentSprite = 0;
        endedPlaying = false;
        time = animationFrames.get(currentSprite).getFrameTime();
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public boolean getDoseLoop() {
        return doseLoop;
    }

    public boolean isEndedPlaying() {
        return endedPlaying;
    }
}
