package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.cards.blue.DoWhile;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMiscAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.blue.GeneticAlgorithm;
import com.megacrit.cardcrawl.cards.red.Rampage;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;

public class DoWhilePower extends AbstractPower
{
    public static final String POWER_ID = "hubris:DoWhilePower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final int MAX_LOOPS = 100;

    private AbstractCard originalCard = null;
    private int stacks;

    public DoWhilePower(AbstractCreature owner)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        type = PowerType.BUFF;
        amount = stacks = 1;
        updateDescription();
        loadRegion("loop");
    }

    @Override
    public void updateDescription()
    {
        if (stacks > 1) {
            description = DESCRIPTIONS[1] + stacks + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0];
        }
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action)
    {
        if (!card.cardID.equals(DoWhile.ID)) {
            if (originalCard == null) {
                originalCard = card;
                stacks = amount;
                amount = MAX_LOOPS;
            }
            if (!originalCard.cardID.equals(card.cardID)) {
                return;
            }

            flash();
            AbstractMonster m = null;
            if (action.target != null) {
                m = (AbstractMonster) action.target;
            }
            AbstractCard tmp = card.makeSameInstanceOf();
            tmp.freeToPlayOnce = false;
            AbstractDungeon.player.limbo.addToBottom(tmp);
            tmp.current_x = card.current_x;
            tmp.current_y = card.current_y;
            tmp.target_x = Settings.WIDTH / 2.0f - 300.0f * Settings.scale;
            tmp.target_y = Settings.HEIGHT / 2.0f;
            if (card == originalCard) {
                // Because tmp.modifyCostForTurn doesn't work on 0 cost cards
                if (tmp.costForTurn >= 0) {
                    tmp.costForTurn += 1;
                    if (tmp.costForTurn < 0) {
                        tmp.costForTurn = 0;
                    }

                    if (tmp.costForTurn != tmp.cost) {
                        tmp.isCostModifiedForTurn = true;
                    }
                    // Because if cost is 0, game skips spending energy, even if costForTurn isn't 0
                    tmp.cost = tmp.costForTurn;
                }
            }
            if (m != null) {
                tmp.calculateCardDamage(m);
            }
            tmp.purgeOnUse = true;

            --amount;
            int energyOnUse = card.energyOnUse;
            if (card.costForTurn == -1) {
                --energyOnUse;
                if (energyOnUse == 0) {
                    amount = -1;
                }
            }
            AbstractDungeon.actionManager.cardQueue.add(0, new CardQueueItem(tmp, m, energyOnUse));

            if (amount <= 0 || !tmp.canUse(AbstractDungeon.player, m)) {
                if (amount == 0) {
                    AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 5.0f,
                            "#rABORTING #rSOFTLOCK", true));
                }
                amount = stacks;
                originalCard = null;
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ID, 1));
                //AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        if (isPlayer) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
        }
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        stacks = amount;
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        stacks = amount;
    }
}
