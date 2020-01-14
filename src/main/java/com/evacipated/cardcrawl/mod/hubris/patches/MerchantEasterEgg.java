package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.relics.NiceRug;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.SmilingMask;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class MerchantEasterEgg
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
        private static AnimatedNpc merchant;

        public static SpireReturn<Void> Prefix(AbstractPlayer __instance, SpriteBatch sb)
        {
            if (__instance.hasRelic(NiceRug.ID) && __instance.hasRelic(SmilingMask.ID)) {
                if (merchant == null) {
                    merchant = new AnimatedNpc(__instance.drawX + __instance.animX, __instance.drawY + __instance.animY + AbstractDungeon.sceneOffsetY,
                            "images/npcs/merchant/skeleton.atlas", "images/npcs/merchant/skeleton.json", "idle");
                    __instance.hb_w = __instance.hb.width = 180.0f * Settings.scale;
                    __instance.hb_h = __instance.hb.height = 170.0f * Settings.scale;
                    __instance.hb_y -= 40.0f * Settings.scale;
                    __instance.healthBarUpdatedEvent();
                }
                merchant.skeleton.setPosition(__instance.drawX + __instance.animX, __instance.drawY + __instance.animY + AbstractDungeon.sceneOffsetY);
                merchant.skeleton.setFlip(true, false);
                merchant.render(sb);
                return SpireReturn.Return(null);
            } else {
                if (merchant != null) {
                    merchant.dispose();
                }
                merchant = null;
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz=AnimatedNpc.class,
            method="render",
            paramtypez={
                    SpriteBatch.class
            }
    )
    @SpirePatch(
            clz=AnimatedNpc.class,
            method="render",
            paramtypez={
                    SpriteBatch.class,
                    Color.class
            }
    )
    public static class RemoveFlipReset
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("setFlip")) {
                        m.replace("");
                    }
                }
            };
        }
    }
}
