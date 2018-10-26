package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardSave;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

// Removes the Duct Tape card when saving
@SpirePatch(
        clz= CardGroup.class,
        method="getCardDeck"
)
public class DuctTapeSavePatch
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
                    m.replace("if (!com.evacipated.cardcrawl.mod.hubris.patches.DuctTapeSavePatch.Do(retVal, card)) {" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }

    public static boolean Do(ArrayList<CardSave> retVal, AbstractCard card)
    {
        return card instanceof DuctTapeCard;
    }
}
