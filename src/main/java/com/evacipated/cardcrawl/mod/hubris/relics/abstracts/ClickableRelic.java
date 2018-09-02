package com.evacipated.cardcrawl.mod.hubris.relics.abstracts;

import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface ClickableRelic
{
    class Data
    {
        private boolean rightClickStart = false;
    }

    Data state = new Data();

    void onRightClick();

    default void clickUpdate()
    {
        if (state.rightClickStart && InputHelper.justReleasedClickRight) {
            state.rightClickStart = false;
            if (hovered()) {
                onRightClick();
            }
        } else if (InputHelper.justClickedRight && hovered()) {
            state.rightClickStart = true;
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
