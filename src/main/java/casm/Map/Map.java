package casm.Map;

import casm.ECS.Components.Collision.ColliderComponent;
import casm.ECS.Components.Collision.ColliderType;
import casm.Scenes.Scene;
import casm.Objects.Entities.Tile;
import casm.SpriteUtils.Assets;
import casm.SpriteUtils.AssetsCollection;
import casm.Utils.Vector2D;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Map {

    private static int cols, rows, tileWidth, tileHeight;
    private static Vector2D playerSpawnPosition, spawnDoor, winDoor;
    private static Set<Vector2D> enemiesSpawnPosition = new HashSet<>();

    public static void loadMap(Scene scene, String path, String sprites_name) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(new FileReader(Paths.get("").toAbsolutePath() + "\\resources\\GameMaps\\" + path));
            JSONArray layers = (JSONArray) json.get("layers");

            tileWidth = Integer.parseInt(json.get("tilewidth").toString());
            tileHeight = Integer.parseInt(json.get("tileheight").toString());
            AssetsCollection.getInstance().addSpriteSheet(sprites_name, tileWidth, tileHeight);
            Assets sprites = AssetsCollection.getInstance().getSpriteSheet(sprites_name);

            int texture_id_offset = Integer.parseInt(((JSONObject) ((JSONArray) json.get("tilesets")).get(0)).get("firstgid").toString());

            layers.forEach(layer -> {
                JSONObject layerObj = (JSONObject) layer;
                if (layerObj.get("type").toString().equals("tilelayer")) {
                    rows = Integer.parseInt(layerObj.get("height").toString());
                    cols = Integer.parseInt(layerObj.get("width").toString());
                    int layer_id = Integer.parseInt(layerObj.get("id").toString());
                    JSONArray data = (JSONArray) layerObj.get("data");
                    String layerName = layerObj.get("name").toString();
                    if (Objects.equals(layerName, "collision")) {
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < cols; j++) {
                                int colliderType = Integer.parseInt(data.get(i * cols + j).toString());
                                if (colliderType != 0) {
                                    Tile tile = (Tile) scene.getLayeringObjects().get(0).get(i * cols + j);
                                    tile.addComponent(new ColliderComponent(ColliderType.values()[colliderType - 1], tile.getTileWidth(), tileHeight));
                                }
                            }
                        }
                    } else
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < cols; j++) {
                                Tile tile = new Tile("tile", sprites, Long.parseLong(data.get(i * cols + j).toString()) - texture_id_offset, j, i);
                                scene.addGameObjectToScene(tile);
                                scene.addGameObjectToLayer(tile, layer_id);
                            }
                        }
                }
                if (layerObj.get("type").toString().equals("objectgroup")) {
                    JSONArray objectGroup = ((JSONArray) layerObj.get("objects"));
                    objectGroup.forEach(positionObject -> {
                        double x = Double.parseDouble(((JSONObject) positionObject).get("x").toString());
                        double y = Double.parseDouble(((JSONObject) positionObject).get("y").toString());
                        if (layerObj.get("name").toString().equals("player"))
                            playerSpawnPosition = new Vector2D(x, y);
                        if (layerObj.get("name").toString().equals("spawn door")) {
                            double h = Double.parseDouble(((JSONObject) positionObject).get("height").toString());
                            spawnDoor = new Vector2D(x, y - h);
                        }
                        if (layerObj.get("name").toString().equals("win door")) {
                            double h = Double.parseDouble(((JSONObject) positionObject).get("height").toString());
                            winDoor = new Vector2D(x, y - h);
                        }
                        if (layerObj.get("name").toString().equals("enemies"))
                            enemiesSpawnPosition.add(new Vector2D(x, y));
                    });

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

    public static Vector2D getPlayerSpawnPosition() {
        return playerSpawnPosition;
    }

    public static Vector2D getSpawnDoor() {
        return spawnDoor;
    }

    public static Vector2D getWinDoor() {
        return winDoor;
    }

    public static Set<Vector2D> getEnemiesSpawnPosition() {
        return enemiesSpawnPosition;
    }
}
