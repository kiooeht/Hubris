package com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AlwaysRetainField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import java.util.ArrayList;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method=SpirePatch.CLASS
)
public class BottleRainField
{
    public static SpireField<Boolean> inBottleRain = new SpireField<>(() -> false);

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="makeStatEquivalentCopy"
    )
    public static class MakeStatEquivalentCopy
    {
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance)
        {
            inBottleRain.set(__result, inBottleRain.get(__instance));
            return __result;
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method="applyStartOfTurnCards"
    )
    public static class Retain
    {
        @SpireInsertPatch(
                locator=Locator.class,
                localvars={"c"}
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c)
        {
            if (c != null && inBottleRain.get(c)) {
                AlwaysRetainField.alwaysRetain.set(c, true);
                // Always Retain should do this, but double up here JUST to be sure
                // because I'm unsure about the patch ordering
                c.retain = true;
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.cards.AbstractCard", "atTurnStart");

                return LineFinder.findAllInOrder(ctBehavior, finalMatcher);
            }
        }
    }
}
