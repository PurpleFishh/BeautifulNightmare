package casm.Utils;

import casm.ECS.Component;
import casm.ECS.Components.PositionComponent;
import casm.Map.Map;
import casm.Utils.Settings.Setting;

/**
 * The camera of the game that follows an object
 */
public class Camera extends Component {

    /**
     * Singleton instance
     */
    private static Camera instance = null;

    /**
     * <b>camera</b> - is the position of the camera in the map
     * <b>cameraOffset</b> - is the offset of the camera in the map
     */
    private Vector2D camera, cameraOffset;
    /**
     * The position of the followed object
     */
    private PositionComponent positionComponent;

    private Camera() {
    }

    /**
     * @return get the singleton instance
     */
    public static Camera getInstance() {
        if (instance == null)
            instance = new Camera();
        return instance;
    }

    /**
     * @return if the camera has a target
     */
    public static boolean hasTargget() {
        return instance != null;
    }

    /**
     * Initialize the camera
     */
    @Override
    public void init() {
        camera = new Vector2D();
        cameraOffset = new Vector2D();

        positionComponent = gameObject.getComponent(PositionComponent.class);
    }

    /**
     * Update camera position
     */
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

    /**
     * Test if an object is the target of the camera
     *
     * @param objectPosition the object to be tested
     * @return if the object given by parameter is the target of the camera
     */
    public boolean equalsToTarget(Vector2D objectPosition) {
        return positionComponent.position.equals(objectPosition);
    }

    /**
     * @return the position of the camera
     */
    public Vector2D getCamera() {
        return camera;
    }

    /**
     * @return the offset of the camera
     */
    public Vector2D getCameraOffset() {
        return cameraOffset;
    }

}
