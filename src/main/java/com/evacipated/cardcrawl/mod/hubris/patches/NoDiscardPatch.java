package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.actions.utility.LimboToHandAction;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.NoDiscardField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        cls="com.megacrit.cardcrawl.actions.utility.UseCardAction",
        method="update"
)
public class NoDiscardPatch
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getClassName().equals("com.megacrit.cardcrawl.cards.CardGroup")
                        && m.getMethodName().equals("moveToDiscardPile")) {
                    m.replace("if (com.evacipated.cardcrawl.mod.hubris.patches.NoDiscardPatch.Nested.Do($1)) {" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }

    public static class Nested
    {
        public static boolean Do(AbstractCard card)
        {
            if (NoDiscardField.noDiscard.get(card)) {
                NoDiscardField.noDiscard.set(card, false);
                AbstractDungeon.player.limbo.addToTop(card);
                AbstractDungeon.actionManager.addToTop(new LimboToHandAction(card));
                return false;
            }
            return true;
        }
    }
}
