package com.evacipated.cardcrawl.mod.hubris.fakes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.shop.Merchant;

public class FakeMerchant extends Merchant
{
    public FakeMerchant(Merchant npc)
    {
        anim = npc.anim;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void render(SpriteBatch sb)
    {
    }
}
