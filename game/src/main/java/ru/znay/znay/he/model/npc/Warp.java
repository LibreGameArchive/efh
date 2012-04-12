package ru.znay.znay.he.model.npc;

import ru.znay.znay.he.Game;
import ru.znay.znay.he.cfg.Constants;
import ru.znay.znay.he.gfx.gui.GuiManager;
import ru.znay.znay.he.gfx.gui.Menu;
import ru.znay.znay.he.gfx.gui.TextPanel;
import ru.znay.znay.he.gfx.gui.TypedTextPanel;
import ru.znay.znay.he.gfx.helper.BitmapHelper;
import ru.znay.znay.he.gfx.helper.PaletteHelper;
import ru.znay.znay.he.gfx.model.Screen;
import ru.znay.znay.he.gfx.sprite.SpriteCollector;
import ru.znay.znay.he.gfx.sprite.SpriteWrapper;
import ru.znay.znay.he.model.Entity;
import ru.znay.znay.he.model.Player;
import ru.znay.znay.he.model.level.tile.Tile;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Денис Сергеевич
 * Date: 12.04.12
 * Time: 12:10
 * To change this template use File | Settings | File Templates.
 */
public class Warp extends Entity implements Menu.MenuCallback {
    private int srcLevel;
    private int dstLevel;
    private int dstX;
    private int dstY;
    private TextPanel textPanel;
    private List<String> strings = new LinkedList<String>();
    private Player player;

    public Warp(int srcLevel, int srcX, int srcY, int dstLevel, int dstX, int dstY, SpriteCollector spriteCollector, Player player) {
        this.srcLevel = srcLevel;
        this.x = srcX;
        this.y = srcY;
        this.dstLevel = dstLevel;
        this.dstX = dstX;
        this.dstY = dstY;
        this.player = player;

        spriteCollector.resetWrappers();
        spriteCollector.addWrapper(new SpriteWrapper(0, 11 * Tile.HALF_SIZE, Tile.HALF_SIZE, Tile.HALF_SIZE, PaletteHelper.getColor(-1, 0, 222, 333)));

        this.sprite = spriteCollector.mergedWrappers("warp", 2, 0, true);

        strings.add("Переход");
        strings.add("Отмена");
        
        System.out.println(player);

    }

    @Override
    public void render(Screen screen) {

        int xt = (x - 4) - screen.getXOffset();
        int yt = (y - 5 * 3 - yr) - screen.getYOffset();

        BitmapHelper.drawNormal(sprite, xt, yt, screen.getViewPort(), 0xFF00FF);
    }

    @Override
    public void touchedBy(Entity entity) {
        if (entity instanceof Player)
        {
            System.out.println("touch");
            showMenu();
        }
    }

    private void showMenu() {

        String mes;

        if (srcLevel == dstLevel)
            mes = "?";
        else
            mes = " в " + Constants.getLevelName(dstLevel) + "?";

        textPanel = new TextPanel("Совершить переход" + mes, 4, 4);

        GuiManager.getInstance().add(textPanel);

        ((Menu) GuiManager.getInstance().get("menu")).showMenu(strings, this);
    }

    private void doWarp() {
        if (srcLevel == dstLevel)
            player.moveXY(dstX, dstY);
        else
            player.moveLevel(dstLevel, dstX, dstY);
    }

    @Override
    public void result(int result) {
        textPanel.close();
        if (result == 0)
            doWarp();
    }
}
