package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class BlackHole extends AbstractRelic
{
    public static final String ID = "hubris:BlackHole";

    public BlackHole()
    {
        super(ID, "blackhole.png", RelicTier.BOSS, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            c.modifyCostForCombat(-9);
        }
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            c.modifyCostForCombat(-9);
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            c.modifyCostForCombat(-9);
            AbstractDungeon.actionManager.addToBottom(new ExhaustSpecificCardAction(c, AbstractDungeon.player.discardPile));
        }
        for (AbstractCard c : AbstractDungeon.player.exhaustPile.group) {
            c.modifyCostForCombat(-9);
        }
    }

    @Override
    public void onEquip()
    {
        atBattleStartPreDraw();
    }

    @Override
    public void onRefreshHand()
    {
        atBattleStartPreDraw();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BlackHole();
    }
}
