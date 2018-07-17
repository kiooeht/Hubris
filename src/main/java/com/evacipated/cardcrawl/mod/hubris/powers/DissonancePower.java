package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.hubris.cards.colorless.MarkOfDissonance;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DissonancePower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Dissonance";
    private AbstractCard card;

    public DissonancePower(AbstractCreature owner)
    {
        name = "Dissonance";
        ID = POWER_ID;
        this.owner = owner;
        this.card = new MarkOfDissonance();
        type = PowerType.DEBUFF;
        updateDescription();
        loadRegion("vulnerable");
    }

    @Override
    public void updateDescription()
    {
        description = "Take #b" + 50 + "% more damage from #yAttacks. NL On death, " + FontHelper.colorString(card.name, "y") + " is shuffled into your draw pile.";
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (type == DamageInfo.DamageType.NORMAL) {
            return damage * 1.5f;
        }
        return damage;
    }

    @Override
    public void onDeath()
    {
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(card, 1, true, false));
    }
}
