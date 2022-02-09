package com.cicdlectures.menuserver.controller;

import static org.assertj.core.api.Assertions.*;

import java.net.URL;
import java.util.List;
import java.util.HashSet;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cicdlectures.menuserver.repository.DishRepository;
import com.cicdlectures.menuserver.repository.MenuRepository;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.dto.DishDto;


// Lance l'application sur un port aléatoire.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Indique de relancer l'application à chaque test.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MenuControllerIT {

    @LocalServerPort
    private int port;

    private URL url;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private TestRestTemplate template;

    private final List<Menu> existingMenus = Arrays.asList(
        new Menu(null, "Christmas menu", new HashSet<>(Arrays.asList(
            new Dish(null, "Ndole", null), 
            new Dish(null, "Eru", null)))),
        new Menu(null, "Wedding menu", new HashSet<>(Arrays.asList(
            new Dish(null, "Poulet DG", null), 
            new Dish(null, "Tarte au chocolat", null)))));

    @BeforeEach
    public void setUp() throws Exception {
        url = new URL("http://localhost:" + port + "/menus");
    }

    @BeforeEach
    public void initDataset() {
        for (Menu menu : existingMenus) {
            menuRepository.save(menu);
        }
    }

    @Test
    public void listsExistingMenus() throws Exception {
        MenuDto[] wantMenus = {
            new MenuDto(Long.valueOf(1), "Christmas menu",
                new HashSet<DishDto>(
                    Arrays.asList(
                        new DishDto(Long.valueOf(1), "Ndole"), 
                        new DishDto(Long.valueOf(2), "Eru")))),
            new MenuDto(Long.valueOf(2), "Wedding menu", new HashSet<DishDto>(
                    Arrays.asList(
                    new DishDto(Long.valueOf(3), "Poulet DG"), 
                    new DishDto(Long.valueOf(4), "Tarte au chocolat")))) };
                    
                    ResponseEntity<MenuDto[]> response = this.template.getForEntity(url.toString(), MenuDto[].class);
                    MenuDto[] gotMenus = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(wantMenus, gotMenus);
    }

    @Test
    public void createsNewMenu() throws Exception {
        MenuDto wantMenu = new MenuDto(Long.valueOf(3), "Dinner's family menu", new HashSet<DishDto>(
            Arrays.asList(
                new DishDto(Long.valueOf(1), "Ndole"), 
                new DishDto(Long.valueOf(2), "Eru"))));

        MenuDto newMenu = new MenuDto(null, "Dinner's family menu",
            new HashSet<DishDto>(Arrays.asList(
                new DishDto(null, "Ndole"), 
                new DishDto(null, "Eru"))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MenuDto> request = new HttpEntity<>(newMenu, headers);

        ResponseEntity<MenuDto> response = this.template.postForEntity(url.toString(), request, MenuDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(wantMenu, response.getBody());
    }
}
