package ru.yanddex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yanddex.practicum.filmorate.controller.FilmController;
import ru.yanddex.practicum.filmorate.model.Film;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = FilmController.class)
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Test
    public void createCorrectFilmEmptyListTest() throws Exception {
        Film film = new Film("Film","Description","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void createFilmEmptyNameTest() throws Exception {
        Film film = new Film("","Description","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [name]"));
        assertTrue(message.contains("default message [Название фильма должно быть заполнено]"));
    }

    @Test
    public void createBigDescriptionTest() throws Exception{
        String badDescrition = "Художественный фильм — произведение киноискусства," +
                " фильм как продукт художественного творчества, имеющий в основе вымышленный сюжет," +
                " воплощённый в сценарии и интерпретируемый режиссёром, " +
                "который создаётся с помощью актёрской игры или средств мультипликации.";
        Film film = new Film("Film",badDescrition,"2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [description]"));
        assertTrue(message.contains("default message [Описание фильма не должно превышать 200 знаков]"));
    }

    @Test
    public void createNegativeDurationFilmTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",-121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [duration]"));
        assertTrue(message.contains("default message [Длительность фильма - положительная величина]"));
    }

    @Test
    public void createFilmWithBadDateReleaseTest() throws Exception{
        Film film = new Film("Film","Descrition","1010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Введена некорректная дата релиза фильма"));
    }

    @Test
    public void putUpdateFilmBadIdTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",121);
        film.setId(22);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Такого фильма нет в базе!"));
    }

    @Test
    public void putUpdateFilmEmptyNameTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("","Descrition","2010-01-01",121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [name]"));
        assertTrue(message.contains("default message [Название фильма должно быть заполнено]"));
    }

    @Test
    public void putUpdateFilmNormalTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("Update","Descrition","2010-01-01",121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = responseUpdate.getResponse().getContentAsString();
        assertEquals(updateJson,message);
    }

    @Test
    public void putUpdateFilmBigDescriptionTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String badDescrition = "Художественный фильм — произведение киноискусства," +
                " фильм как продукт художественного творчества, имеющий в основе вымышленный сюжет," +
                " воплощённый в сценарии и интерпретируемый режиссёром, " +
                "который создаётся с помощью актёрской игры или средств мультипликации.";
        Film update = new Film("Update",badDescrition,"2010-01-01",121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [description]"));
        assertTrue(message.contains("default message [Описание фильма не должно превышать 200 знаков]"));
    }

    @Test
    public void putUpdateFilmNegativeDurationTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("Update","Descrition","2010-01-01",-121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [duration]"));
        assertTrue(message.contains("default message [Длительность фильма - положительная величина]"));
    }

    @Test
    public void putUpdateFilmBadDataReleaseTest() throws Exception{
        Film film = new Film("Film","Descrition","2010-01-01",121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("Update","Descrition","1010-01-01",121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("Введена некорректная дата релиза фильма"));
    }
}
