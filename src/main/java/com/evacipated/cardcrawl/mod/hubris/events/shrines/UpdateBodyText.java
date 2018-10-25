package com.evacipated.cardcrawl.mod.hubris.events.shrines;

import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.FrozenEgg2;
import com.megacrit.cardcrawl.relics.MoltenEgg2;
import com.megacrit.cardcrawl.relics.ToxicEgg2;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class UpdateBodyText extends AbstractImageEvent
{
    public static final String ID = "hubris:UPDATEBODYTEXT";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private CurrentScreen curScreen = CurrentScreen.INTRO;
    private OptionChosen option = OptionChosen.NONE;

    private enum CurrentScreen
    {
        INTRO, MAIN, DONE
    }

    private enum OptionChosen
    {
        NONE, UPGRADE, GAIN
    }

    public UpdateBodyText()
    {
        super(NAME, DESCRIPTIONS[0], HubrisMod.assetPath("images/events/updatebodytext1.jpg"));

        imageEventText.setDialogOption(OPTIONS[0]);
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
                case GAIN:
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(AbstractDungeon.gridSelectScreen.selectedCards.get(0).makeCopy(),
                            Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                    AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    break;
            }
            option = OptionChosen.NONE;
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (curScreen) {
            case INTRO:
                imageEventText.loadImage(HubrisMod.assetPath("images/events/updatebodytext2.jpg"));
                imageEventText.updateBodyText(DESCRIPTIONS[1]);
                imageEventText.updateDialogOption(0, OPTIONS[1], !AbstractDungeon.player.masterDeck.hasUpgradableCards());
                imageEventText.setDialogOption(OPTIONS[2]);
                imageEventText.setDialogOption(OPTIONS[3]);

                curScreen = CurrentScreen.MAIN;
                break;
            case MAIN:
                switch (buttonPressed) {
                    case 0:
                        option = OptionChosen.UPGRADE;
                        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getUpgradableCards(), 1, OPTIONS[5], true, false, false, false);
                        break;
                    case 1:
                        AbstractDungeon.player.increaseMaxHp(3, false);
                        break;
                    case 2:
                        option = OptionChosen.GAIN;
                        CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                        AbstractCard card = null;
                        for (int i=0; i<10; ++i) {
                            card = AbstractDungeon.getCard(AbstractDungeon.rollRarity());
                            if (!group.contains(card)) {
                                if (card.type == AbstractCard.CardType.ATTACK && AbstractDungeon.player.hasRelic(MoltenEgg2.ID)) {
                                    card.upgrade();
                                } else if (card.type == AbstractCard.CardType.SKILL && AbstractDungeon.player.hasRelic(ToxicEgg2.ID)) {
                                    card.upgrade();
                                } else if (card.type == AbstractCard.CardType.POWER && AbstractDungeon.player.hasRelic(FrozenEgg2.ID)) {
                                    card.upgrade();
                                }
                                group.addToBottom(card);
                                UnlockTracker.markCardAsSeen(card.cardID);
                            } else {
                                --i;
                            }
                        }

                        AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[6], false);
                        break;
                }
                StringBuilder tmp = new StringBuilder();
                for (int i=0; i<200; ++i) {
                    tmp.append(randColor(DESCRIPTIONS[2]));
                }
                imageEventText.updateBodyText(tmp.toString());
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[4]);
                curScreen = CurrentScreen.DONE;
                break;
            case DONE:
                try {
                    HubrisMod.otherSaveData.setBool("UPDATEBODYTEXT", true);
                    HubrisMod.otherSaveData.save();
                } catch (IOException ignore) {
                }
                openMap();
                break;
        }
    }

    private static String[] colors = {"efc851", "7fff00", "ff6563", "87ceeb", Settings.PURPLE_COLOR.toString()};
    private static String randColor(String input)
    {
        StringBuilder retVal = new StringBuilder();
        Scanner s = new Scanner(input);

        while (s.hasNext()) {
            retVal.append("[#").append(colors[new Random().nextInt(colors.length)]).append("]").append(s.next());
            retVal.append("[] ");
        }

        s.close();
        return retVal.toString();
    }
}