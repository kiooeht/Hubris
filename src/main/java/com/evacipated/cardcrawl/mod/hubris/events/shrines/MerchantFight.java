package com.evacipated.cardcrawl.mod.hubris.events.shrines;

import com.megacrit.cardcrawl.events.AbstractEvent;

public class MerchantFight extends AbstractEvent
{
    public MerchantFight()
    {
        noCardsInRewards = true;
        enterCombat();
    }

    @Override
    protected void buttonEffect(int i)
    {

    }
}
