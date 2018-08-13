package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.actions.utility.LimboToHandAction;
import com.evacipated.cardcrawl.mod.hubris.powers.WhilePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class WhileAction extends AbstractGameAction
{
    public WhileAction()
    {
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.WAIT;
        source = AbstractDungeon.player;
    }

    @Override
    public void update()
    {
        if (AbstractDungeon.player.drawPile.size() + AbstractDungeon.player.discardPile.size() == 0) {
            isDone = true;
            return;
        }
        if (AbstractDungeon.player.drawPile.isEmpty()) {
            AbstractDungeon.actionManager.addToTop(new WhileAction());
            AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
            isDone = true;
            return;
        } else {
            AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
            AbstractDungeon.player.drawPile.group.remove(card);
            AbstractDungeon.getCurrRoom().souls.remove(card);
            AbstractDungeon.player.limbo.group.add(card);
            card.current_y = -200.0f * Settings.scale;
            card.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
            card.target_y = Settings.HEIGHT / 2.0f;
            card.targetAngle = 0.0f;
            card.lighten(false);
            card.drawScale = 0.12f;
            card.targetDrawScale = 0.75f;
            AbstractMonster target = AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRng);
            if (!card.canUse(AbstractDungeon.player, target)) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, WhilePower.POWER_ID));
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                AbstractDungeon.actionManager.addToTop(new LimboToHandAction(card));
                AbstractDungeon.actionManager.addToTop(new WaitAction(0.4f));
            } else {
                card.applyPowers();
                AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
                AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
                if (!Settings.FAST_MODE) {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
                } else {
                    AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
                }
                if (!AbstractDungeon.player.hasPower(WhilePower.POWER_ID)) {
                    AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                            new WhilePower(AbstractDungeon.player)));
                }
            }
        }

        isDone = true;
    }
}
