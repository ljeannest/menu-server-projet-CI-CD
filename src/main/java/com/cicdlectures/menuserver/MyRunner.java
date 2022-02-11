package com.cicdlectures.menuserver;

import java.util.Arrays;
import java.util.HashSet;

import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;
import com.cicdlectures.menuserver.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyRunner.class);
    
    @Autowired
    DishRepository dishRepository;

    @Autowired
    MenuRepository menuRepository;

    @Override
    public void run(String... args) throws Exception {
        menuRepository.deleteAll();

        menuRepository.save(new Menu((long) 1, "Christmas menu", new HashSet<Dish>(
            Arrays.asList(
                new Dish(Long.valueOf(2), "Eru", null),
                new Dish(Long.valueOf(3), "Ndole", null)))));

        menuRepository.save(new Menu((long) 1, "Christmas menu", new HashSet<Dish>(
            Arrays.asList(
                new Dish(Long.valueOf(2), "Ndole", null),
                new Dish(Long.valueOf(3), "Tarte au chocolat", null)))));   
        
        menuRepository.save(new Menu((long) 1, "Christmas menu", new HashSet<Dish>(
            Arrays.asList(
                new Dish(Long.valueOf(2), "Tarte au chocolat", null),
                new Dish(Long.valueOf(3), "POulet DG", null)))));
        
        menuRepository.findAll().forEach((menu)-> {
            logger.info("{}", menu);
        });        
    }
}
