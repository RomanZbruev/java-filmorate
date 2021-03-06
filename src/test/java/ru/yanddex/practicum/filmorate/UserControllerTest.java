package ru.yanddex.practicum.filmorate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
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
import ru.yanddex.practicum.filmorate.model.User;
import ru.yanddex.practicum.filmorate.service.UserService;
import ru.yanddex.practicum.filmorate.storage.user.UserStorage;


import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    UserStorage userStorage;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    @BeforeEach
    public void beforeEach(){
        userStorage.clearStorage();
    }
    @Test
    public void createCorrectUserEmptyListTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void createNewUserEmptyEmailTest() throws Exception {
        User user = new User("", "user", "Sergey", "2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [?????????? ???? ???????????? ???????? ????????????]"));
    }

    @Test
    public void createNewUserBadEmailTest() throws Exception {
        User user = new User("wiwiwiwiwi", "user", "Sergey", "2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [?????????? ???????????? ?????????????????? @]"));
    }

    @Test
    public void createNewUserEmptyLoginTest() throws Exception {
        User user = new User("user@yandex.ru", "", "Sergey", "2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [?????????? ???? ?????????? ???????? ????????????]"));
    }

    @Test
    public void createNewUserBadLoginTest() throws Exception {
        User user = new User("user@yandex.ru", "u ser", "Sergey", "2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [?????????? ???????????? ???????????????? ???? ???????? ?? ???????? ?????? ????????????????]"));
    }

    @Test
    public void createNewUserEmptyNameChangeToLoginTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "", "2010-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response.getResponse().getContentAsString();
        User user1 = gson.fromJson(message, User.class);
        assertTrue(user1.getName().equals(user1.getLogin()));
    }

    @Test
    public void createNewUserBadBirthdayTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2030-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [???????? ???????????????? ???? ?????????? ???????? ?? ??????????????]"));
    }

    @Test
    public void updateUserBadIdTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        user.setId(1211221);
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response.getResolvedException().getMessage();
        assertTrue(message.contains("???????????? ???????????????????????? ?????? ?? ????????!"));
    }

    @Test
    public void updateUserCorrectTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru", "update", "Sergey", "2020-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = responseUpdate.getResponse().getContentAsString();
        assertTrue(message.contains("\"email\":\"user@yandex.ru\""));
        assertTrue(message.contains("\"login\":\"update\""));
        assertTrue(message.contains("\"name\":\"Sergey\""));
        assertTrue(message.contains("\"birthday\":\"2020-01-01\""));
    }

    @Test
    public void updateUserEmptyEmailTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("", "update", "Sergey", "2020-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [?????????? ???? ???????????? ???????? ????????????]"));
    }

    @Test
    public void updateNewUserBadEmailTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("wiwiwiwiwi", "user", "Sergey", "2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [email]"));
        assertTrue(message.contains("default message [?????????? ???????????? ?????????????????? @]"));
    }

    @Test
    public void updateNewUserEmptyLoginTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru", "", "Sergey", "2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [?????????? ???? ?????????? ???????? ????????????]"));
    }

    @Test
    public void updateNewUserBadLoginTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru", "u ser", "Sergey", "2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [login]"));
        assertTrue(message.contains("default message [?????????? ???????????? ???????????????? ???? ???????? ?? ???????? ?????? ????????????????]"));
    }

    @Test
    public void updateNewUserEmptyNameChangeToLoginTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru", "user", "", "2010-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = responseUpdate.getResponse().getContentAsString();
        User user1 = gson.fromJson(message, User.class);
        assertTrue(user1.getName().equals(user1.getLogin()));
    }

    @Test
    public void updateNewUserBadBirthdayTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User update = new User("user@yandex.ru", "user", "Sergey", "2030-01-01");
        update.setId(1);
        String jsonUpdate = gson.toJson(update);
        MvcResult responseUpdate = mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(jsonUpdate))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = responseUpdate.getResolvedException().getMessage();
        assertTrue(message.contains("default message [birthday]"));
        assertTrue(message.contains("default message [???????? ???????????????? ???? ?????????? ???????? ?? ??????????????]"));
    }

    @Test
    public void addFriendNormalWorkTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user2 = new User("user2@yandex.ru", "user2", "Sergey", "2020-01-01");
        String json2 = gson.toJson(user);
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json2))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
    }

    @Test
    public void addFriendBadIdTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
    }

    @Test
    public void getUserByIdNormalTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response3.getResponse().getContentAsString();
        assertTrue(message.contains("\"email\":\"user@yandex.ru\""));
        assertTrue(message.contains("\"login\":\"user\""));
        assertTrue(message.contains("\"name\":\"Sergey\""));
        assertTrue(message.contains("\"birthday\":\"2020-01-01\""));
    }

    @Test
    public void getUserByIdBadIdTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.get("/users/2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response2.getResolvedException().getMessage();
        assertTrue(message.contains("???????????? ???????????????????????? ?????? ?? ????????"));
    }

    @Test
    public void deleteFriendNormalWorkTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user2 = new User("user2@yandex.ru", "user2", "Sergey", "2020-01-01");
        String json2 = gson.toJson(user);
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json2))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult test = mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        Assertions.assertTrue(userStorage.getUserById(1).getFriends().size()==0);
        Assertions.assertTrue(userStorage.getUserById(2).getFriends().size()==0);
    }

    @Test
    public void deleteFriendNotInFriendListTest() throws Exception {
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        User user2 = new User("user2@yandex.ru", "user2", "Sergey", "2020-01-01");
        String json2 = gson.toJson(user);
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json2))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response3 = mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError()).andReturn();
        String message = response3.getResolvedException().getMessage();
        Assertions.assertTrue(message.contains("???????????? ???????????????? ???? ????????????. ???????????????????????? ???? ?? ???????????? ????????????"));
    }

    @Test
    public void getFriendListEmptyListTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        String json = gson.toJson(user);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response2.getResponse().getContentAsString();
        Assertions.assertTrue(message.contains("[]"));
    }

    @Test
    public void getFriendListNormalTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        User user2 = new User("user2@yandex.ru", "user2", "Sergey", "2020-01-01");
        userStorage.create(user2);
        userService.addFriend(user,user2);
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response2.getResponse().getContentAsString();
        assertTrue(message.contains("\"email\":\"user2@yandex.ru\""));
        assertTrue(message.contains("\"login\":\"user2\""));
        assertTrue(message.contains("\"name\":\"Sergey\""));
        assertTrue(message.contains("\"birthday\":\"2020-01-01\""));
    }

    @Test
    public void getCommonFriendListNormalTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        User user2 = new User("user2@yandex.ru", "user2", "Sergey", "2020-01-01");
        userStorage.create(user2);
        User user3 = new User("user3@yandex.ru", "user3", "Sergey", "2020-01-01");
        userStorage.create(user3);
        userService.addFriend(user,user2);
        userService.addFriend(user3,user2);
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/3"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response2.getResponse().getContentAsString();
        assertTrue(message.contains("\"email\":\"user2@yandex.ru\""));
        assertTrue(message.contains("\"login\":\"user2\""));
        assertTrue(message.contains("\"name\":\"Sergey\""));
        assertTrue(message.contains("\"birthday\":\"2020-01-01\""));
    }

    @Test
    public void getCommonFriendEmptyListTest() throws Exception{
        User user = new User("user@yandex.ru", "user", "Sergey", "2020-01-01");
        userStorage.create(user);
        User user2 = new User("user2@yandex.ru", "user2", "Sergey", "2020-01-01");
        userStorage.create(user2);
        MvcResult response2 = mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andReturn();
        String message = response2.getResponse().getContentAsString();
        assertTrue(message.contains("[]"));
    }
}
