package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.cards.DuctTapeCard;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;
import java.util.List;

public class DuctTape extends HubrisRelic
{
    public static final String ID = "hubris:DuctTape";
    private static final int COUNT = 2;
    private boolean cardSelected = true;

    private static final String CONFIG_KEY = "ductTape_";

    public DuctTape()
    {
        super(ID, "ductTape.png", RelicTier.UNCOMMON, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    public static void save(SpireConfig config, int startIndex, int endIndex)
    {
        if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ID)) {
            for (int i=startIndex; i<endIndex; ++i) {
                config.setInt(CONFIG_KEY + (i - startIndex), i);
            }
        } else {
            config.remove(CONFIG_KEY);
        }
    }

    public static void load(SpireConfig config)
    {
        if (AbstractDungeon.player.hasRelic(ID)) {
            int insertIndex = -1;
            List<AbstractCard> cards = new ArrayList<>(COUNT);
            for (int i=0; i<COUNT; ++i) {
                if (config.has(CONFIG_KEY + i)) {
                    int cardIndex = config.getInt(CONFIG_KEY + i);

                    if (cardIndex >= 0 && cardIndex < AbstractDungeon.player.masterDeck.group.size()) {
                        if (insertIndex == -1) {
                            insertIndex = cardIndex;
                        }
                        AbstractCard c = AbstractDungeon.player.masterDeck.group.get(cardIndex);
                        if (c == null) {
                            return;
                        }
                        cards.add(c);
                    }
                }
            }
            AbstractDungeon.player.masterDeck.group.removeAll(cards);
            AbstractDungeon.player.masterDeck.group.add(insertIndex, new DuctTapeCard(cards));
        }
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
