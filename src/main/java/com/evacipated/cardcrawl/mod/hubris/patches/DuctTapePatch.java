package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.mod.hubris.relics.DuctTape;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.lang.reflect.Field;

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
                    } else if (m.getMethodName().equals("renderPortraitFrame")) {
                        m.replace("if (this instanceof com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard) {" +
                                "((com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard) this).renderDuctTapePortraitFrame($$);" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }

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
                    f.set(__instance, ductTapeCard.largePortrait.getTexture());
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
}
