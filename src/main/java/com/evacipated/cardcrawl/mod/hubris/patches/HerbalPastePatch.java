package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.HerbalPaste;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.RegenAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.RegenPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

import java.util.ArrayList;

public class HerbalPastePatch
{
    @SpirePatch(
            clz=RegenPower.class,
            method=SpirePatch.CONSTRUCTOR
    )
    public static class CanGoNegative
    {
        public static void Postfix(RegenPower __instance, AbstractCreature owner, int heal)
        {
            if (owner.isPlayer && AbstractDungeon.player.hasRelic(HerbalPaste.ID)) {
                __instance.canGoNegative = true;
            }
        }
    }

    @SpirePatch(
            clz=RegenPower.class,
            method="atEndOfTurn"
    )
    public static class AtEndOfTurn
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(NewExpr e) throws CannotCompileException
                {
                    if (e.getClassName().equals(RegenAction.class.getName())) {
                        e.replace("if ($2 < 0) {" +
                                //"--amount;" +
                                "updateDescription();" +
                                "$_ = new " + LoseHPAction.class.getName() + "($1, $1, -$2);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz=RegenAction.class,
            method="update"
    )
    public static class RegenActionPatch
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("remove")) {
                        m.replace("$_ = " + Nested.class.getName() + ".Do(target, p);");
                    }
                }
            };
        }

        public static class Nested
        {
            public static boolean Do(AbstractCreature target, AbstractPower power)
            {
                if (!((AbstractPlayer)target).hasRelic(HerbalPaste.ID)) {
                    return target.powers.remove(power);
                }
                return false;
            }
        }
    }
}
