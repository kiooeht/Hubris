package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.relics.MedicalManual;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;

@SpirePatch(
        cls="com.megacrit.cardcrawl.cards.AbstractCard",
        method="useMedicalKit"
)
public class MedicalManualPatch
{
    public static void Postfix(AbstractCard __instance, AbstractPlayer p)
    {
        if (p.hasRelic(MedicalManual.ID)) {
            p.getRelic(MedicalManual.ID).flash();
            p.heal(MedicalManual.HEAL);
        }
    }
}
