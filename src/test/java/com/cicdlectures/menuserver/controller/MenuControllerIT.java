package com.cicdlectures.menuserver.controller;

import java.net.URL;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.cicdlectures.menuserver.repository.MenuRepository;
import com.cicdlectures.menuserver.service.ListMenuService;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.dto.DishDto;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;

import static org.junit.jupiter.api.Assertions.assertEquals;



// Lance l'application sur un port aléatoire.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Indique de relancer l'application à chaque test.
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MenuControllerIT {

    @LocalServerPort
    private int port;

    private URL getMenusURL() throws Exception {
        return new URL("http://localhost:" + port + "/menus");
    }
  
    // Injecte automatiquement l'instance du menu repository
    @Autowired
    private MenuRepository menuRepository;
    
    // Injecte automatiquement l'instance du TestRestTemplate
    @Autowired
    private TestRestTemplate template;

    @Test
    @DisplayName("lists all known menus")
    public void listsAllMenus() throws Exception {

        Menu menu = new Menu(
            null,
            "Christmas menu", 
            new HashSet<>(
                Arrays.asList(
                    new Dish(null, "Ndole", null),
                    new Dish(null, "Eru", null)
                )
            )
        );

        menuRepository.save(menu);
  
        // Effectue une requête GET /menus
        ResponseEntity<MenuDto[]> response = this.template.getForEntity(getMenusURL().toString(), MenuDto[].class);
  
        //Parse le payload de la réponse sous forme d'array de MenuDto
        List<MenuDto> gotMenus = Arrays.asList(response.getBody());
          
        // Verifie que le status de la réponse est 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
          
        // On défini wantMenus, les résultats attendus
        List<MenuDto> wantMenus = Arrays.asList(
            new MenuDto(
                Long.valueOf(1),
                "Christmas menu",
                new HashSet<>(
                    Arrays.asList(
                        new DishDto(Long.valueOf(1), "Ndole"),
                        new DishDto(Long.valueOf(2), "Eru")
                    )
                )
            )
        );
      
        // On compare la valeur obtenue avec la valeur attendue.
        assertEquals(wantMenus, gotMenus);
    }
}
