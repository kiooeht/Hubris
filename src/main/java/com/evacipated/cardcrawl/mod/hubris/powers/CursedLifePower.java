package com.evacipated.cardcrawl.mod.hubris.powers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.hubris.vfx.cardManip.BetterShowCardAndObtainEffect;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.CollectorCurseEffect;

public class CursedLifePower extends TwoAmountPower implements HealthBarRenderPower
{
    public static final String POWER_ID = "hubris:CursedLife";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private static Color color1 = Color.PURPLE.cpy();
    private static Color color2 = Color.PINK.cpy();

    private float colorTimer = 0;

    public CursedLifePower(AbstractCreature owner, int amount, int selfDamage)
    {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        amount2 = selfDamage;
        type = PowerType.BUFF;
        updateDescription();
        priority = -99;
        isTurnBased = true;
        loadRegion("brutality");
    }

    @Override
    public void updateDescription()
    {
        description = DESCRIPTIONS[0] + amount2
                + DESCRIPTIONS[1] + FontHelper.colorString(owner.name, "y")
                + DESCRIPTIONS[2] + amount;
        if (amount == 1) {
            description += DESCRIPTIONS[4];
        } else {
            description += DESCRIPTIONS[3];
        }
        description += DESCRIPTIONS[5];
    }

    @Override
    public void onDeath()
    {
        flash();
        for (int i=0; i<amount; ++i) {
            AbstractDungeon.effectList.add(new BetterShowCardAndObtainEffect(
                    AbstractDungeon.returnRandomCurse(),
                    Settings.WIDTH / 2.0f,
                    Settings.HEIGHT / 2.0f
            ));
        }
    }

    @Override
    public void duringTurn()
    {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, ID, 1));
        updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new CollectorCurseEffect(owner.hb.cX, owner.hb.cY), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(owner, owner, amount2));
            }
        }
    }

    @Override
    public void onRemove()
    {
        if (amount > 1) {
            // Add a copy, only one will be removed
            owner.powers.add(0, this);
            // Cancel the removal text effect
            AbstractDungeon.effectList.remove(AbstractDungeon.effectList.size() - 1);
        }
    }

    @Override
    public int getHealthBarAmount()
    {
        return amount2;
    }

    @Override
    public Color getColor()
    {
        colorTimer += Gdx.graphics.getDeltaTime() * 4;

        return color1.cpy().lerp(color2, (float) ((Math.sin(colorTimer) + 1) / 2));
    }
}
