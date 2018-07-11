package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.DisguiseKitOption;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class DisguiseKit extends AbstractRelic
{
    public static final String ID = "hubris:DisguiseKit";
    public AbstractPlayer.PlayerClass chosenClass = null;
    private boolean pickCard = false;

    public DisguiseKit()
    {
        super(ID, "test3.png", RelicTier.COMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        if (chosenClass == null) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[0] + chosenClassName() + DESCRIPTIONS[2];
        }
    }

    private String chosenClassName()
    {
        return "#b" + String.join(" #b", AbstractPlayer.getTitle(chosenClass).split(" "));
    }

    @Override
    public void onEquip()
    {
        flash();

        pickCard = true;
        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractPlayer.PlayerClass pc : AbstractPlayer.PlayerClass.values()) {
            if (pc != AbstractDungeon.player.chosenClass) {
                group.addToTop(new DisguiseKitOption(pc));
            }
        }

        AbstractDungeon.gridSelectScreen.open(group, 1, "", false);
    }

    @Override
    public void update()
    {
        super.update();

        if (pickCard && !AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            DisguiseKitOption selected = (DisguiseKitOption) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            chosenClass = selected.chosenClass;
            description = getUpdatedDescription();
            tips.clear();
            tips.add(new PowerTip(name, description));
            initializeTips();

            CardCrawlGame.dungeon.initializeCardPools();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DisguiseKit();
    }
}
