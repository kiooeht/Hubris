package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class PureNail extends HubrisRelic
{
    public static final String ID = "hubris:PureNail";

    public PureNail()
    {
        super(ID, "oldNail.png", RelicTier.SPECIAL, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void obtain()
    {
        if (AbstractDungeon.player.hasRelic(OldNail.ID)) {
            for (int i=0; i<AbstractDungeon.player.relics.size(); ++i) {
                if (AbstractDungeon.player.relics.get(i).relicId.equals(OldNail.ID)) {
                    instantObtain(AbstractDungeon.player, i, false);
                    break;
                }
            }
        } else {
            super.obtain();
        }
    }

    @Override
    public int onAttackedMonster(DamageInfo info, int damageAmount)
    {
        if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS&& info.type != DamageInfo.DamageType.THORNS) {
            return damageAmount * 2;
        }
        return damageAmount;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new PureNail();
    }
}
