package ru.yanddex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yanddex.practicum.filmorate.model.Film;
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.FilmService;
import ru.yanddex.practicum.filmorate.storage.film.FilmStorage;
import ru.yanddex.practicum.filmorate.storage.user.UserStorage;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    FilmService filmService;
    @Autowired
    UserStorage userStorage;
    @Autowired
    FilmStorage filmStorage;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    public void beforeEach(){
        userStorage.clearStorage();
        filmStorage.clearStorage();
    }
    @Test
    public void createCorrectFilmEmptyListTest() throws Exception {
        Film film = new Film("Film", "Description", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void createFilmEmptyNameTest() throws Exception {
        Film film = new Film("", "Description", "2010-01-01", 121);
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
    public void createBigDescriptionTest() throws Exception {
        String badDescrition = "Художественный фильм — произведение киноискусства," +
                " фильм как продукт художественного творчества, имеющий в основе вымышленный сюжет," +
                " воплощённый в сценарии и интерпретируемый режиссёром, " +
                "который создаётся с помощью актёрской игры или средств мультипликации.";
        Film film = new Film("Film", badDescrition, "2010-01-01", 121);
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
    public void createNegativeDurationFilmTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", -121);
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
    public void createFilmWithBadDateReleaseTest() throws Exception {
        Film film = new Film("Film", "Descrition", "1010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Введена некорректная дата релиза фильма"));
    }

    @Test
    public void putUpdateFilmBadIdTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        film.setId(22);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Такого фильма нет в базе"));
    }

    @Test
    public void putUpdateFilmEmptyNameTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("", "Descrition", "2010-01-01", 121);
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
    public void putUpdateFilmNormalTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("Update", "Descrition", "2010-01-01", 121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = responseUpdate.getResponse().getContentAsString();
        assertTrue(message.contains("\"id\":1"));
        assertTrue(message.contains("\"description\":\"Descrition\""));
        assertTrue(message.contains("\"name\":\"Update\""));
        assertTrue(message.contains("\"releaseDate\":\"2010-01-01\""));
        assertTrue(message.contains("\"duration\":121"));
    }

    @Test
    public void putUpdateFilmBigDescriptionTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String badDescrition = "Художественный фильм — произведение киноискусства," +
                " фильм как продукт художественного творчества, имеющий в основе вымышленный сюжет," +
                " воплощённый в сценарии и интерпретируемый режиссёром, " +
                "который создаётся с помощью актёрской игры или средств мультипликации.";
        Film update = new Film("Update", badDescrition, "2010-01-01", 121);
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
    public void putUpdateFilmNegativeDurationTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("Update", "Descrition", "2010-01-01", -121);
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
    public void putUpdateFilmBadDataReleaseTest() throws Exception {
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Film update = new Film("Update", "Descrition", "1010-01-01", 121);
        update.setId(1);
        String updateJson = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(updateJson))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("Введена некорректная дата релиза фильма"));
    }

    @Test
    public void getFilmByIdNormalId() throws Exception{
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.get("/films/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response3.getResponse().getContentAsString();
        assertTrue(message.contains("\"id\":1"));
        assertTrue(message.contains("\"description\":\"Descrition\""));
        assertTrue(message.contains("\"name\":\"Film\""));
        assertTrue(message.contains("\"releaseDate\":\"2010-01-01\""));
        assertTrue(message.contains("\"duration\":121"));
    }

    @Test
    public void getFilmByIdBadIdTest() throws Exception{
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        String json = gson.toJson(film);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.get("/films/12"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response3.getResolvedException().getMessage();
        assertTrue(message.contains("Такого фильма нет в базе"));
    }

    @Test
    public void addLikeNormalTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        filmStorage.create(film);
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        assertTrue(filmStorage.getFilmById(1).getLikes().size()==1);
    }

    @Test
    public void addLikeBadIdFilmTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        filmStorage.create(film);
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.put("/films/12/like/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
    }

    @Test
    public void deleteLikeNormalTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        filmStorage.create(film);
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.put("/films/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response4 = mockMvc.perform(MockMvcRequestBuilders.delete("/films/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        assertEquals(0, filmStorage.getFilmById(1).getLikes().size());
    }

    @Test
    public void deleteLikeWithoutLikeTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        filmStorage.create(film);
        MvcResult response4 = mockMvc.perform(MockMvcRequestBuilders.delete("/films/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response4.getResolvedException().getMessage();
        assertTrue(message.contains("Этот пользователь не оставлял лайк для фильма"));
    }

    @Test
    public void getPopularFilmsNormalWithoutCountsTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        filmStorage.create(film);
        filmService.addLike(user,film);
        MvcResult response4 = mockMvc.perform(MockMvcRequestBuilders.get("/films/popular"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response4.getResponse().getContentAsString();
        assertTrue(message.contains("\"id\":1"));
        assertTrue(message.contains("\"description\":\"Descrition\""));
        assertTrue(message.contains("\"name\":\"Film\""));
        assertTrue(message.contains("\"releaseDate\":\"2010-01-01\""));
        assertTrue(message.contains("\"duration\":121"));
    }

    @Test
    public void getPopularFilmsNormalWithCountsTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        User user1 = new User("user1@yandex.ru", "user", "Sergey", "2020-01-01");
        Film film = new Film("Film", "Descrition", "2010-01-01", 121);
        filmStorage.create(film);
        Film film2 = new Film("Film2", "Descrition2", "2020-01-01", 221);
        filmStorage.create(film2);
        filmService.addLike(user,film);
        filmService.addLike(user,film2);
        filmService.addLike(user1,film);
        MvcResult response4 = mockMvc.perform(MockMvcRequestBuilders.get("/films/popular?count=1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response4.getResponse().getContentAsString();
        assertTrue(message.contains("\"id\":1"));
        assertTrue(message.contains("\"description\":\"Descrition\""));
        assertTrue(message.contains("\"name\":\"Film\""));
        assertTrue(message.contains("\"releaseDate\":\"2010-01-01\""));
        assertTrue(message.contains("\"duration\":121"));
        assertFalse(message.contains("\"id\":2"));
        assertFalse(message.contains("\"description\":\"Descrition2\""));
        assertFalse(message.contains("\"name\":\"Film2\""));
        assertFalse(message.contains("\"releaseDate\":\"2020-01-01\""));
        assertFalse(message.contains("\"duration\":221"));
    }

}

