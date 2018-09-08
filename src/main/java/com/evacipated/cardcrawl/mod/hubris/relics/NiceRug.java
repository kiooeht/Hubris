package com.evacipated.cardcrawl.mod.hubris.relics;

import com.evacipated.cardcrawl.mod.hubris.fakes.FakeMerchant;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

public class NiceRug extends HubrisRelic
{
    public static final String ID = "hubris:NiceRug";
    private ShopRoom shopRoom;

    public NiceRug()
    {
        super(ID, "test4.png", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }

    @Override
    public void onEnterRoom(AbstractRoom room)
    {
        if (room instanceof ShopRoom) {
            shopRoom = (ShopRoom) room;
        }
    }

    @Override
    public void update()
    {
        super.update();

        if (shopRoom != null && shopRoom.merchant != null) {
            shopRoom.merchant = new FakeMerchant(shopRoom.merchant);
            shopRoom = null;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new NiceRug();
    }
}
