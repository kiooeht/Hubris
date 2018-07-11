package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.patches.cards.AbstractCard.PyramidsField;
import com.megacrit.cardcrawl.actions.unique.DeckToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MysteriousPyramids extends AbstractRelic
{
    public static final String ID = "hubris:MysteriousPyramids";
    private static final int COUNT = 2;
    private boolean cardSelected = true;
    private List<AbstractCard> cards = new ArrayList<>(COUNT);

    public MysteriousPyramids()
    {
        super(ID, "pyramids.png", RelicTier.UNCOMMON, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onCardDraw(AbstractCard c)
    {
        if (PyramidsField.inPyramids.get(c)) {
            for (Iterator<AbstractCard> it = AbstractDungeon.player.drawPile.group.iterator(); it.hasNext();) {
                AbstractCard card = it.next();
                if (PyramidsField.inPyramids.get(card)) {
                    flash();
                    it.remove();
                    AbstractDungeon.player.hand.addToTop(card);
                }
            }

            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
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

        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck,
                COUNT, DESCRIPTIONS[1] + name + ".",
                false, false, false, false);
    }

    @Override
    public void update()
    {
        super.update();

        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == COUNT) {
            cardSelected = true;

            for (int i=0; i<COUNT; ++i) {
                cards.add(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
                PyramidsField.inPyramids.set(cards.get(i), true);
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();

            description = DESCRIPTIONS[2] + FontHelper.colorString(cards.get(0).name, "y")
                    + DESCRIPTIONS[3] + FontHelper.colorString(cards.get(1).name, "y") + DESCRIPTIONS[4];
            tips.clear();
            tips.add(new PowerTip(name, description));
            initializeTips();
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new MysteriousPyramids();
    }
}
