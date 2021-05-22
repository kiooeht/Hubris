package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.SpawnDaggerAction;
import com.megacrit.cardcrawl.actions.unique.SummonGremlinAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.beyond.Reptomancer;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class ScarierMaskPatches
{
    @SpirePatch(
            clz=SummonGremlinAction.class,
            method="identifySlot"
    )
    public static class GremlinLeaderPatch
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

    @SpirePatch(
            clz=Reptomancer.class,
            method="canSpawn"
    )
    @SpirePatch(
            clz=Reptomancer.class,
            method="takeTurn"
    )
    @SpirePatch(
            clz=SpawnDaggerAction.class,
            method="update"
    )
    public static class ReptomancerPatch
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException
                {
                    if (f.getFieldName().equals("isDying")) {
                        f.replace("$_ = $proceed($$) || $0.isEscaping;");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz= MonsterGroup.class,
            method="shouldFlipVfx"
    )
    public static class FixShieldAndSpearCrash
    {
        public static SpireReturn<Boolean> Prefix(MonsterGroup __instance)
        {
            if (MonsterHelper.SHIELD_SPEAR_ENC.equals(AbstractDungeon.lastCombatMetricKey) &&
                    __instance.monsters.size() < 2) {
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
