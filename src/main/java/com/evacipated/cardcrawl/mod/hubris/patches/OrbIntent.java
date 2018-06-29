package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;

import java.lang.reflect.Field;

public class OrbIntent
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.monsters.AbstractMonster",
            method="getIntentImg"
    )
    public static class Img
    {
        public static Texture Postfix(Texture __result, AbstractMonster __instance)
        {
            if (__instance.intent == OrbUsingMonster.Enums.CHANNEL_ORBS) {
                return ImageMaster.INTENT_MAGIC_L;
            }
            return __result;
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.monsters.AbstractMonster",
            method="updateIntentTip"
    )
    public static class Tip
    {
        public static void Postfix(AbstractMonster __instance)
        {
            if (__instance.intent == OrbUsingMonster.Enums.CHANNEL_ORBS) {
                try {
                    Field f = AbstractMonster.class.getDeclaredField("intentTip");
                    f.setAccessible(true);
                    PowerTip intentTip = (PowerTip)f.get(__instance);
                    intentTip.header = AbstractMonster.TEXT[15];
                    intentTip.body = AbstractMonster.TEXT[26];
                    intentTip.img = ImageMaster.INTENT_MAGIC;
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.monsters.AbstractMonster",
            method="renderDamageRange"
    )
    public static class RenderDamageRange
    {
        public static void Postfix(AbstractMonster __instance, SpriteBatch sb)
        {
            if (__instance.intent == OrbUsingMonster.Enums.CHANNEL_ORBS && __instance instanceof OrbUsingMonster) {
                OrbUsingMonster monster = (OrbUsingMonster)__instance;

                float bobEffectY = 0;
                Color intentColor = Color.WHITE;
                try {
                    Field f = AbstractMonster.class.getDeclaredField("bobEffect");
                    f.setAccessible(true);
                    bobEffectY = ((BobEffect)f.get(__instance)).y;

                    f = AbstractMonster.class.getDeclaredField("intentColor");
                    f.setAccessible(true);
                    intentColor = (Color)f.get(__instance);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(monster.numberToChannel),
                        __instance.intentHb.cX - 30.0f * Settings.scale, __instance.intentHb.cY + bobEffectY - 12.0f * Settings.scale,
                        intentColor);
            }
        }
    }
}
