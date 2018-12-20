package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.interfaces.SpellArteLogicAffector;
import mysticmod.patches.MysticTags;

public class FoxTail extends HubrisRelic implements SpellArteLogicAffector
{
    public static final String ID = "hubris:FoxTail";

    public FoxTail()
    {
        super(ID, "foxTail.png", RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean modifyIsSpell(AbstractCard card, boolean isSpell)
    {
        if (card.hasTag(MysticTags.IS_CANTRIP)) {
            return true;
        }
        return isSpell;
    }

    @Override
    public boolean modifyIsArte(AbstractCard card, boolean isArte)
    {
        return isArte;
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new FoxTail();
    }
}
