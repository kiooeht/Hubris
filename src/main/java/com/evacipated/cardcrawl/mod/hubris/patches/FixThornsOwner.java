package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.powers.ThornsPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.NewExpr;

@SpirePatch(
        clz=ThornsPower.class,
        method="stackPower"
)
public class FixThornsOwner
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(NewExpr e) throws CannotCompileException
            {
                if (e.getClassName().equals(DamageInfo.class.getName())) {
                    e.replace("{" +
                            "$1 = thornsInfo.owner;" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }
}
