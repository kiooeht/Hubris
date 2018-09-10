package com.evacipated.cardcrawl.mod.hubris.relics.abstracts;

import com.evacipated.cardcrawl.mod.hubris.patches.HitboxRightClick;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface ClickableRelic
{
    void onRightClick();

    default void clickUpdate()
    {
        if (this instanceof AbstractRelic) {
            AbstractRelic relic = (AbstractRelic) this;
            if (HitboxRightClick.rightClicked.get(relic.hb)) {
                onRightClick();
            }
        } else {
            throw new NotImplementedException();
        }
    }

    default boolean hovered()
    {
        if (this instanceof AbstractRelic) {
            AbstractRelic relic = (AbstractRelic) this;
            return relic.hb.hovered;
        }
        throw new NotImplementedException();
    }
}
