package com.evacipated.cardcrawl.mod.hubris.patches.potions.AbstractPotion;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.relics.EmptyBottle;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.ui.panels.TopPanel;

@SpirePatch(
        cls="com.megacrit.cardcrawl.potions.AbstractPotion",
        method=SpirePatch.CLASS
)
public class PotionUseCountField
{
    public static SpireField<Integer> useCount = new SpireField<>(() -> EmptyBottle.POTION_USES);

    @SpirePatch(
            cls="com.megacrit.cardcrawl.ui.panels.TopPanel",
            method="renderPotions"
    )
    public static class Render
    {
        public static void Postfix(TopPanel __instance, SpriteBatch sb)
        {
            if (AbstractDungeon.player.hasRelic(EmptyBottle.ID)) {
                for (AbstractPotion p : AbstractDungeon.player.potions) {
                    if (!(p instanceof PotionSlot) && p.isObtained) {
                        FontHelper.renderFontRightTopAligned(sb, FontHelper.topPanelAmountFont,
                                String.valueOf(useCount.get(p)),
                                p.posX + 20.0f * Settings.scale, p.posY - 14.0f * Settings.scale,
                                Settings.CREAM_COLOR);
                    }
                }
            }
        }
    }
}
