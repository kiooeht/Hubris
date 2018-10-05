package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.powers.ChampionShieldPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ChampionShield extends HubrisRelic
{
    public static final String ID = "hubris:ChampionShield";
    private boolean isActive = false;

    public ChampionShield()
    {
        super(ID, "championShield.png", RelicTier.COMMON, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void atBattleStart()
    {
        isActive = false;
        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction()
        {
            @Override
            public void update()
            {
                if (!ChampionShield.this.isActive && AbstractDungeon.player.isBloodied) {
                    ChampionShield.this.flash();
                    ChampionShield.this.pulse = true;
                    AbstractDungeon.player.addPower(new ChampionShieldPower(AbstractDungeon.player));
                    AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, ChampionShield.this));

                    ChampionShield.this.isActive = true;
                }
                isDone = true;
            }
        });
    }

    @Override
    public void onBloodied()
    {
        flash();
        pulse = true;
        if (!isActive && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(p, p, new ChampionShieldPower(p)));
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(p, this));
            isActive = true;
        }
    }

    @Override
    public void onNotBloodied()
    {
        stopPulse();
        isActive = false;
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            AbstractPlayer p = AbstractDungeon.player;
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(p, p, ChampionShieldPower.POWER_ID));
        }
    }

    @Override
    public void onVictory()
    {
        pulse = false;
        isActive = false;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ChampionShield();
    }
}
