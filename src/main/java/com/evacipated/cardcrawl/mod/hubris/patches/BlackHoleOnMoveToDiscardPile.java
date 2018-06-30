package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BlackHole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.Soul;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.Soul",
        method="discard",
        paramtypes={"com.megacrit.cardcrawl.cards.AbstractCard", "boolean"}
)
public class BlackHoleOnMoveToDiscardPile
{
    public static void Postfix(Soul __instance, AbstractCard card, boolean visualOnly)
    {
        if (AbstractDungeon.player.hasRelic(BlackHole.ID)) {
            if (!visualOnly) {
                AbstractDungeon.player.discardPile.moveToExhaustPile(card);
            }
        }
    }
}
