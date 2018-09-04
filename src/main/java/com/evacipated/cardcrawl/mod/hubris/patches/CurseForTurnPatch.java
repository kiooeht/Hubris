package com.evacipated.cardcrawl.mod.hubris.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CurseForTurnPatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method=SpirePatch.CLASS
    )
    public static class Field
    {
        public static SpireField<Boolean> curseForTurn = new SpireField<>(() -> false);
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="resetAttributes"
    )
    public static class ResetAttibutes
    {
        public static void Prefix(AbstractCard __instance)
        {
            if (Field.curseForTurn.get(__instance)) {
                Field.curseForTurn.set(__instance, false);
                AbstractCard copy = __instance.makeCopy();
                __instance.color = copy.color;
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="renderEnergy"
    )
    public static class RenderEnergy
    {
        public static SpireReturn Prefix(AbstractCard __instance, SpriteBatch sb)
        {
            if (Field.curseForTurn.get(__instance)) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.cards.AbstractCard",
            method="canUse"
    )
    public static class CanUse
    {
        public static SpireReturn<Boolean> Prefix(AbstractCard __instance, AbstractPlayer p, AbstractMonster m)
        {
            if (Field.curseForTurn.get(__instance)) {
                __instance.cantUseMessage = AbstractCard.TEXT[13];
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }
}
