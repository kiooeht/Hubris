package com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.CardGroup;
import javassist.CtBehavior;

import java.util.ArrayList;

public class InnateOncePatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method=SpirePatch.CLASS
    )
    public static class Field
    {
        public static SpireField<Boolean> isInnateOnce = new SpireField<>(() -> false);
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.CardGroup",
            method="initializeDeck"
    )
    public static class PlayingCardMapPatch
    {
        @SpireInsertPatch(
                localvars={"copy"}
        )
        public static void Insert(CardGroup __instance, CardGroup masterDeck, CardGroup copy)
        {
            assert copy.size() == masterDeck.size();

            for (int i=0; i<copy.size(); ++i) {
                if (Field.isInnateOnce.get(masterDeck.group.get(i))) {
                    copy.group.get(i).isInnate = true;
                    Field.isInnateOnce.set(masterDeck.group.get(i), false);
                }
            }
        }

        public static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.cards.CardGroup", "shuffle");

                return LineFinder.findInOrder(ctBehavior, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
