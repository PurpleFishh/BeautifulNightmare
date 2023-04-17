package casm.SpriteUtils.Animation;

import casm.SpriteUtils.Assets;
import casm.SpriteUtils.ImageLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class AnimationsExtract {

    public static List<AnimationState> extractAnimations(String path) {
        List<AnimationState> animationsList = new ArrayList<>();
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(Paths.get("").toAbsolutePath() + "\\resources\\animations\\" + path));


            String sprite_name = json.get("sprite_image").toString();
            int framesWidth = Integer.parseInt(json.get("frames_width").toString());
            int framesHeight = Integer.parseInt(json.get("frames_height").toString());
            int rows = Integer.parseInt(json.get("rows").toString());

            JSONArray animations = (JSONArray) json.get("animations");
            for (int i = 0; i < rows; ++i) {
                JSONObject animationInfo = (JSONObject) animations.get(i);
                String name = animationInfo.get("name").toString();
                if(!name.equals("none")) {
                    int cols = Integer.parseInt(animationInfo.get("columns").toString());
                    int xStartPoint = Integer.parseInt(animationInfo.get("x_start").toString());
                    int yStartPoint = Integer.parseInt(animationInfo.get("y_start").toString());
                    int frameWidth = Integer.parseInt(animationInfo.get("frame_width").toString());
                    int frameHeight = Integer.parseInt(animationInfo.get("frame_height").toString());
                    double speed = Double.parseDouble(animationInfo.get("speed").toString());

                    BufferedImage sprite_sheet = ImageLoader.LoadImage("animations\\" + sprite_name).getSubimage(0, i * framesHeight, cols * framesWidth, framesHeight);
                    Assets animationAssets = new Assets(sprite_sheet, xStartPoint, yStartPoint, frameWidth, frameHeight, framesWidth, framesHeight, cols * framesWidth, framesHeight);

                    AnimationState state = new AnimationState(name);
                    if (animationInfo.containsKey("frame_time")) {
                        List<Double> framesTime = new ArrayList<>();
                        for (Object timeExtracted : (JSONArray) animationInfo.get("frame_time"))
                            framesTime.add(Double.parseDouble(timeExtracted.toString()));
                        state.addFrames(animationAssets.getAssets(), framesTime, speed);
                    } else
                        state.addFrames(animationAssets.getAssets(), 1, speed);
                    state.setLoop(Boolean.parseBoolean(animationInfo.get("loop").toString()));
                    animationsList.add(state);
                }
            }
            return animationsList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return animationsList;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
