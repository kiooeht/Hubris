package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import constructmod.cards.AbstractCycleCard;

public class ClockworkCow extends AbstractRelic
{
    public static final String ID = "hubris:ClockworkCow";
    private static final int DAMAGE = 3;

    public ClockworkCow()
    {
        super(ID, "clockworkCow.png", RelicTier.UNCOMMON, LandingSound.CLINK);

        setCounter(0);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + DAMAGE + DESCRIPTIONS[1];
    }

    public void onCycleCard(AbstractCycleCard card)
    {
        setCounter(counter + 1);
        if (counter == 10) {
            setCounter(0);
            flash();
            pulse = false;
            AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(null,
                    DamageInfo.createDamageMatrix(DAMAGE, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            AbstractDungeon.actionManager.addToTop(new TalkAction(true, "Moo.", 1.0F, 2.0F));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        } else if (counter == 9) {
            beginPulse();
            pulse = true;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ClockworkCow();
    }
}
