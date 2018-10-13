package me.tina.monsterhunter.entity;

import java.util.logging.Logger;

import javax.persistence.*;

@Entity
public class Weapon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String weaponClass;
    private String name;
    private int attackPower;
    private int rarity;
    private int parentId;

    private final static Logger LOGGER = Logger.getLogger(Weapon.class.getName());
    private final static int DEFAULT_ID = 0;
    private final static String DEFAULT_WEAPON_CLASS = "";
    private final static String DEFAULT_NAME = "";
    private final static int DEFAULT_ATTACK_POWER = 0;
    private final static int DEFAULT_RARITY = 0;
    private final static int DEFAULT_PARENT_ID = 0;

    public Weapon() {}

    public Weapon(String weaponClass, String name, int attackPower, int rarity, int parentId) {
        this.setWeaponClass(weaponClass);
        this.setName(name);
        this.setAttackPower(attackPower);
        this.setRarity(rarity);
        this.setParentId(parentId);
    }

    public Weapon(String weaponClass, int parentId) {
        this.setWeaponClass(weaponClass);
        this.setParentId(parentId);

        this.name = DEFAULT_NAME;
        this.attackPower = DEFAULT_ATTACK_POWER;
        this.rarity = DEFAULT_RARITY;
    }

    public int getId() { return this.id; }

    public void setId(int id) {
        if (id < 0) {
            LOGGER.warning("Weapon id cannot be negative.");
            this.id = DEFAULT_ID;
        }

        this.id = id;
    }

    public String getName() { return this.name; }

    public void setName(String name) {
        if (name == null) {
            LOGGER.warning("Weapon name cannot be null.");
            this.name = DEFAULT_NAME;
        }

        this.name = name;
    }

    public String getWeaponClass() { return this.weaponClass; }

    public void setWeaponClass(String weaponClass) {
        if (weaponClass == null) {
            LOGGER.warning("Weapon class cannot be null.");
            this.weaponClass = DEFAULT_WEAPON_CLASS;
        }

        this.weaponClass = weaponClass;
    }

    public int getAttackPower() { return this.attackPower; }

    public void setAttackPower(int attackPower) {
        if (attackPower < 0) {
            LOGGER.warning("Weapon attack power cannot be negative.");
            this.attackPower = DEFAULT_ATTACK_POWER;
        }

        this.attackPower = attackPower;
    }

    public int getRarity() { return this.rarity; }

    public void setRarity(int rarity) {
        if (rarity < 0) {
            this.rarity = DEFAULT_RARITY;
        }

        this.rarity = rarity;
    }

    public int getParentId() { return this.parentId; }

    public void setParentId(int parentId) {
        if (parentId < 0) {
            LOGGER.warning("Weapon parent id cannot be negative.");
            this.parentId = DEFAULT_PARENT_ID;
        }

        this.parentId = parentId;
    }

    @Override
    public String toString() {
        String formattedWeapon = "Weapon{" +
                "id=" + id + "," +
                "weapon_class=" + weaponClass + ", " +
                "name=" + name + ", " + ", " +
                "attack_power=" + attackPower + ", " +
                "rarity=" + rarity + ", " +
                "parent_id=" + parentId + "}";

        return formattedWeapon;
    }
}
