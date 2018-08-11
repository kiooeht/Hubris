package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.actions.common.DrawCardAction",
        method="update"
)
public class DrawCardActionPatch
{
    @SpireInsertPatch
    public static SpireReturn Insert(DrawCardAction __instance)
    {
        if (!AbstractDungeon.player.discardPile.isEmpty()) {
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, __instance.amount + 1));
            AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
            __instance.isDone = true;
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }

    public static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher("org.apache.logging.log4j.Logger", "warn");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
        }
    }
}
