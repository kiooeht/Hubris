package com.evacipated.cardcrawl.mod.hubris.events.thecity;

import com.evacipated.cardcrawl.mod.hubris.relics.BottledHeart;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Circlet;

public class Experiment extends AbstractImageEvent
{
    public static final String ID = "hubris:Experiment";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private int lostHP = 0;

    public Experiment()
    {
        super(NAME, DESCRIPTIONS[0], null);
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (screenNum) {
            case 0: // Beginning: Agree or Leave
                switch (buttonPressed) {
                    case 0:
                        screenNum = 2;
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[2] + (AbstractDungeon.player.currentHealth - 1) + OPTIONS[3]);
                        imageEventText.updateDialogOption(1, OPTIONS[4]);
                        break;
                    case 1:
                        screenNum = 1;
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[1]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1: // Leave/Flee/etc.
                switch (buttonPressed) {
                    default:
                        openMap();
                        break;
                }
                break;
            case 2: // First agree: Lose all but 1 HP or Flee
                switch (buttonPressed) {
                    case 0:
                        lostHP = AbstractDungeon.player.currentHealth - 1;
                        AbstractDungeon.player.damage(new DamageInfo(null, AbstractDungeon.player.currentHealth - 1));
                        screenNum = 3;
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        imageEventText.updateDialogOption(0, OPTIONS[5] + (AbstractDungeon.player.maxHealth - 1) + OPTIONS[6]);
                        imageEventText.updateDialogOption(1, OPTIONS[7] + (lostHP / 4) + OPTIONS[8]);
                        break;
                    case 1:
                        screenNum = 1;
                        imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        imageEventText.updateDialogOption(0, OPTIONS[1]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 3: // Continue (Lose HP): Lose Max HP or Attack
                switch (buttonPressed) {
                    case 0:
                        if (AbstractDungeon.player.hasRelic(BottledHeart.ID)) {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f,
                                    RelicLibrary.getRelic(Circlet.ID).makeCopy());
                        } else {
                            AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f,
                                    RelicLibrary.getRelic(BottledHeart.ID).makeCopy());
                        }
                        screenNum = 1;
                        imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        imageEventText.updateDialogOption(0, OPTIONS[1]);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 1:
                        AbstractDungeon.player.heal(lostHP / 4);
                        screenNum = 1;
                        imageEventText.updateBodyText(DESCRIPTIONS[6]);
                        imageEventText.updateDialogOption(0, OPTIONS[1]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
        }
    }
}
