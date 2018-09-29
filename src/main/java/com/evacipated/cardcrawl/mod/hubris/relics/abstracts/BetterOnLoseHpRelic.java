package com.evacipated.cardcrawl.mod.hubris.relics.abstracts;

import com.megacrit.cardcrawl.cards.DamageInfo;

public interface BetterOnLoseHpRelic
{
    int betterOnLoseHp(DamageInfo info, int damageAmount);
}
