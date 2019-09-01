package com.evacipated.cardcrawl.mod.hubris.patches;

import com.evacipated.cardcrawl.mod.hubris.actions.unique.ZylophoneUseAction;
import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.ZylophoneField;
import com.evacipated.cardcrawl.mod.hubris.relics.Zylophone;
import com.evacipated.cardcrawl.mod.stslib.RelicTools;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Corruption;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class ZylophonePatch
{
    @SpirePatch(
            clz=AbstractPlayer.class,
            method="useCard"
    )
    public static class MultiUse
    {
        public static ExprEditor Instrument()
        {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException
                {
                    if (m.getClassName().equals(AbstractCard.class.getName()) && m.getMethodName().equals("use")) {
                        //m.replace(MultiUse.class.getName() + ".use($0, $$, energyOnUse);");
                        m.replace(
                                "if (" + MultiUse.class.getName() + ".isZylophone($0)) {" +
                                        MultiUse.class.getName() + ".use($0, $$, energyOnUse);" +
                                        "} else {" +
                                        "$_ = $proceed($$);" +
                                        "}"
                        );
                    }
                }
            };
        }

        public static boolean isZylophone(AbstractCard c)
        {
            return ZylophoneField.costsX.get(c);
        }

        public static void use(AbstractCard c, AbstractPlayer player, AbstractMonster monster, int energyOnUse)
        {
            if (player.hasRelic(ChemicalX.ID)) {
                player.getRelic(ChemicalX.ID).flash();
                energyOnUse += 2;
            }
            AbstractDungeon.actionManager.addToBottom(new ZylophoneUseAction(c, player, monster, energyOnUse));
        }
    }

    @SpirePatch(
            clz=AbstractPlayer.class,
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
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractCard.class, "costForTurn");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    private static boolean has01CostCard()
    {
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.cost == 0 || c.cost == 1) {
                return true;
            }
        }
        return false;
    }

    @SpirePatch(
            clz=AbstractDungeon.class,
            method="returnRandomRelicKey"
    )
    @SpirePatch(
            clz=AbstractDungeon.class,
            method="returnEndRandomRelicKey"
    )
    public static class AvoidIfNoAppropriateCards
    {
        public static String Postfix(String __result, AbstractRelic.RelicTier tier)
        {
            if (__result.equals(Zylophone.ID) && !has01CostCard()) {
                RelicTools.returnRelicToPool(tier, __result);
                return AbstractDungeon.returnRandomRelicKey(tier);
            }
            return __result;
        }
    }
}
