/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier5;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.trinkets.ThirteenLeafClover;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Rapier;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class LeiMing extends MeleeWeapon {

	{
		image = ItemSpriteSheet.LEI_MING;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1.1f;

		tier = 5;
	}

	@Override
	public int min(int lvl) {
		return 1;
	}
	@Override
	public int max(int lvl) {
		return  40+8*lvl;
	}

	@Override
	public int damageRoll(Char owner) {
		int damage;
		if (owner instanceof Hero){
			if (Random.Float() < ThirteenLeafClover.alterHeroDamageChance()){
				damage=ThirteenLeafClover.alterDamageRoll(minDamage(owner), maxDamage(owner));
			} else {
				damage=Random.IntRange(minDamage(owner), maxDamage(owner));
			}
		}else {
			damage=damageRoll(owner);
		}
		return augment.damageFactor(damage);
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//伤害*150%
		Rapier.lungeAbility(hero, target, 1.5f, 0, this);
	}

	@Override
	public String abilityInfo() {
		Hero hero= Dungeon.hero;
		if (levelKnown){
			return Messages.get(this, "ability_desc", 1, augment.damageFactor(Math.round(maxDamage(hero)*1.5f)));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0), Math.round(max(0)*1.5f));
		}
	}

	public String upgradeAbilityStat(int level){
		return augment.damageFactor(min(level)) + "-" + augment.damageFactor(Math.round(max(level)*1.5f));
	}

}
