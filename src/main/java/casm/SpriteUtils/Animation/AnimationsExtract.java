package casm.SpriteUtils.Animation;

import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.StateMachine.AnimationStateMachine.AnimationState;
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

/**
 * AnimationsExtract is used to extract animations from a json file.
 * You pass throw a JSON file the animation sheet, every state of the animation, the number of frames and its dimensions and offsets, and it will extract it and store everything.
 */
public class AnimationsExtract {
    /**
     * The instance of the singleton class.
     */
    private static AnimationsExtract instance = null;

    private AnimationsExtract() {
    }

    /**
     * @return get the singleton instance.
     */
    public static AnimationsExtract getInstance() {
        if (instance == null)
            instance = new AnimationsExtract();
        return instance;
    }

    /**
     * This method extracts all the animations from a json file.
     * JSON format example:
     * <pre>{@code
     * {
     *   "sprite_image": "player_spritesheer.png",
     *   "frames_width": "128",
     *   "frames_height": "64",
     *   "rows": "9",
     *   "animations": [
     *     {
     *       "name": "idle",
     *       "columns": "8",
     *       "x_start": "48",
     *       "y_start": "20",
     *       "frame_width": "32",
     *       "frame_height": "44",
     *       "speed" : "1",
     *       "loop" : true,
     *       "frame_time": [30, 1, 1, 1, 1, 1, 1, 1]
     *     },
     *     {
     *       "name": "run",
     *       "columns": "8",
     *       "x_start": "48",
     *       "y_start": "14",
     *       "frame_width": "34",
     *       "frame_height": "50",
     *       "speed" : "2",
     *       "loop" : true
     *     },
     *   ]
     * }
     * }</pre>
     *
     * @param path path to the json file.
     * @return a list of all the animations extracted from the json file.
     */
    public List<AnimationState> extractAnimations(String path) {
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
                if (!name.equals("none")) {
                    int cols = Integer.parseInt(animationInfo.get("columns").toString());
                    int xStartPoint = Integer.parseInt(animationInfo.get("x_start").toString());
                    int yStartPoint = Integer.parseInt(animationInfo.get("y_start").toString());
                    int frameWidth = Integer.parseInt(animationInfo.get("frame_width").toString());
                    int frameHeight = Integer.parseInt(animationInfo.get("frame_height").toString());
                    double speed = Double.parseDouble(animationInfo.get("speed").toString());

                    BufferedImage sprite_sheet = AssetsCollection.getInstance().addSprite("animations\\" + sprite_name)
                            .getTexture().getSubimage(0, i * framesHeight, cols * framesWidth, framesHeight);
                    Assets animationAssets = new Assets(sprite_sheet, xStartPoint, yStartPoint, frameWidth, frameHeight,
                            framesWidth, framesHeight, cols * framesWidth, framesHeight);

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
