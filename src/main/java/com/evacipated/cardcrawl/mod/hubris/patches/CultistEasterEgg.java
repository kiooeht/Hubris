package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.relics.DeadBranch;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CultistEasterEgg
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method=SpirePatch.CLASS
    )
    public static class IsCultistField
    {
        public static SpireField<Boolean> isCultist = new SpireField<>(false);
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method="renderPlayerImage"
    )
    public static class RenderPlayImage
    {
        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (__instance.hasRelic(CultistMask.ID) && __instance.hasRelic(DeadBranch.ID)) {
                if (!IsCultistField.isCultist.get(__instance)) {
                    IsCultistField.isCultist.set(__instance, true);
                    try {
                        Field flipHorizontal = AbstractCreature.class.getDeclaredField("flipHorizontal");
                        flipHorizontal.setAccessible(true);
                        Method loadAnimation = AbstractCreature.class.getDeclaredMethod("loadAnimation", String.class, String.class, float.class);
                        loadAnimation.setAccessible(true);

                        loadAnimation.invoke(__instance, "images/monsters/theBottom/cultist/skeleton.atlas", "images/monsters/theBottom/cultist/skeleton.json", 1.0F);
                        flipHorizontal.set(__instance, true);

                        AnimationState.TrackEntry e = __instance.state.setAnimation(0, "waving", true);
                        e.setTime(e.getEndTime() * MathUtils.random());
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method="render"
    )
    public static class UndoFlipAfterDeath
    {
        private static Field renderCorpse;

        static
        {
            try {
                renderCorpse = AbstractPlayer.class.getDeclaredField("renderCorpse");
                renderCorpse.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        public static void Prefix(AbstractPlayer __instance, SpriteBatch sb)
        {
            try {
                if (renderCorpse != null && (boolean)renderCorpse.get(__instance) && IsCultistField.isCultist.get(__instance)) {
                    Field flipHorizontal = AbstractCreature.class.getDeclaredField("flipHorizontal");
                    flipHorizontal.setAccessible(true);
                    flipHorizontal.set(__instance, false);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
