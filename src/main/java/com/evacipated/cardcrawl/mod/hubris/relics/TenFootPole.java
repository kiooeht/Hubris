package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic;
import com.evacipated.cardcrawl.mod.stslib.relics.OnReceivePowerRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import mimicmod.monsters.Mimic;
import mimicmod.powers.MimicSurprisePower;

public class TenFootPole extends HubrisRelic implements OnReceivePowerRelic, BetterOnLoseHpRelic
{
    public static final String ID = "hubris:TenFootPole";

    public TenFootPole()
    {
        super(ID, "test5.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        String ret = DESCRIPTIONS[0];
        if (HubrisMod.hasMimicMod) {
            ret += DESCRIPTIONS[1];
        }
        return ret;
    }


    @Override
    public int betterOnLoseHp(DamageInfo info, int damageAmount)
    {
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) {
            flash();
            return super.onAttacked(info, damageAmount) / 2;
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public boolean onReceivePower(AbstractPower power, AbstractCreature source)
    {
        if (HubrisMod.hasMimicMod) {
            if (power instanceof EntanglePower && source instanceof Mimic) {
                return false;
            } else if (power instanceof MimicSurprisePower) {
                return false;
            }
        }
        return true;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new TenFootPole();
    }
}
