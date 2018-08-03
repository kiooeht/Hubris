package com.evacipated.cardcrawl.mod.hubris.powers;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class WarpPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Warp";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private AbstractMonster parent;

    public WarpPower(AbstractCreature owner, AbstractMonster parent)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.parent = parent;
        updateDescription();
        loadRegion("confusion");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + parent.name + DESCRIPTIONS[1];
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
