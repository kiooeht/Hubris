package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        cls="com.megacrit.cardcrawl.actions.GameActionManager",
        method="getNextAction"
)
public class MonsterStun
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals("com.megacrit.cardcrawl.monsters.AbstractMonster")
                        && m.getMethodName().equals("takeTurn")) {
                    m.replace("if (!m.hasPower(com.evacipated.cardcrawl.mod.hubris.powers.StunPower.POWER_ID)) {" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }
}
