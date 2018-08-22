package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.TheBottler;
import com.evacipated.cardcrawl.modthespire.lib.*;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.dungeons.AbstractDungeon",
        method="getEvent"
)
public class TheBottlerPatch
{
    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"tmp"}
    )
    public static void Insert(ArrayList<String> tmp)
    {
        if (!TheBottler.canAppear()) {
            tmp.remove(TheBottler.ID);
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher("java.util.ArrayList", "isEmpty");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
