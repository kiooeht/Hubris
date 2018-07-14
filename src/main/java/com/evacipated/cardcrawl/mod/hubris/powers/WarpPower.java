package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WarpPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Warp";

    private AbstractMonster parent;

    public WarpPower(AbstractCreature owner, AbstractMonster parent)
    {
        name = "Warp";
        ID = POWER_ID;
        this.owner = owner;
        this.parent = parent;
        updateDescription();
        loadRegion("confusion");
    }

    @Override
    public void updateDescription()
    {
        description = "On death, " + parent.name + " will return.";
    }

    @Override
    public void onDeath()
    {
        AbstractDungeon.actionManager.addToTop(new ClearCardQueueAction());

        AbstractDungeon.getCurrRoom().cannotLose = false;
        AbstractDungeon.getCurrRoom().monsters.monsters.clear();
        AbstractDungeon.getCurrRoom().monsters.add(parent);

        parent.createIntent();
        AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(parent, parent));
    }
}
