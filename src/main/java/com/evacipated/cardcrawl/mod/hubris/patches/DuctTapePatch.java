package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class DuctTapePatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="renderImage"
    )
    public static class RenderCardBg
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("renderCardBg")) {
                        m.replace("if (this instanceof com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard) {" +
                                "((com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard) this).renderDuctTapeCardBg($$);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }
}
