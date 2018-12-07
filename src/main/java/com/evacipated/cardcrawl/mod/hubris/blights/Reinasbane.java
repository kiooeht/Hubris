package com.evacipated.cardcrawl.mod.hubris.blights;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.input.InputAction;
import com.megacrit.cardcrawl.screens.options.OptionsPanel;
import com.megacrit.cardcrawl.screens.options.ToggleButton;

import java.lang.reflect.Field;

public class Reinasbane extends AbstractBlight
{
    public static final String ID = "hubris:Reinasbane";
    //private static final BlightStrings blightStrings = CardCrawlGame.languagePack.getBlightString(ID);
    public static final String NAME = "Reinasbane";
    public static final String[] DESCRIPTION = new String[]{
            "Disable all keyboard shortcuts. NL Disable Fast Mode."
    };

    public Reinasbane()
    {
        super(ID, NAME, DESCRIPTION[0], "muzzle.png", true);
    }

    @Override
    public void onEquip()
    {
        Settings.FAST_MODE = false;
        try {
            Field f = OptionsPanel.class.getDeclaredField("fastToggle");
            f.setAccessible(true);

            ToggleButton fastToggle = (ToggleButton) f.get(AbstractDungeon.settingsScreen.panel);
            fastToggle.enabled = false;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update()
    {
        Settings.FAST_MODE = false;
        try {
            Field f = OptionsPanel.class.getDeclaredField("fastToggle");
            f.setAccessible(true);

            ToggleButton fastToggle = (ToggleButton) f.get(AbstractDungeon.settingsScreen.panel);
            fastToggle.enabled = false;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        super.update();
    }

    @SpirePatch(
            clz=InputAction.class,
            method="isPressed"
    )
    @SpirePatch(
            clz=InputAction.class,
            method="isJustPressed"
    )
    public static class BlockKeys
    {
        public static SpireReturn<Boolean> Prefix(InputAction __instance)
        {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasBlight(ID)) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
