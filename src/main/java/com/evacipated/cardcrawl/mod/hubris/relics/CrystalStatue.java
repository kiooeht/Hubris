package com.evacipated.cardcrawl.mod.hubris.relics;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class CrystalStatue extends AbstractRelic
{
    public static final String ID = "hubris:CrystalStatue";
    private static final int AMT = 20;

    public CrystalStatue()
    {
        super(ID, "crystalStatue.png", RelicTier.UNCOMMON, LandingSound.MAGICAL);

        setCounter(AMT);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter > 0) {
            return DESCRIPTIONS[3] + DESCRIPTIONS[1] + counter + DESCRIPTIONS[2];
        }
        return DESCRIPTIONS[0];
    }

    @Override
    public void setCounter(int c)
    {
        if (c < 0) {
            c = 0;
        }
        counter = c;
        if (counter == 0) {
            AbstractDungeon.player.energy.energyMaster -= 1;
            AbstractDungeon.player.energy.energy -= 1;
        }

        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
            flash();
            setCounter(counter - damageAmount);
        }

        return damageAmount;
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    @Override
    public void onUnequip()
    {
        if (counter > 0) {
            AbstractDungeon.player.energy.energyMaster -= 1;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new CrystalStatue();
    }
}
