package casm.Objects.Menu;

import casm.Builder.BuilderDirector;
import casm.Builder.BuilderTypeEnum;
import casm.Factory.MenuFactory.MenuEntityType;
import casm.Objects.Object;
import casm.Utils.Vector2D;

public class ButtonsBuilderDirector implements BuilderDirector {
    private ButtonBuilder builder;

    public ButtonsBuilderDirector() {
        this.builder = new ButtonBuilder();
    }

    @Override
    public Object makeObject(BuilderTypeEnum type, Vector2D spawnPosition) {
        builder.reset();
        String sheetPath = "menu_assets\\buttons\\buttons_spritesheet.png";
        if (type.equals(MenuEntityType.BUTTON.PLAY)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        } else if (type.equals(MenuEntityType.BUTTON.LOAD)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        } else if (type.equals(MenuEntityType.BUTTON.EXIT)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        } else if (type.equals(MenuEntityType.BUTTON.RETRY)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        } else if (type.equals(MenuEntityType.BUTTON.RESUME)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        } else if (type.equals(MenuEntityType.BUTTON.NEW)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        } else if (type.equals(MenuEntityType.BUTTON.SETTINGS)) {
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        }else if (type.equals(MenuEntityType.BUTTON.BACK)) {
            sheetPath = "menu_assets\\buttons\\back_button_spritesheet.png";
            return builder.setType((MenuEntityType.BUTTON) type).addImage(sheetPath).setPosition(spawnPosition)
                    .clickable().build();
        }
        return null;
    }
}
