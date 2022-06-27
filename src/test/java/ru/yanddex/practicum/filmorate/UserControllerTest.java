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
import ru.yanddex.practicum.filmorate.controller.UserController;
import ru.yanddex.practicum.filmorate.model.User;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @Test
    public void createCorrectUserEmptyListTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void createNewUserEmptyEmailTest() throws Exception{
        User user = new User("","user","Sergey","2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [Почта не должна быть пустой]"));
    }

    @Test
    public void createNewUserBadEmailTest() throws Exception{
        User user = new User("wiwiwiwiwi","user","Sergey","2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [Почта должна содержать @]"));
    }

    @Test
    public void createNewUserEmptyLoginTest() throws Exception{
        User user = new User("user@yandex.ru","","Sergey","2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [Логин не может быть пустым]"));
    }

    @Test
    public void createNewUserBadLoginTest() throws Exception{
        User user = new User("user@yandex.ru","u ser","Sergey","2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [Логин должен состоять из букв и цифр без пробелов]"));
    }

    @Test
    public void createNewUserEmptyNameChangeToLoginTest() throws Exception{
        User user = new User("user@yandex.ru","user","","2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response.getResponse().getContentAsString();
        User user1 = gson.fromJson(message,User.class);
        assertTrue(user1.getName().equals(user1.getLogin()));
    }

    @Test
    public void createNewUserBadBirthdayTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2030-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [Дата рождения не может быть в будущем]"));
    }

    @Test
    public void updateUserBadIdTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2020-01-01");
        user.setId(1211221);
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("Такого пользователя нет в базе!"));
    }

    @Test
    public void updateUserCorrectTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru","update","Sergey","2020-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = responseUpdate.getResponse().getContentAsString();
        assertEquals(jsonUpdate,message);
    }

    @Test
    public void updateUserEmptyEmailTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("","update","Sergey","2020-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [Почта не должна быть пустой]"));
    }

    @Test
    public void updateNewUserBadEmailTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("wiwiwiwiwi","user","Sergey","2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [Почта должна содержать @]"));
    }

    @Test
    public void updateNewUserEmptyLoginTest() throws Exception{
        User user = new User( "user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru","","Sergey","2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [Логин не может быть пустым]"));
    }

    @Test
    public void updateNewUserBadLoginTest() throws Exception{
        User user = new User( "user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru","u ser","Sergey","2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [Логин должен состоять из букв и цифр без пробелов]"));
    }

    @Test
    public void updateNewUserEmptyNameChangeToLoginTest() throws Exception{
        User user = new User( "user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru","user","","2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = responseUpdate.getResponse().getContentAsString();
        User user1 = gson.fromJson(message,User.class);
        assertTrue(user1.getName().equals(user1.getLogin()));
    }

    @Test
    public void updateNewUserBadBirthdayTest() throws Exception{
        User user = new User("user@yandex.ru","user","Sergey","2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru","user","Sergey","2030-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [Дата рождения не может быть в будущем]"));
    }
}
