package com.evacipated.cardcrawl.mod.hubris.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StunPower extends AbstractPower
{
    public static final String POWER_ID = "hubris:Stunned";

    private byte moveByte;
    private AbstractMonster.Intent moveIntent;

    public StunPower(AbstractCreature owner)
    {
        name = "Stunned";
        ID = POWER_ID;
        this.owner = owner;
        amount = 1;
        type = PowerType.DEBUFF;
        updateDescription();

        if (owner instanceof AbstractMonster) {
            AbstractMonster m = (AbstractMonster)owner;
            moveByte = m.nextMove;
            moveIntent = m.intent;
        }
    }

    @Override
    public void updateDescription()
    {
        description = "Does nothing for #b" + amount + (amount == 1 ? " turn." : " turns.");
        img = ImageMaster.loadImage("images/relics/torii.png");
    }

    @Override
    public void atEndOfRound()
    {
        reducePower(1);
        if (amount <= 0) {
            if (owner instanceof AbstractMonster) {
                AbstractMonster m = (AbstractMonster)owner;
                m.setMove(moveByte, moveIntent);
                m.createIntent();
            }
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, ID));
        }
    }
}
