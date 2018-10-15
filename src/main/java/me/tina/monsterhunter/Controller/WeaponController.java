package me.tina.monsterhunter.Controller;

import me.tina.monsterhunter.entity.Weapon;
import me.tina.monsterhunter.repository.WeaponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@RestController
public class WeaponController {
    @Autowired
    WeaponRepository weaponRepository;

    @RequestMapping("/")
    public String index() {
        String welcomeMsg = "Welcome to my mini Monster Hunter API! :D \n\n" +
                "Here you will find a small database where you can create and store the weapons you find in the game." +
                "You can also keep track of a particular weapon tree that you're planning or are working on.";

        return welcomeMsg;
    }

    @GetMapping("/weapons/all")
    public List<Weapon> getAllWeapons() {
        return weaponRepository.findAll();
    }

    // To add root level nodes, request user to provide '0' as id
    @PostMapping("/weapons/{parentId}")
    public List<Weapon> createWeapons(@PathVariable("parentId") String id, @RequestBody List<Map<String, String>> body) {
        if (Integer.parseInt(id) < 0 || id.isEmpty()) {
            // TODO: error message - no entity found
            return new ArrayList<Weapon>();
        }

        int parentId = Integer.parseInt(id);
        List<Weapon> successfulEntries = new ArrayList<Weapon>();
        HashSet<Map<String, String>> uniqueEntries = new HashSet<Map<String, String>>(body);

        if (parentId == 0) {
            for (Map<String, String> entry : uniqueEntries) {
                String weaponClass = entry.get("weaponClass");

                if (weaponClass != null) {
                    if (weaponRepository.findRootWeapon(weaponClass) != null) {
                        // TODO: error message - found dupe from previous call
                        return new ArrayList<Weapon>();
                    }
                    else {
                        Weapon wepEntry = new Weapon(weaponClass, parentId);
                        successfulEntries.add(wepEntry);
                        weaponRepository.save(wepEntry);
                    }
                }
            }
        }
        else {
            Weapon parentWep = (weaponRepository.findById(parentId).isPresent()) ? weaponRepository.findById(parentId).get() : null;
            if (parentWep == null) {
                // TODO: error msg - entity not found; parent dne
                return new ArrayList<Weapon>();
            }

            for (Map<String, String> entry : body) {
                String weaponClass = entry.get("weaponClass");
                String name = entry.get("name");
                String attackPower = entry.get("attackPower");
                String rarity = entry.get("rarity");

                if (StringUtils.isBlank(weaponClass) || StringUtils.isBlank(name)) {
                    // TODO: error message - invalid param provided
                    return new ArrayList<Weapon>();
                }

                String parentClass = parentWep.getWeaponClass().toLowerCase();
                if (!weaponClass.toLowerCase().equals(parentClass)) {
                    // TODO: error message - invalid param provided
                    return new ArrayList<Weapon>();
                }

                if (weaponRepository.findChildWeapon(weaponClass, name,
                        Integer.parseInt(attackPower), Integer.parseInt(rarity), parentId) != null) {
                    // TODO: error message - dupe found from previous call
                    return new ArrayList<Weapon>();
                }
                else {
                    Weapon wepEntry = new Weapon(weaponClass, name, Integer.parseInt(attackPower), Integer.parseInt(rarity), parentId);
                    successfulEntries.add(wepEntry);
                    weaponRepository.save(wepEntry);
                }
            }
        }

        return successfulEntries;
    }

    // Must provide parentId, if no parentId --> retrieve heirarchy at the root which is getting everything
    // Thus, user should use /weapons/all request
    // or user could provide 0, and we make a findAll() call but that defeats the purpose for the /weapons/all availability
    // 1. get rid of /weapons/all so this call does everything
    // 2. do above mentioned
    // 3. provide both
    @GetMapping("/weapons/{parentId}")
    public List<Weapon> getWeapons(@PathVariable("parentId") String id) {
        if (Integer.parseInt(id) < 0 || id.isEmpty()) {
            // TODO: error message - entity not found
            return new ArrayList<Weapon>();
        }

        int parentId = Integer.parseInt(id);
        List<Weapon> foundChildren = new ArrayList<Weapon>();
        Weapon parentWep = (weaponRepository.findById(parentId).isPresent()) ? weaponRepository.findById(parentId).get() : null;

        if (parentWep == null) {
            // TODO: err msg - entity not found
            return new ArrayList<Weapon>();
        }

        foundChildren.add(parentWep);

        List<Integer> descendentsId = new ArrayList<Integer>();
        List<Weapon> children = weaponRepository.findByParentId(parentId);

        for (Weapon child : children) {
            descendentsId.add(child.getId());
            foundChildren.add(child);
        }

        // TODO: Potential duplicate code from removeWeapons -- refactor to helper function if possible
        for (int i = 0; i < descendentsId.size(); i++) {
            int currChild = descendentsId.get(i);
            children = weaponRepository.findByParentId(currChild);

            for (Weapon child : children) {
                descendentsId.add(child.getId());
                foundChildren.add(child);
            }
        }

        return foundChildren;
    }

    @DeleteMapping("/weapons/{id}")
    public List<Weapon> removeWeapons(@PathVariable String id) {
        if (Integer.parseInt(id) < 0 || id.isEmpty()) {
            // TODO: error message - entity not found
            return new ArrayList<Weapon>();
        }

        int parentId = Integer.parseInt(id);
        List<Weapon> removedChildren = new ArrayList<Weapon>();
        Weapon parentWep = (weaponRepository.findById(parentId).isPresent()) ? weaponRepository.findById(parentId).get() : null;

        if (parentWep == null) {
            // TODO: err msg - entity not found
            return new ArrayList<Weapon>();
        }

        removedChildren.add(parentWep);
        weaponRepository.delete(parentWep);

        List<Integer> descendentsId = new ArrayList<Integer>();
        List<Weapon> children = weaponRepository.findByParentId(parentId);

        for (Weapon child : children) {
            descendentsId.add(child.getId());
            removedChildren.add(child);
            weaponRepository.delete(child);
        }

        for (int i = 0; i < descendentsId.size(); i++) {
            int currChild = descendentsId.get(i);
            children = weaponRepository.findByParentId(currChild);

            for (Weapon child : children) {
                descendentsId.add(child.getId());
                removedChildren.add(child);
                weaponRepository.delete(child);
            }
        }

        return removedChildren;
    }
}
