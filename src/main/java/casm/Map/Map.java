package casm.Map;

import casm.ECS.Components.ColliderComponent;
import casm.ECS.Scene;
import casm.Entities.Tile;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import netscape.javascript.JSObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

public class Map {

    private static int cols, rows, tileWidth, tileHeight;

    public static void loadMap(Scene scene, String path, String sprites_name) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(Paths.get("").toAbsolutePath() + "\\resources\\GameMaps\\" + path));
            JSONArray layers = (JSONArray) json.get("layers");

            tileWidth = Integer.parseInt(json.get("tilewidth").toString());
            tileHeight = Integer.parseInt(json.get("tileheight").toString());
            AssetsCollection.addSpritesheet(sprites_name, tileWidth, tileHeight);
            Assets sprites = AssetsCollection.getSpritesheet(sprites_name);

            int texture_id_offset = Integer.parseInt(((JSONObject) ((JSONArray) json.get("tilesets")).get(0)).get("firstgid").toString());

            layers.forEach(layer -> {
                JSONObject layerObj = (JSONObject) layer;

                rows = Integer.parseInt(layerObj.get("height").toString());
                cols = Integer.parseInt(layerObj.get("width").toString());
                int layer_id = Integer.parseInt(layerObj.get("id").toString());
                JSONArray data = (JSONArray) layerObj.get("data");
                String layerName = layerObj.get("name").toString();
                if (Objects.equals(layerName, "collision")) {
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            if(data.get(i * cols + j).toString().equals("1")) {
                                Tile tile = (Tile) scene.getLayeringObjects().get(0).get(i * cols + j);
                                tile.addComponent(new ColliderComponent("tile", tile.getTileWidth(), tileHeight));
                            }
                        }
                    }
                } else
                    for (int i = 0; i < rows; i++) {
                        for (int j = 0; j < cols; j++) {
                            Tile tile = new Tile("tile", sprites, Long.parseLong(data.get(i * cols + j).toString()) - texture_id_offset, j, i);
                            scene.addGameObjectToScene(tile);
                            scene.addGameOjectToLayer(tile, layer_id);
                        }
                    }

            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCols() {
        return cols;
    }
    public static int getRows() {
        return rows;
    }

    public static int getTileWidth() {
        return tileWidth;
    }
    public static int getTileHeight() {
        return tileHeight;
    }
}
