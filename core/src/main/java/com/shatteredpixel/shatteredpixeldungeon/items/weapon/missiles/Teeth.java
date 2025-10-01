package com.shatteredpixel.shatteredpixeldungeon.items.weapon.missiles;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;

public class Teeth extends MissileWeapon {

    {
        image = ItemSpriteSheet.TEETH;
        hitSound = Assets.Sounds.HIT_STAB;
        hitSoundPitch = 1.1f;
        tier = 1;
        bones = false;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
}