package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;
import com.shatteredpixel.shatteredpixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;

public abstract class Cooldown extends FlavourBuff{
    public int icon() { return BuffIndicator.TIME; }
    public float iconFadePercent() { return Math.max(0, visualcooldown() / duration()); }
    public abstract void tintIcon(Image icon);
    public  abstract int duration();
}
