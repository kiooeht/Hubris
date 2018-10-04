package com.evacipated.cardcrawl.mod.hubris.relics;

import basemod.DevConsole;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.screens.select.RelicSelectScreen;
import com.evacipated.cardcrawl.mod.hubris.vfx.ObtainRelicLater;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.util.ArrayList;

public class Backtick extends HubrisRelic
{
    public static final String ID = "hubris:Backtick";
    private boolean relicSelected = true;
    private RelicSelectScreen relicSelectScreen;
    private boolean fakeHover = false;

    public Backtick()
    {
        super(ID, "backtick.png", RelicTier.BOSS, LandingSound.CLINK);

        RelicStrings newRelicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
        flavorText = Input.Keys.toString(DevConsole.toggleKey) + newRelicStrings.FLAVOR + Test447.ID;
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEquip()
    {
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;

        openRelicSelect();
    }

    private void openRelicSelect()
    {
        relicSelected = false;

        ArrayList<AbstractRelic> relics = new ArrayList<>();
        relics.addAll(RelicLibrary.starterList);
        relics.addAll(RelicLibrary.commonList);
        relics.addAll(RelicLibrary.uncommonList);
        relics.addAll(RelicLibrary.rareList);
        relics.addAll(RelicLibrary.shopList);

        relics.removeIf(r ->
                !UnlockTracker.isRelicSeen(r.relicId)
                || UnlockTracker.isRelicLocked(r.relicId)
                || AbstractDungeon.player.hasRelic(r.relicId));

        relicSelectScreen = new RelicSelectScreen();
        relicSelectScreen.open(relics);
    }

    @Override
    public void update()
    {
        super.update();

        if (!relicSelected) {
            if (relicSelectScreen.doneSelecting()) {
                relicSelected = true;

                AbstractRelic relic = relicSelectScreen.getSelectedRelics().get(0).makeCopy();
                switch (relic.tier) {
                    case COMMON:
                        AbstractDungeon.commonRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case UNCOMMON:
                        AbstractDungeon.uncommonRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case RARE:
                        AbstractDungeon.rareRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case SHOP:
                        AbstractDungeon.shopRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                    case BOSS:
                        AbstractDungeon.bossRelicPool.removeIf(id ->  id.equals(relic.relicId));
                        break;
                }
                AbstractDungeon.effectsQueue.add(0, new ObtainRelicLater(relic));

                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            } else {
                relicSelectScreen.update();
                if (!hb.hovered) {
                    fakeHover = true;
                }
                hb.hovered = true;
            }
        }
    }

    @Override
    public void renderTip(SpriteBatch sb)
    {
        if (!relicSelected && fakeHover) {
            relicSelectScreen.render(sb);
        }
        if (fakeHover) {
            fakeHover = false;
            hb.hovered = false;
        } else {
            super.renderTip(sb);
        }
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb)
    {
        super.renderInTopPanel(sb);

        if (!relicSelected && !fakeHover) {
            relicSelectScreen.render(sb);
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Backtick();
    }
}
