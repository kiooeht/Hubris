package com.evacipated.cardcrawl.mod.hubris.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class AnimationTimeScaleAction extends AbstractGameAction
{
    private float timeScale;

    public AnimationTimeScaleAction(AbstractCreature target, float timeScale)
    {
        this.target = target;
        this.timeScale = timeScale;
    }

    @Override
    public void update()
    {
        target.state.setTimeScale(timeScale);
        isDone = true;
    }
}
