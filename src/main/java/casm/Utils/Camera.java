package casm.Utils;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.Map.Map;
import casm.Utils.Settings.Setting;

public class Camera extends Component {

    private static Camera instance = null;

    private Vector2D camera, cameraOffset;
    private PositionComponent positionComponent;

    private Camera() {
    }

    public static Camera getInstance() {
        if (instance == null)
            instance = new Camera();
        return instance;
    }

    public static boolean hasTargget() {
        return instance != null;
    }

    @Override
    public void init() {
        camera = new Vector2D();
        cameraOffset = new Vector2D();

        positionComponent = gameObject.getComponent(PositionComponent.class);
    }

    @Override
    public void update() {
        cameraOffset = (Vector2D) camera.clone();

        camera = (Vector2D) positionComponent.position.clone();
        camera.x -= (double) Setting.SCREEN_WIDTH / 2;
        camera.y -= (double) Setting.SCREEN_HEIGHT / 2;
        if (camera.x < 0)
            camera.x = 0;
        if (camera.y < 0)
            camera.y = 0;
        if (camera.x > Map.getCols() * Map.getTileWidth() - Setting.SCREEN_WIDTH)
            camera.x = Map.getCols() * Map.getTileWidth() - Setting.SCREEN_WIDTH;
        if (camera.y > Map.getRows() * Map.getTileHeight() - Setting.SCREEN_HEIGHT)
            camera.y = Map.getRows() * Map.getTileHeight() - Setting.SCREEN_HEIGHT;

        cameraOffset.x = camera.x - cameraOffset.x;
        cameraOffset.y = camera.y - cameraOffset.y;
    }

    public boolean equalsToTarget(Vector2D objectPosition) {
        return positionComponent.position.equals(objectPosition);
    }

    public Vector2D getCamera() {
        return camera;
    }

    public Vector2D getCameraOffset() {
        return cameraOffset;
    }

    public void setCameraOffset(Vector2D cameraOffset) {
        this.cameraOffset = cameraOffset;
    }
}
