package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import mysticmod.MysticMod;
import mysticmod.interfaces.SpellArteLogicAffector;
import mysticmod.patches.RefreshSpellArteLogicField;

public class MadmansSpellBook extends HubrisRelic implements SpellArteLogicAffector
{
    public static final String ID = "hubris:MadmansSpellBook";

    private boolean stopRecursion = false;

    public MadmansSpellBook()
    {
        super(ID, "madmansSpellBook.png", RelicTier.SHOP, LandingSound.SOLID);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public int getPrice()
    {
        return 50;
    }

    @Override
    public boolean modifyIsSpell(AbstractCard card, boolean isSpell)
    {
        if (stopRecursion) {
            return isSpell;
        }

        stopRecursion = true;
        RefreshSpellArteLogicField.checkArte.set(card, true);
        boolean ret = MysticMod.isThisAnArte(card);
        RefreshSpellArteLogicField.checkArte.set(card, true);
        stopRecursion = false;
        return ret;
    }

    @Override
    public boolean modifyIsArte(AbstractCard card, boolean isArte)
    {
        if (stopRecursion) {
            return isArte;
        }

        stopRecursion = true;
        RefreshSpellArteLogicField.checkSpell.set(card, true);
        boolean ret = MysticMod.isThisASpell(card);
        RefreshSpellArteLogicField.checkSpell.set(card, true);
        stopRecursion = false;
        return ret;
    }

    @Override
    public void onEquip()
    {
        // Force a refresh when this relic is obtained
        for (AbstractCard card : AbstractDungeon.player.hand.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.exhaustPile.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
        for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
            RefreshSpellArteLogicField.checkSpell.set(card, true);
            RefreshSpellArteLogicField.checkArte.set(card, true);
        }
    }

    @Override
    public void onUnequip()
    {
        // Force a refresh when this relic is lost
        onEquip();
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new MadmansSpellBook();
    }
}
