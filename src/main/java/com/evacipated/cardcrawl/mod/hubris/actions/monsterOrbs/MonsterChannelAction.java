package com.evacipated.cardcrawl.mod.hubris.actions.monsterOrbs;

import com.evacipated.cardcrawl.mod.hubris.monsters.OrbUsingMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

public class MonsterChannelAction extends AbstractGameAction
{
    private OrbUsingMonster owner;
    private AbstractOrb orbType;

    public MonsterChannelAction(OrbUsingMonster owner, AbstractOrb newOrbType)
    {
        duration = Settings.ACTION_DUR_FAST;
        this.owner = owner;
        orbType = newOrbType;
    }

    @Override
    public void update()
    {
        if (duration == Settings.ACTION_DUR_FAST) {
            owner.channelOrb(orbType);
        }
        tickDuration();
    }
}
