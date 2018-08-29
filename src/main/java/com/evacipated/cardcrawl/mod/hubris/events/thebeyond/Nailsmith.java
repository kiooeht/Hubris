package com.evacipated.cardcrawl.mod.hubris.events.thebeyond;

import com.evacipated.cardcrawl.mod.hubris.relics.OldNail;
import com.evacipated.cardcrawl.mod.hubris.relics.PureNail;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class Nailsmith extends AbstractImageEvent
{
    public static final String ID = "hubris:Nailsmith";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IGNORE = DESCRIPTIONS[3];

    private static final int GOLD_COST = 100;

    private CurrentScreen curScreen = CurrentScreen.MAIN;
    private OptionChosen option = OptionChosen.NONE;

    private CardGroup upgradedCards;

    private enum CurrentScreen
    {
        MAIN, DONE
    }

    private enum OptionChosen
    {
        NONE, UPGRADE, NAIL
    }

    public Nailsmith()
    {
        super(NAME, DESCRIPTIONS[0], "images/events/blacksmith.jpg");

        upgradedCards = getUpgradedCards(AbstractDungeon.player.masterDeck);
        String oldNailName = new OldNail().name;
        if (AbstractDungeon.player.hasRelic(OldNail.ID) && AbstractDungeon.player.gold >= GOLD_COST && upgradedCards.size() >= 2) {
            imageEventText.setDialogOption(OPTIONS[4] + GOLD_COST + OPTIONS[5] + FontHelper.colorString(oldNailName, "g") + OPTIONS[6]);
        } else {
            imageEventText.setDialogOption(OPTIONS[7] + GOLD_COST + OPTIONS[8], true);
        }
        if (AbstractDungeon.player.masterDeck.hasUpgradableCards()) {
            imageEventText.setDialogOption(OPTIONS[0]);
        } else {
            imageEventText.setDialogOption(OPTIONS[1], true);
        }
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    public void update()
    {
        super.update();

        if (!AbstractDungeon.isScreenUp && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            switch (option) {
                case UPGRADE:
                    AbstractDungeon.gridSelectScreen.selectedCards.get(0).upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(AbstractDungeon.gridSelectScreen.selectedCards.get(0));
                    AbstractDungeon.effectsQueue.add(new ShowCardBrieflyEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeStatEquivalentCopy()));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    break;
                case NAIL:
                    AbstractCard card1 = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                    AbstractCard card2 = AbstractDungeon.gridSelectScreen.selectedCards.get(1);
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();

                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card1, Settings.WIDTH / 4.0F, Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card2, Settings.WIDTH / 4.0f * 3.0f, Settings.HEIGHT / 2.0f));

                    AbstractDungeon.player.masterDeck.removeCard(card1);
                    AbstractDungeon.player.masterDeck.removeCard(card2);

                    if (AbstractDungeon.player.hasRelic(PureNail.ID)) {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f,
                                RelicLibrary.getRelic(Circlet.ID).makeCopy());
                    } else {
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f,
                                RelicLibrary.getRelic(PureNail.ID).makeCopy());
                    }
                    break;
            }
            option = OptionChosen.NONE;
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (curScreen) {
            case MAIN:
                switch (buttonPressed) {
                    case 0:
                        curScreen = CurrentScreen.DONE;
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        option = OptionChosen.NAIL;
                        AbstractDungeon.player.loseGold(GOLD_COST);
                        AbstractDungeon.gridSelectScreen.open(upgradedCards,
                                2, OPTIONS[9], false, false, false, true);
                        break;
                    case 1:
                        curScreen = CurrentScreen.DONE;
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        option = OptionChosen.UPGRADE;
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(),
                                1, OPTIONS[3], true, false, false, false);
                        break;
                    case 2:
                        curScreen = CurrentScreen.DONE;
                        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
                        imageEventText.updateBodyText(IGNORE);
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case DONE:
                openMap();
                break;
        }
    }

    private static CardGroup getUpgradedCards(CardGroup group)
    {
        CardGroup ret = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard c : group.group) {
            if (c.upgraded) {
                ret.group.add(c);
            }
        }
        return ret;
    }
}