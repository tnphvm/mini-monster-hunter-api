package me.tina.monsterhunter.Controller;

import me.tina.monsterhunter.entity.Weapon;
import me.tina.monsterhunter.repository.WeaponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            // TODO: return meaningful error
            return new ArrayList<Weapon>();
        }

        int parentId = Integer.parseInt(id);
        List<Weapon> successfulEntries = new ArrayList<Weapon>();

        if (parentId == 0) {
            for (Map<String, String> entry : body) {
                String weaponClass = entry.get("weaponClass");

                if (weaponClass != null) {
                    Weapon wepEntry = new Weapon(weaponClass, parentId);
                    successfulEntries.add(wepEntry);
                    weaponRepository.save(wepEntry);
                }
                // TODO: check for dups
                // TODO: return error if field not found - 1. continue through list and add all correct ones & return bad entries, 2. halt entirely and return error
            }
        }
        else {
            for (Map<String, String> entry : body) {
                String weaponClass = entry.get("weaponClass");
                String name = entry.get("name");
                String attackPower = entry.get("attackPower");
                String rarity = entry.get("rarity");

                // TODO: More technical based on data validation -- entry's weapon class should match parent weapon class
                // TODO: return error if field not found - 1. continue through list and add all correct ones & return bad entries, 2. halt entirely and return error

                if (weaponClass != null && name != null && attackPower != null && rarity != null) {
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
            // TODO: return meaningful error
            return new ArrayList<Weapon>();
        }
    }
//
//    @DeleteMapping("/weapons/{id}")
//    public boolean removeWeapons(@PathVariable String id) {
//
//    }
}
