package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.BlackHole;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class BlackHoleDeadBranchFix
{
    @SpirePatch(
            clz=ShowCardAndAddToDiscardEffect.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez={
                    AbstractCard.class,
                    float.class,
                    float.class
            }
    )
    @SpirePatch(
            clz=ShowCardAndAddToDiscardEffect.class,
            method=SpirePatch.CONSTRUCTOR,
            paramtypez={
                    AbstractCard.class
            }
    )
    public static class Ctors
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor()
            {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException
                {
                    if (f.getClassName().equals(AbstractPlayer.class.getName()) && f.getFieldName().equals("discardPile")) {
                        f.replace("if (" + AbstractDungeon.class.getName() + ".player.hasRelic(" + BlackHole.class.getName() + ".ID)) {" +
                                "$_ = " + AbstractDungeon.class.getName() + ".player.exhaustPile;" +
                                "} else {" +
                                "$_ = $proceed($$);" +
                                "}");
                    }
                }
            };
        }
    }
}
