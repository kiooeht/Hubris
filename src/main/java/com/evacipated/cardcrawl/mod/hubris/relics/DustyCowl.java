package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.DevConsole;
import com.badlogic.gdx.Input;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Random;

public class DustyCowl extends AbstractRelic
{
    public static final String ID = "hubris:DustyCowl";
    private static final int BLOCK = 3;

    public DustyCowl()
    {
        super(ID, "test2.png", RelicTier.COMMON, LandingSound.HEAVY);

        rerollFlavorText();
    }

    public void rerollFlavorText()
    {
        RelicStrings newRelicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
        int n = 0;
        do {
            n = new Random().nextInt(8) + 2;
        } while (n == 5);
        flavorText = newRelicStrings.FLAVOR + String.format("f%slth.", new String(new char[n]).replace('\0', 'i'));
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + BLOCK + DESCRIPTIONS[1];
    }

    @Override
    public void onExhaust(AbstractCard card)
    {
        if (card.isEthereal) {
            flash();
            AbstractDungeon.actionManager.addToTop(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, BLOCK, true));
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DustyCowl();
    }
}
