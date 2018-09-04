package com.evacipated.cardcrawl.mod.hubris.rooms;

import coloredmap.ColoredRoom;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.hubris.HubrisMod;
import com.evacipated.cardcrawl.mod.hubris.events.thebeyond.Nailsmith;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;

@ColoredRoom
public class NailsmithRoom extends AbstractRoom
{
    private EventRoom fakeRoom;

    public NailsmithRoom()
    {
        phase = RoomPhase.EVENT;
        mapSymbol = "NS";
        mapImg = ImageMaster.loadImage(HubrisMod.assetPath("images/ui/map/smith.png"));
        mapImgOutline = ImageMaster.loadImage(HubrisMod.assetPath("images/ui/map/smithOutline.png"));

        fakeRoom = new EventRoom();
    }

    @Override
    public void onPlayerEntry()
    {
        AbstractDungeon.overlayMenu.proceedButton.hide();
        event = fakeRoom.event = new Nailsmith();
        fakeRoom.event.onEnterRoom();
    }

    @Override
    public AbstractCard.CardRarity getCardRarity(int roll)
    {
        return fakeRoom.getCardRarity(roll);
    }

    @Override
    public void update()
    {
        fakeRoom.update();
    }

    @Override
    public void render(SpriteBatch sb)
    {
        fakeRoom.render(sb);
        fakeRoom.renderEventTexts(sb);
    }

    @Override
    public void renderAboveTopPanel(SpriteBatch sb)
    {
        fakeRoom.renderAboveTopPanel(sb);
    }
}
