package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PyramidsField;
import com.evacipated.cardcrawl.mod.hubris.relics.MysteriousPyramids;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="renderCard"
)
public class RenderPyramids
{
    public static void Postfix(AbstractCard __instance, SpriteBatch sb, boolean hovered, boolean selected)
    {
        if (!Settings.hideCards /*&& !__instance.isOnScreen()*/ && !__instance.isFlipped
                && PyramidsField.inPyramids.get(__instance)) {
            AbstractRelic r = new MysteriousPyramids();
            r.scale = __instance.drawScale;
            r.currentX = __instance.current_x + 390.0f * __instance.drawScale / 3.0f * Settings.scale;
            r.currentY = __instance.current_y + 546.0f * __instance.drawScale / 3.0f * Settings.scale;
            r.renderOutline(sb, false);
            r.render(sb);
        }
    }
}
