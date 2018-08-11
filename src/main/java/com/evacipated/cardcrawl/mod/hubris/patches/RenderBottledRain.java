package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.BottleRainField;
import com.evacipated.cardcrawl.mod.hubris.relics.BottledRain;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.Field;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="renderCard"
)
public class RenderBottledRain
{
    private static Field relicRotation;

    static {
        try {
            relicRotation = AbstractRelic.class.getDeclaredField("rotation");
            relicRotation.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void Postfix(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (!Settings.hideCards /*&& !__instance.isOnScreen()*/ && !__instance.isFlipped
                && BottleRainField.inBottleRain.get(__instance)) {
            AbstractRelic r = new BottledRain();
            r.scale = __instance.drawScale * 0.8f;
            try {
                relicRotation.set(r, __instance.angle);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            r.currentX = __instance.current_x + 390.0f * __instance.drawScale / 3.0f * Settings.scale;
            r.currentY = __instance.current_y + 566.0f * __instance.drawScale / 3.0f * Settings.scale;
            r.renderOutline(sb, false);
            r.render(sb);
        }
    }
}
