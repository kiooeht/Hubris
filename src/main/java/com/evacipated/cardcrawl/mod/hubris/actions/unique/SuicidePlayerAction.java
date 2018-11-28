package com.evacipated.cardcrawl.mod.hubris.actions.unique;

import com.evacipated.cardcrawl.mod.hubris.powers.PotatoPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SuicidePlayerAction extends AbstractGameAction
{
    public SuicidePlayerAction()
    {
        duration = 0.0f;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;
        // Because Potato was causing a soft lock
        p.powers.removeIf(power -> PotatoPower.POWER_ID.equals(power.ID));
        p.currentHealth = 0;
        p.healthBarUpdatedEvent();
        p.damage(new DamageInfo(p, 1, DamageInfo.DamageType.HP_LOSS));
        isDone = true;
    }
}
