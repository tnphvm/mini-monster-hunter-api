package me.tina.monsterhunter.repository;

import me.tina.monsterhunter.entity.Weapon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeaponRepository extends JpaRepository<Weapon, Integer> {
    @Query("SELECT wep from Weapon wep WHERE " +
            "LOWER(wep.weaponClass) = LOWER(:weaponClass) AND " +
            "wep.parentId = 0 AND " +
            "wep.name = '' AND " +
            "wep.attackPower = 0 AND " +
            "wep.rarity = 0")
    Weapon findRootWeapon(@Param("weaponClass") String weaponClass);

    @Query("SELECT wep from Weapon wep WHERE " +
            "LOWER(wep.weaponClass) = LOWER(:weaponClass) AND " +
            "wep.parentId = :parentId AND " +
            "LOWER(wep.name) = LOWER(:name) AND " +
            "wep.attackPower = :attackPower AND " +
            "wep.rarity = :rarity")
    Weapon findChildWeapon(@Param("weaponClass") String weaponClass,
                           @Param("name") String name,
                           @Param("attackPower") int attackPower,
                           @Param("rarity") int rarity,
                           @Param("parentId") int parentId);

    List<Weapon> findByParentId(int parentId);
}
