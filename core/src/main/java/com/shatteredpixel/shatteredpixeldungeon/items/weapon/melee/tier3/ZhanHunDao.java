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

package com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.tier3;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.enchantments.Grim;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.MeleeWeapon;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Sword;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
public class ZhanHunDao extends MeleeWeapon {

	{
		image = ItemSpriteSheet.ZHAN_HUN_DAO;
		hitSound = Assets.Sounds.HIT_SLASH;
		hitSoundPitch = 1f;

		tier = 3;
	}

	@Override
	public int min(int lvl) {
		return 1;
	}
	@Override
	public int max(int lvl) {
		return 20+4*lvl;
	}

	@Override
	public int proc(Char attacker, Char defender, int damage) {
		if (defender.isImmune(Grim.class)) {
			return damage;
		}
		int level = Math.max( 0, buffedLvl() );

		float maxChance = 0.5f + .05f*level;
		maxChance*=100;

		//we defer logic using an actor here so we can know the true final damage
		//see Char.damage
		Buff.affect(defender, Grim.GrimTracker.class).maxChance = maxChance;
		if (defender.buff(Grim.GrimTracker.class) != null
				&& attacker instanceof Hero){
			defender.buff(Grim.GrimTracker.class).qualifiesForBadge = true;
		}
		return super.proc(attacker, defender, damage);
	}

	@Override
	protected int baseChargeUse(Hero hero, Char target){
		if (hero.buff(Sword.CleaveTracker.class) != null){
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public String targetingPrompt() {
		return Messages.get(this, "prompt");
	}

	@Override
	protected void duelistAbility(Hero hero, Integer target) {
		//+(7+lvl) damage, roughly +40% base dmg, +30% scaling
		int dmgBoost = augment.damageFactor( damageBoost(5+ buffedLvl()+hero.weaponMastery) );
		Sword.cleaveAbility(hero, target, 1, dmgBoost, this);
	}

	@Override
	public String abilityInfo() {
		Hero hero=Dungeon.hero;
		int dmgBoost = damageBoost( 5 + buffedLvl() +hero.weaponMastery) ;
		if (levelKnown){
			return Messages.get(this, "ability_desc", augment.damageFactor(minDamage(hero)+dmgBoost), augment.damageFactor(maxDamage(hero)+dmgBoost));
		} else {
			return Messages.get(this, "typical_ability_desc", min(0)+5, max(0)+5);
		}
	}

	public String upgradeAbilityStat(int level){
		int dmgBoost = 7 + level;
		return augment.damageFactor(min(level)+dmgBoost) + "-" + augment.damageFactor(max(level)+dmgBoost);
	}

}
