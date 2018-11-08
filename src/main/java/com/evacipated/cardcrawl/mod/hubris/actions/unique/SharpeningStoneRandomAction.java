package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PiercingTag;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SharpeningStoneRandomAction extends AbstractGameAction
{
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("SharpeningStoneAction");
    public static final String[] TEXT = uiStrings.TEXT;

    private List<AbstractCard> cannotPlay = new ArrayList<>();

    public SharpeningStoneRandomAction(AbstractCreature source, int amount)
    {
        setValues(AbstractDungeon.player, source, amount);
        actionType = ActionType.CARD_MANIPULATION;
        duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            CardGroup cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                if (c.type == AbstractCard.CardType.ATTACK && !c.hasTag(PiercingTag.HUBRIS_PIERCING)) {
                    cards.addToTop(c);
                }
            }
            for (int i=0; i<amount; ++i) {
                if (cards.isEmpty()) {
                    break;
                }
                AbstractCard c = cards.getRandomCard(AbstractDungeon.cardRandomRng);
                sharpenCard(c);
                cards.removeCard(c);
            }
        }
        tickDuration();
    }

    private void sharpenCard(AbstractCard c)
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
        // Effect
        AbstractDungeon.effectsQueue.add(new UpgradeShineEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
        AbstractCard tmp = c.makeStatEquivalentCopy();
        tmp.rawDescription = TEXT[1] + tmp.rawDescription;
        tmp.initializeDescription();
        AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(tmp));
    }
}
