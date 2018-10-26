package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.List;

public class DuctTape extends HubrisRelic implements CustomSavable<List<CardSave>>
{
    public static final String ID = "hubris:DuctTape";
    private static final int COUNT = 2;
    private boolean cardSelected = true;

    public DuctTape()
    {
        super(ID, "ductTape.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public List<CardSave> onSave()
    {
        for (int i=0; i<AbstractDungeon.player.masterDeck.size(); ++i) {
            AbstractCard c = AbstractDungeon.player.masterDeck.group.get(i);
            if (c instanceof DuctTapeCard) {
                return ((DuctTapeCard) c).makeCardSaves();
            }
        }
        return null;
    }

    @Override
    public void onLoad(List<CardSave> cardSaves)
    {
        if (cardSaves == null) {
            return;
        }

        List<AbstractCard> cards = new ArrayList<>();
        for (CardSave save : cardSaves) {
            cards.add(CardLibrary.getCopy(save.id, save.upgrades, save.misc));
        }
        AbstractDungeon.player.masterDeck.addToTop(new DuctTapeCard(cards));
    }

    @Override
    public void onEquip()
    {
        cardSelected = false;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        AbstractDungeon.gridSelectScreen.open(CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck),
                COUNT, DESCRIPTIONS[1] + name + ".",
                false, false, false, false);
    }

    @Override
    public void update()
    {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == COUNT) {
            cardSelected = true;

            List<AbstractCard> cards = new ArrayList<>(COUNT);
            for (int i=0; i<COUNT; ++i) {
                cards.add(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
                AbstractDungeon.player.masterDeck.group.remove(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
            }
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new DuctTapeCard(cards), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new DuctTape();
    }
}
