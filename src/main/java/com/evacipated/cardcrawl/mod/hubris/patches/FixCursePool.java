package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.Map;

@SpirePatch(
        cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
        method="addCurseCards"
)
public class FixCursePool
{
    public static void Postfix(AbstractDungeon __instance)
    {
        // Clear the curse pool, but keep any that won't be re-added
        AbstractDungeon.curseCardPool.group.removeIf(c -> c.color == AbstractCard.CardColor.CURSE);

        for (Map.Entry<String, AbstractCard> c : CardLibrary.cards.entrySet()) {
            AbstractCard card = c.getValue();
            if (card.color == AbstractCard.CardColor.CURSE && card.rarity != AbstractCard.CardRarity.SPECIAL) {
                AbstractDungeon.curseCardPool.addToTop(card);
            }
        }
    }
}
