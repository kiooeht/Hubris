package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.scenes.TitleBackground;

import java.lang.reflect.Field;

@SpirePatch(
        clz=TitleBackground.class,
        method=SpirePatch.CONSTRUCTOR
)
public class LogoPatch
{
    private static final float CHANCE = 0.01f;

    public static void Postfix(TitleBackground __instance)
    {
        if (HubrisMod.otherSaveData.has("UPDATEBODYTEXT") && HubrisMod.otherSaveData.getBool("UPDATEBODYTEXT")) {
            if (MathUtils.random() <= CHANCE) {
                try {
                    Field f = TitleBackground.class.getDeclaredField("titleLogoImg");
                    f.setAccessible(true);
                    f.set(null, ImageMaster.loadImage(HubrisMod.assetPath("images/ui/title_logo/UPDATE_LOGO_TEXT.png")));
                } catch (IllegalAccessException | NoSuchFieldException ignore) {
                }
            }
        }
    }
}
