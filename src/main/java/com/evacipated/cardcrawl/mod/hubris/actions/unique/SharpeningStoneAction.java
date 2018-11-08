package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PiercingTag;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SharpeningStoneAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SharpeningStoneAction");
    public static final String[] TEXT = uiStrings.TEXT;

    private List<AbstractCard> cannotPlay = new ArrayList<>();

    public SharpeningStoneAction(AbstractCreature source, int amount)
    {
        setValues(AbstractDungeon.player, source, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_MED;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_MED) {
            if (!AbstractDungeon.player.hand.isEmpty()) {
                for (AbstractCard c : AbstractDungeon.player.hand.group) {
                    if (c.type != AbstractCard.CardType.ATTACK || c.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                        cannotPlay.add(c);
                    }
                }
                if (cannotPlay.size() == AbstractDungeon.player.hand.size()) {
                    isDone = true;
                    return;
                }

                AbstractDungeon.player.hand.group.removeAll(cannotPlay);

                if (AbstractDungeon.player.hand.size() == 1) {
                    sharpenCard(AbstractDungeon.player.hand.getTopCard(), true);
                    returnCards();
                    isDone = true;
                    return;
                }

                AbstractDungeon.handCardSelectScreen.open(TEXT[0], amount, false, false, false, false, true);
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25f));
                tickDuration();
                return;
            }
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                sharpenCard(c, false);
            }
            returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.clear();
        }
        tickDuration();
    }

    private void sharpenCard(AbstractCard c, boolean singleCard)
    {
        // Make piercing
        c.tags.add(PiercingTag.HUBRIS_PIERCING);
        c.damageTypeForTurn = DamageInfo.DamageType.HP_LOSS;
        // Prepend piercing to description
        c.rawDescription = TEXT[1] + c.rawDescription;
        c.initializeDescription();
        // Alter name
        c.name = "=" + c.name + "=";
        try {
            Method initializeTitle = AbstractCard.class.getDeclaredMethod("initializeTitle");
            initializeTitle.setAccessible(true);
            initializeTitle.invoke(c);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        if (!singleCard) {
            AbstractDungeon.player.hand.addToTop(c);
        }
    }

    private void returnCards()
    {
        for (AbstractCard c : cannotPlay) {
            AbstractDungeon.player.hand.addToTop(c);
        }
        AbstractDungeon.player.hand.refreshHandLayout();
    }
}
