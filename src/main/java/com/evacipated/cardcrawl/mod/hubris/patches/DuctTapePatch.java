package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.*;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;

public class DuctTapePatch
{
    @SpirePatch(
            clz=SingleCardViewPopup.class,
            method="open",
            paramtypez={
                    AbstractCard.class
            }
    )
    public static class LargeCardView_1
    {
        public static void Postfix(SingleCardViewPopup __instance, AbstractCard card)
        {
            if (card instanceof DuctTapeCard) {
                DuctTapeCard ductTapeCard = (DuctTapeCard) card;
                try {
                    Field f = SingleCardViewPopup.class.getDeclaredField("portraitImg");
                    f.setAccessible(true);
                    f.set(__instance, ductTapeCard.calculateLargePortrait());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SpirePatch(
            clz=SingleCardViewPopup.class,
            method="open",
            paramtypez={
                    AbstractCard.class,
                    CardGroup.class
            }
    )
    public static class LargeCardView_2
    {
        public static void Postfix(SingleCardViewPopup __instance, AbstractCard card, CardGroup group)
        {
            LargeCardView_1.Postfix(__instance, card);
        }
    }

    @SpirePatch(
            clz=SingleCardViewPopup.class,
            method="render"
    )
    public static class LargeRenderCardBg
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getMethodName().equals("renderCardBack")) {
                        m.replace("if (card instanceof " + DuctTapeCard.class.getName() + ") {" +
                                "((" + DuctTapeCard.class.getName() + ") card).renderDuctTapeLargeCardBg($$);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    } else if (m.getMethodName().equals("renderFrame")) {
                        m.replace("if (card instanceof " + DuctTapeCard.class.getName() + ") {" +
                                "((" + DuctTapeCard.class.getName() + ") card).renderDuctTapeLargeFrame($$);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }

                @Override
                public void edit(FieldAccess f) throws CannotCompileException
                {
                    if (f.getFieldName().equals("isViewingUpgrade")) {
                        f.replace("if (card instanceof " + DuctTapeCard.class.getName() + ") {" +
                                "$_ = false;" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }

        private static AbstractCard ductTapeCard = null;
        private static AbstractCard ductTapeCardOrig = null;
        @SpireInsertPatch(
                rloc=1,
                localvars={"card"}
        )
        public static void Insert1(SingleCardViewPopup __instance, SpriteBatch sb, @ByRef AbstractCard[] card)
        {
            if (card[0] != ductTapeCardOrig) {
                ductTapeCardOrig = null;
                ductTapeCard = null;
            }

            if (card[0] instanceof DuctTapeCard && SingleCardViewPopup.isViewingUpgrade && ductTapeCard == null) {
                ductTapeCardOrig = card[0];
                ductTapeCard = card[0].makeStatEquivalentCopy();
                card[0].upgrade();
                card[0].displayUpgrades();
            }

            if (card[0] instanceof DuctTapeCard && !SingleCardViewPopup.isViewingUpgrade && ductTapeCard != null) {
                card[0] = ductTapeCard;
                ductTapeCard = null;
                ductTapeCardOrig = null;
            }
        }
    }
}
