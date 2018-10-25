package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.curses.Despair;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.ShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DespairPatch
{
    @SpirePatch(
            clz=EmptyDeckShuffleAction.class,
            method="update"
    )
    public static class EmptyDeckShuffle
    {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(EmptyDeckShuffleAction __instance)
        {
            List<AbstractCard> cards = new ArrayList<>();
            Iterator<AbstractCard> iter = AbstractDungeon.player.discardPile.group.iterator();
            while (iter.hasNext()) {
                AbstractCard c = iter.next();
                if (c.cardID.equals(Despair.ID)) {
                    cards.add(c);
                    iter.remove();
                }
            }
            for (AbstractCard c : cards) {
                AbstractDungeon.player.discardPile.addToTop(c);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(EmptyDeckShuffleAction.class, "vfxDone");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz=ShuffleAction.class,
            method="update"
    )
    public static class Shuffle
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"group"}
        )
        public static void Insert(ShuffleAction __instance, CardGroup group)
        {
            if (group.type == CardGroup.CardGroupType.DRAW_PILE) {
                List<AbstractCard> cards = new ArrayList<>();
                Iterator<AbstractCard> iter = group.group.iterator();
                while (iter.hasNext()) {
                    AbstractCard c = iter.next();
                    if (c.cardID.equals(Despair.ID)) {
                        cards.add(c);
                        iter.remove();
                    }
                }
                for (AbstractCard c : cards) {
                    group.addToTop(c);
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(ShuffleAction.class, "isDone");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
