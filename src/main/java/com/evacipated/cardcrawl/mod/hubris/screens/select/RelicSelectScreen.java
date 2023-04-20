package com.evacipated.cardcrawl.mod.hubris.screens.select;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class RelicSelectScreen implements ScrollBarListener
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("RelicViewScreen");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float SPACE = 80.0F * Settings.scale;
    private static final float START_X = 600.0F * Settings.scale;
    private static final float START_Y = Settings.HEIGHT - 300.0F * Settings.scale;
    private float scrollY = START_Y;
    private float targetY = this.scrollY;
    private float scrollLowerBound = Settings.HEIGHT - 200.0F * Settings.scale;
    private float scrollUpperBound = scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;//2600.0F * Settings.scale;
    private int row = 0;
    private int col = 0;
    private static final Color RED_OUTLINE_COLOR = new Color(-10132568);
    private static final Color GREEN_OUTLINE_COLOR = new Color(2147418280);
    private static final Color BLUE_OUTLINE_COLOR = new Color(-2016482392);
    private static final Color BLACK_OUTLINE_COLOR = new Color(168);
    private AbstractRelic hoveredRelic = null;
    private AbstractRelic clickStartedRelic = null;
    private boolean grabbedScreen = false;
    private float grabStartY = 0.0F;
    private ScrollBar scrollBar;
    private Hitbox controllerRelicHb = null;

    private ArrayList<AbstractRelic> relics;
    private boolean show = false;
    private int selectCount = 1;
    private ArrayList<AbstractRelic> selectedRelics = new ArrayList<>();

    public boolean doneSelecting()
    {
        return selectedRelics.size() >= selectCount;
    }

    public ArrayList<AbstractRelic> getSelectedRelics()
    {
        ArrayList<AbstractRelic> ret = new ArrayList<>(selectedRelics);
        selectedRelics.clear();
        return ret;
    }

    public RelicSelectScreen()
    {
        scrollBar = new ScrollBar(this);
    }

    public void open(ArrayList<AbstractRelic> relics)
    {
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen(0.5f);
        AbstractDungeon.overlayMenu.proceedButton.hide();
        show = true;

        controllerRelicHb = null;
        this.relics = relics;
        targetY = scrollLowerBound;
        scrollY = Settings.HEIGHT - 400.0f * Settings.scale;

        calculateScrollBounds();

        selectedRelics.clear();
    }

    public void close()
    {
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.GRID;
        AbstractDungeon.closeCurrentScreen();
        show = false;
    }

    public boolean isOpen()
    {
        return show;
    }

    public void update()
    {
        if (!isOpen()) {
            return;
        }

        updateControllerInput();
        if (Settings.isControllerMode && controllerRelicHb != null) {
            if (Gdx.input.getY() > Settings.HEIGHT * 0.7F) {
                targetY += Settings.SCROLL_SPEED;
                if (targetY > scrollUpperBound) {
                    targetY = scrollUpperBound;
                }
            } else if (Gdx.input.getY() < Settings.HEIGHT * 0.3F) {
                targetY -= Settings.SCROLL_SPEED;
                if (targetY < scrollLowerBound) {
                    targetY = scrollLowerBound;
                }
            }
        }

        if (hoveredRelic != null) {
            if (InputHelper.justClickedLeft || CInputActionSet.select.isJustPressed()) {
                clickStartedRelic = hoveredRelic;
            }
            if (InputHelper.justReleasedClickLeft || CInputActionSet.select.isJustPressed())
            {
                CInputActionSet.select.unpress();
                if (hoveredRelic == clickStartedRelic)
                {
                    selectedRelics.add(hoveredRelic);
                    clickStartedRelic = null;

                    if (doneSelecting()) {
                        close();
                    }
                }
            }

            if (InputHelper.justClickedRight || CInputActionSet.select.isJustPressed()) {
                clickStartedRelic = hoveredRelic;
            }
            if (InputHelper.justReleasedClickRight || CInputActionSet.select.isJustPressed())
            {
                CInputActionSet.select.unpress();
                if (hoveredRelic == clickStartedRelic)
                {
                    CardCrawlGame.relicPopup.open(hoveredRelic, relics);
                    clickStartedRelic = null;
                }
            }
        } else {
            clickStartedRelic = null;
        }
        boolean isScrollingScrollBar = scrollBar.update();
        if (!isScrollingScrollBar) {
            updateScrolling();
        }
        InputHelper.justClickedLeft = false;
        InputHelper.justClickedRight = false;

        hoveredRelic = null;
        updateList(relics);
        if (Settings.isControllerMode && controllerRelicHb != null) {
            Gdx.input.setCursorPosition((int)controllerRelicHb.cX, (int)(Settings.HEIGHT - controllerRelicHb.cY));
        }
    }

    private void updateControllerInput()
    {
        // TODO
    }

    private void updateScrolling()
    {
        int y = InputHelper.mY;
        if (!grabbedScreen)
        {
            if (InputHelper.scrolledDown) {
                targetY += Settings.SCROLL_SPEED;
            } else if (InputHelper.scrolledUp) {
                targetY -= Settings.SCROLL_SPEED;
            }
            if (InputHelper.justClickedLeft)
            {
                grabbedScreen = true;
                grabStartY = (y - targetY);
            }
        }
        else if (InputHelper.isMouseDown)
        {
            targetY = (y - grabStartY);
        }
        else
        {
            grabbedScreen = false;
        }
        scrollY = MathHelper.scrollSnapLerpSpeed(scrollY, targetY);
        resetScrolling();
        updateBarPosition();
    }

    private void calculateScrollBounds()
    {
        int size = relics.size();

        int scrollTmp = 0;
        if (size > 10) {
            scrollTmp = size / 10 - 2;
            if (size % 10 != 0) {
                ++scrollTmp;
            }
            scrollUpperBound = scrollLowerBound + (scrollTmp * SPACE) - (Settings.HEIGHT * 0.5f);
        } else {
            scrollUpperBound = scrollLowerBound + Settings.DEFAULT_SCROLL_LIMIT;
        }
    }

    private void resetScrolling()
    {
        if (targetY < scrollLowerBound) {
            targetY = MathHelper.scrollSnapLerpSpeed(targetY, scrollLowerBound);
        } else if (targetY > scrollUpperBound) {
            targetY = MathHelper.scrollSnapLerpSpeed(targetY, scrollUpperBound);
        }
    }

    private void updateList(ArrayList<AbstractRelic> list)
    {
        for (AbstractRelic r : list)
        {
            r.hb.move(r.currentX, r.currentY);
            r.update();
            if (r.hb.hovered)
            {
                hoveredRelic = r;
            }
        }
    }

    public void render(SpriteBatch sb)
    {
        if (!isOpen()) {
            return;
        }

        row = -1;
        col = 0;
        renderList(sb, relics);

        scrollBar.render(sb);
    }

    private void renderList(SpriteBatch sb, ArrayList<AbstractRelic> list)
    {
        row += 1;
        col = 0;
        for (AbstractRelic r : list) {
            if (col == 10) {
                col = 0;
                row += 1;
            }
            r.currentX = (START_X + SPACE * col);
            r.currentY = (scrollY - SPACE * row);
            if (RelicLibrary.redList.contains(r)) {
                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, RED_OUTLINE_COLOR);
                } else {
                    r.render(sb, false, RED_OUTLINE_COLOR);
                }
            } else if (RelicLibrary.greenList.contains(r)) {
                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, GREEN_OUTLINE_COLOR);
                } else {
                    r.render(sb, false, GREEN_OUTLINE_COLOR);
                }
            } else if (RelicLibrary.blueList.contains(r)) {
                if (UnlockTracker.isRelicLocked(r.relicId)) {
                    r.renderLock(sb, BLUE_OUTLINE_COLOR);
                } else {
                    r.render(sb, false, BLUE_OUTLINE_COLOR);
                }
            } else if (UnlockTracker.isRelicLocked(r.relicId)) {
                r.renderLock(sb, BLACK_OUTLINE_COLOR);
            } else {
                r.render(sb, false, BLACK_OUTLINE_COLOR);
            }
            col += 1;
        }
    }

    @Override
    public void scrolledUsingBar(float newPercent)
    {
        float newPosition = MathHelper.valueFromPercentBetween(scrollLowerBound, scrollUpperBound, newPercent);
        scrollY = newPosition;
        targetY = newPosition;
        updateBarPosition();
    }

    private void updateBarPosition()
    {
        float percent = MathHelper.percentFromValueBetween(scrollLowerBound, scrollUpperBound, scrollY);
        scrollBar.parentScrolledToPercent(percent);
    }
}
