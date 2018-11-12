package com.evacipated.cardcrawl.mod.hubris.relics;

import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.RareCodexAction;
import com.evacipated.cardcrawl.mod.hubris.actions.unique.ReverseEnlightenmentAction;
import com.evacipated.cardcrawl.mod.hubris.cards.colorless.Mulligan;
import com.evacipated.cardcrawl.mod.hubris.cards.curses.NaturalOne;
import com.evacipated.cardcrawl.mod.hubris.powers.EnergyGainPower;
import com.evacipated.cardcrawl.mod.hubris.powers.FixedDrawPower;
import com.evacipated.cardcrawl.mod.hubris.relics.abstracts.HubrisRelic;
import com.evacipated.cardcrawl.mod.hubris.vfx.combat.ShowRollResult;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EffectHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Omamori;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Icosahedron extends HubrisRelic implements ClickableRelic
{
    public static final String ID = "hubris:Icosahedron";

    public Icosahedron()
    {
        super(ID, "icosahedron.png", RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStartPreDraw()
    {
        if (counter != 1 && counter != 20) {
            beginLongPulse();
        }
    }

    @Override
    public void onVictory()
    {
        stopPulse();
        if (counter != 1 && counter != 20 && counter != -1) {
            setCounter(-1);
        }
    }

    @Override
    public String getUpdatedDescription()
    {
        switch (counter) {
            case -1:
                return CLICKABLE_DESCRIPTIONS()[0] + DESCRIPTIONS[0] + DESCRIPTIONS[1];
            case 1:
                return DESCRIPTIONS[3] + DESCRIPTIONS[2];
            case 20: {
                return DESCRIPTIONS[4] + DESCRIPTIONS[2];
            }
            default:
                return DESCRIPTIONS[1] + DESCRIPTIONS[5] + ShowRollResult.Roll.getDescription(counter);
        }
    }

    @Override
    public void setCounter(int c)
    {
        super.setCounter(c);
        description = getUpdatedDescription();
        tips.clear();
        tips.add(new PowerTip(name, description));
        initializeTips();
    }

    @Override
    public void update()
    {
        super.update();

        try {
            Field f = AbstractRelic.class.getDeclaredField("rotation");
            f.setAccessible(true);

            float rotation = f.getFloat(this);
            if (rotation > 0.0f) {
                rotation -= Gdx.graphics.getDeltaTime() * 540.0f;
                f.setFloat(this, rotation);
            } else if (rotation < 0) {
                f.setFloat(this, 0.0f);
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRightClick()
    {
        if (!isObtained || counter > 0) {
            return;
        }

        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            try {
                Field f = AbstractRelic.class.getDeclaredField("rotation");
                f.setAccessible(true);
                f.set(this, 359.0f);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }

            int roll = new Random().random(1, 20);
            doRoll(roll);
        }
    }

    private void doRoll(int roll)
    {
        stopPulse();
        flash();

        AbstractPlayer p = AbstractDungeon.player;
        List<AbstractGameAction> actions = new ArrayList<>();

        setCounter(roll);

        switch (roll) {
            case 20: // Natural Twenty.
                actions.add(new GainEnergyAction(1));
                AbstractDungeon.player.energy.energy += 1;
                AbstractDungeon.player.energy.energyMaster += 1;
                break;
            case 19: // Heal to full.
                actions.add(new HealAction(p, p, p.maxHealth));
                break;
            case 18: // At the start of each turn, Gain [E].
                actions.add(new ApplyPowerAction(p, p, new EnergyGainPower(p, 1), 1));
                break;
            case 17: // Choose 1 of 3 rare cards to add to your hand.
                actions.add(new RareCodexAction());
                break;
            case 16: // Gain a random rare potion.
                actions.add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, true)));
                break;
            case 15: // Draw 1 additional card each turn.
                actions.add(new ApplyPowerAction(p, p, new FixedDrawPower(p, 1), 1));
                break;
            case 14: // Gain 1 Strength, 1 Dexterity, and 1 Focus.
                actions.add(new ApplyPowerAction(p, p, new StrengthPower(p, 1), 1));
                actions.add(new ApplyPowerAction(p, p, new DexterityPower(p, 1), 1));
                actions.add(new ApplyPowerAction(p, p, new FocusPower(p, 1), 1));
                break;
            case 13: // Gain 3 Artifact.
                actions.add(new ApplyPowerAction(p, p, new ArtifactPower(p, 3), 3));
                break;
            case 12: // Gain a random common potion.
                actions.add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, true)));
                break;
            case 11: // Gain 100 Gold.
                AbstractDungeon.player.gainGold(100);
                EffectHelper.gainGold(p, this.currentX, this.currentY, 100);
                break;
            case 10: // Add a Mulligan to your hand.
                actions.add(new MakeTempCardInHandAction(new Mulligan()));
                break;
            case 9: // Lose 50 Gold.
                AbstractDungeon.player.loseGold(50);
                break;
            case 8: // Gain 5 Weak.
                actions.add(new ApplyPowerAction(p, p, new WeakPower(p, 5, false)));
                break;
            case 7: // Gain 5 Frail.
                actions.add(new ApplyPowerAction(p, p, new FrailPower(p, 5, false)));
                break;
            case 6: // Gain 5 Vulnerable.
                actions.add(new ApplyPowerAction(p, p, new VulnerablePower(p, 5, true)));
                break;
            case 5: // Increase the cost of cards in your hand by 1 this combat.
                actions.add(new ReverseEnlightenmentAction());
                break;
            case 4: // Draw 1 less card each turn.
                actions.add(new ApplyPowerAction(p, p, new FixedDrawPower(p, -1), -1));
                break;
            case 3: // Add 3 Void to your draw pile.
                actions.add(new MakeTempCardInDrawPileAction(new VoidCard(), 3, true, true));
                break;
            case 2: // Lose 10 HP.
                actions.add(new LoseHPAction(p, p, 10));
                break;
            case 1: // Natural One.
                if (!AbstractDungeon.player.hasRelic(Omamori.ID) || AbstractDungeon.player.getRelic(Omamori.ID).counter == 0) {
                    actions.add(new MakeTempCardInHandAction(new NaturalOne()));
                }
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(new NaturalOne(), Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f));
                break;
        }

        AbstractDungeon.topLevelEffects.add(new ShowRollResult(roll, actions));
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new Icosahedron();
    }
}
