package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.powers.BefuddledPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(
        cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
        method="draw",
        paramtypes={"int"}
)
public class BefuddledPatch
{
    public static ExprEditor Instrument()
    {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall m) throws CannotCompileException
            {
                if (m.getMethodName().equals("addToHand")) {
                    m.replace("{" +
                            "com.evacipated.cardcrawl.mod.hubris.patches.BefuddledPatch.Nested.Do($$);" +
                            "$_ = $proceed($$);" +
                            "}");
                }
            }
        };
    }

    public static class Nested
    {
        public static void Do(AbstractCard card)
        {
            BefuddledPower power = (BefuddledPower) AbstractDungeon.player.getPower(BefuddledPower.POWER_ID);
            if (power != null) {
                power.Do(card);
            }
        }
    }
}
