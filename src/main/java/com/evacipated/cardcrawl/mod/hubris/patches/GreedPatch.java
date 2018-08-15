package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.Greed;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.rewards.RewardItem;
import javassist.CtBehavior;

@SpirePatch(
        cls="com.megacrit.cardcrawl.rewards.RewardItem",
        method="applyGoldBonus"
)
public class GreedPatch
{
    @SpireInsertPatch
    public static void Insert(RewardItem __instance, boolean theft)
    {
        __instance.bonusGold += Greed.countCopiesInDeck() * Greed.GOLD_AMOUNT;
    }

    public static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.characters.AbstractPlayer", "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
