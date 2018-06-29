package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BlackHole extends AbstractRelic
{
    public static final String ID = "hubris:Black Hole";

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
    public void onDrawOrDiscard()
    {
        try {
            Method resetCardBeforeMoving = CardGroup.class.getDeclaredMethod("resetCardBeforeMoving", AbstractCard.class);
            resetCardBeforeMoving.setAccessible(true);

            List<AbstractCard> list = new ArrayList<>(AbstractDungeon.player.discardPile.group);
            for (AbstractCard c : list) {
                //AbstractDungeon.player.discardPile.moveToExhaustPile(c);
                resetCardBeforeMoving.invoke(AbstractDungeon.player.discardPile, c);
                AbstractDungeon.player.exhaustPile.addToTop(c);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
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
