package com.cicdlectures.menuserver.service;

import java.util.List;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cicdlectures.menuserver.repository.MenuRepository;
import com.cicdlectures.menuserver.service.ListMenuService;
import com.cicdlectures.menuserver.dto.MenuDto;
import com.cicdlectures.menuserver.dto.DishDto;
import com.cicdlectures.menuserver.model.Dish;
import com.cicdlectures.menuserver.model.Menu;

public class ListMenuServiceTests {

    private MenuRepository repository;

    private ListMenuService subject;
  
    @BeforeEach
    public void init() {
      this.repository = mock(MenuRepository.class);
      subject = new ListMenuService(repository);
    }
  
    @Test
    @DisplayName("lists all known menus")
    public void listsKnownMenus() {

      Iterable<Menu> menus = Arrays.asList(
        new Menu(Long.valueOf(1),
          "Christmas menu", 
          new HashSet<>(
            Arrays.asList(
              new Dish(Long.valueOf(1), "Ndole", null),
              new Dish(Long.valueOf(2), "Eru", null)
            )
          )
        )
      );
      
      // On configure le menuRepository pour qu'il retourne notre liste de menus.
      when(repository.findAll()).thenReturn(menus);

      // On appelle notre sujet
      List<MenuDto> gotMenus = subject.listMenus();

      // On défini wantMenus, les résultats attendus
      Iterable<MenuDto> wantMenus = Arrays.asList(
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