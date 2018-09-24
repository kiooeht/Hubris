package com.evacipated.cardcrawl.mod.hubris.patches.powerInterfaces;

import com.evacipated.cardcrawl.mod.hubris.powers.abstracts.OnReceivePowerPower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CtBehavior;

@SpirePatch(
        clz=ApplyPowerAction.class,
        method="update"
)
public class OnReceivePowerPatch
{
    @SpireInsertPatch(
            locator=Locator.class,
            localvars={"powerToApply"}
    )
    public static void Insert(ApplyPowerAction __instance, AbstractPower powerToApply)
    {
        if (__instance.target != null) {
            for (AbstractPower power : __instance.target.powers) {
                if (power instanceof OnReceivePowerPower) {
                    ((OnReceivePowerPower)power).onReceivePower(powerToApply, __instance.target, __instance.source);
                }
            }
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
