package com.evacipated.cardcrawl.mod.hubris.events.shrines;

import com.evacipated.cardcrawl.mod.hubris.relics.Icosahedron;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Circlet;

public class TheFatedDie extends AbstractImageEvent
{
    public static final String ID = "hubris:TheFatedDie";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    public TheFatedDie()
    {
        super(NAME, DESCRIPTIONS[0], null);
        imageEventText.setDialogOption(OPTIONS[0] + FontHelper.colorString(new Icosahedron().name, "g") + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (screenNum) {
            case 0:
                switch (buttonPressed) {
                    case 0:
                        if (AbstractDungeon.player.hasRelic(Icosahedron.ID)) {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2,
                                    RelicLibrary.getRelic(Circlet.ID).makeCopy());
                        } else {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2,
                                    RelicLibrary.getRelic(Icosahedron.ID).makeCopy());
                        }
                        screenNum = 1;
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        break;
                     case 1:
                        screenNum = 1;
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                switch (buttonPressed) {
                    default:
                        openMap();
                        break;
                }
                break;
        }
    }
}
