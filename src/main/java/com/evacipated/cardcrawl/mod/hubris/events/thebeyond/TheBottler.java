package com.evacipated.cardcrawl.mod.hubris.events.thebeyond;

import com.evacipated.cardcrawl.mod.hubris.relics.BottledRain;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.BottledFlame;
import com.megacrit.cardcrawl.relics.BottledLightning;
import com.megacrit.cardcrawl.relics.BottledTornado;

import java.util.ArrayList;
import java.util.List;

public class TheBottler extends AbstractImageEvent
{
    public static final String ID = "hubris:TheBottler";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static List<String> bottleIDs;

    private int bottleToReBottle = -1;

    static
    {
        bottleIDs = new ArrayList<>(4);
        addBottleRelic(BottledFlame.ID);
        addBottleRelic(BottledLightning.ID);
        addBottleRelic(BottledTornado.ID);
        addBottleRelic(BottledRain.ID);
    }

    public static void addBottleRelic(String id)
    {
        bottleIDs.add(id);
    }

    public static boolean canAppear()
    {
        for (String id : bottleIDs) {
            if (AbstractDungeon.player.hasRelic(id)) {
                return true;
            }
        }
        return false;
    }

    public TheBottler()
    {
        super(NAME, DESCRIPTIONS[0], null);
        for (String id : bottleIDs) {
            addBottleOption(id);
        }
        imageEventText.setDialogOption(OPTIONS[4]);
    }

    private void addBottleOption(String id)
    {
        if (AbstractDungeon.player.hasRelic(id)) {
            imageEventText.setDialogOption(FontHelper.colorString(OPTIONS[0] + RelicLibrary.getRelic(id).name + OPTIONS[1], "g"));
        } else {
            imageEventText.setDialogOption(OPTIONS[2] + RelicLibrary.getRelic(id).name + OPTIONS[3], true);
        }
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (screenNum) {
            case 0:
                if (buttonPressed == imageEventText.optionList.size()-1) {
                    openMap();
                } else {
                    bottleToReBottle = buttonPressed;
                    screenNum = 1;
                    imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    imageEventText.updateDialogOption(0, FontHelper.colorString(OPTIONS[5], "g"));
                    imageEventText.clearRemainingOptions();
                }
                break;
            case 1:
                AbstractRelic oldBottle = AbstractDungeon.player.getRelic(bottleIDs.get(bottleToReBottle));
                int relicIndex = AbstractDungeon.player.relics.indexOf(oldBottle);
                oldBottle.onUnequip();
                AbstractRelic newBottle = RelicLibrary.getRelic(bottleIDs.get(bottleToReBottle)).makeCopy();
                newBottle.instantObtain(AbstractDungeon.player, relicIndex, true);

                screenNum = 2;
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                imageEventText.clearRemainingOptions();
                break;
            case 2:
                switch (buttonPressed) {
                    default:
                        openMap();
                        break;
                }
                break;
        }
    }
}
