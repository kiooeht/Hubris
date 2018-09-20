package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ScarierMaskPatches
{
    @SpirePatch(
            clz=SummonGremlinAction.class,
            method="identifySlot"
    )
    public static class GremlinLeader
    {
        public static SpireReturn<Integer> Prefix(SummonGremlinAction __instance, AbstractMonster[] gremlins)
        {
            for (int i=0; i<gremlins.length; ++i) {
                if (gremlins[i] == null || gremlins[i].isDying || gremlins[i].escaped) {
                    return SpireReturn.Return(i);
                }
            }
            return SpireReturn.Continue();
        }
    }
}
