package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.CultistMask;
import com.megacrit.cardcrawl.relics.DeadBranch;

public class CultistEasterEgg
{
    @SpirePatch(
            clz=AbstractPlayer.class,
            method="renderPlayerImage"
    )
    @SpirePatch(
            clz=basemod.abstracts.CustomPlayer.class,
            method="renderPlayerImage"
    )
    public static class RenderPlayImage
    {
        private static AnimatedNpc cultist;

        public static SpireReturn<Void> Prefix(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (__instance.hasRelic(CultistMask.ID) && __instance.hasRelic(DeadBranch.ID)) {
                if (cultist == null) {
                    cultist = new AnimatedNpc(__instance.drawX + __instance.animX, __instance.drawY + __instance.animY + AbstractDungeon.sceneOffsetY - 35.0f * Settings.scale,
                            "images/monsters/theBottom/cultist/skeleton.atlas", "images/monsters/theBottom/cultist/skeleton.json", "waving");
                }
                cultist.skeleton.setPosition(__instance.drawX + __instance.animX, __instance.drawY + __instance.animY + AbstractDungeon.sceneOffsetY - 35.0f * Settings.scale);
                cultist.skeleton.setFlip(true, false);
                cultist.render(sb);
                return SpireReturn.Return(null);
            } else {
                if (cultist != null) {
                    cultist.dispose();
                }
                cultist = null;
            }
            return SpireReturn.Continue();
        }
    }
}
