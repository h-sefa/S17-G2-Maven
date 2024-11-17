package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers;

    private Taxable taxable;


    @Autowired
    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

    @PostConstruct
    public void init(){
        developers = new HashMap<>();
        developers.put(22, new JuniorDeveloper(22,"Junior1",3000d));
        developers.put(33, new SeniorDeveloper(33,"Senior1",7000d));
        developers.put(44, new MidDeveloper(44,"Mid1",5000d));

    }

    @GetMapping
    public List<Developer> getDevelopers(){
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDevelopers(@PathVariable int id){
        return developers.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Developer createDeveloper(@RequestBody Developer developer){

        if(developer.getExperience() == Experience.JUNIOR){
            developers.put(developer.getId(),
                    new JuniorDeveloper(developer.getId(),
                                        developer.getName(),
                                  developer.getSalary()*(100-taxable.getSimpleTaxRate())/100));
        }
        else if(developer.getExperience() == Experience.MID){
            developers.put(developer.getId(),
                    new MidDeveloper(developer.getId(),
                            developer.getName(),
                            developer.getSalary()*(100-taxable.getMiddleTaxRate())/100));
        }
        else if(developer.getExperience() == Experience.SENIOR){
            developers.put(developer.getId(),
                    new SeniorDeveloper(developer.getId(),
                            developer.getName(),
                            developer.getSalary()*(100-taxable.getUpperTaxRate())/100));
        }
        else{
            return null;
        }
        return developers.get(developer.getId());

    }

    @PutMapping("/{id}")
    public Developer updateDeveloper(@PathVariable int id, @RequestBody Developer developer){
        return developers.replace(id, developer);
    }

    @DeleteMapping("/{id}")
    public List<Developer> deleteDeveloper(@PathVariable int id){
        developers.remove(id);
        return developers.values().stream().toList();
    }
}
