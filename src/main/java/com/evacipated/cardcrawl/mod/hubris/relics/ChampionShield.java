package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.powers.ChampionShieldPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.HashSet;
import java.util.Set;

public class ChampionShield extends HubrisRelic implements OnReceivePowerRelic
{
    public static final String ID = "hubris:ChampionShield";
    private boolean isActive = false;

    public static Set<AbstractPower> debuffsFromMonsters = new HashSet<>();

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
    public void onUnequip()
    {
        debuffsFromMonsters.clear();
    }

    @Override
    public void atBattleStart()
    {
        debuffsFromMonsters.clear();

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
    public boolean onReceivePower(AbstractPower power, AbstractCreature source)
    {
        if (power.type == AbstractPower.PowerType.DEBUFF && source instanceof AbstractMonster) {
            debuffsFromMonsters.removeIf(p -> !AbstractDungeon.player.powers.contains(p));
            debuffsFromMonsters.add(power);
        }
        return true;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new ChampionShield();
    }
}
