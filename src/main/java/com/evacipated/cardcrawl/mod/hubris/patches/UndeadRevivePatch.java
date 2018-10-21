package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.powers.UndeadPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        clz=GameActionManager.class,
        method="getNextAction"
)
public class UndeadRevivePatch
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(AbstractMonster.class.getName()) && m.getMethodName().equals("takeTurn")) {
                    m.replace("{" +
                            "if (m.nextMove == -72) {" +
                            UndeadRevivePatch.class.getName() + ".Revive(m);" +
                            "}" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }

    public static void Revive(AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new HealAction(m, m, m.maxHealth));
        AbstractPower undead = m.getPower(UndeadPower.POWER_ID);
        m.halfDead = false;
        m.isDying = false;

        if (undead != null) {
            undead.onSpecificTrigger();
        }
    }
}
