package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.ZylophoneField;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Corruption;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CtBehavior;

public class ZylophonePatch
{
    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method="useCard"
    )
    public static class MultiUse
    {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse)
        {
            if (ZylophoneField.costsX.get(c)) {
                for (int i = 0; i < energyOnUse - 1; ++i) {
                    c.use(__instance, monster);
                }
                if (__instance.hasRelic(ChemicalX.ID)) {
                    __instance.getRelic(ChemicalX.ID).flash();
                    c.use(__instance, monster);
                    c.use(__instance, monster);
                }
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.cards.AbstractCard", "use");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            cls="com.megacrit.cardcrawl.characters.AbstractPlayer",
            method="useCard"
    )
    public static class UseEnergy
    {
        @SpireInsertPatch(
                locator=Locator.class
        )
        public static void Insert(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse)
        {
            if (ZylophoneField.costsX.get(c) && c.costForTurn == -1 && !c.freeToPlayOnce
                && (!__instance.hasPower(Corruption.ID) || c.type != AbstractCard.CardType.SKILL)) {
                __instance.energy.use(EnergyPanel.totalCount);
            }
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher("com.megacrit.cardcrawl.cards.AbstractCard", "cost");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
