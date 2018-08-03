package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DEPRECATED_NoAttackPower;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="hasEnoughEnergy"
)
public class NoAttackPatch
{
    @SpireInsertPatch(
            rloc=5
    )
    public static SpireReturn Insert(AbstractCard __instance)
    {
        if (AbstractDungeon.player.hasPower(DEPRECATED_NoAttackPower.POWER_ID) && __instance.type == AbstractCard.CardType.ATTACK) {
            __instance.cantUseMessage = AbstractCard.TEXT[10];
            return SpireReturn.Return(false);
        }
        return SpireReturn.Continue();
    }
}
