package org.ormmicro.dao;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.ormmicro.entity.Role;
import org.ormmicro.entity.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenericDAOTest {

    private static Connection connection;


    @SneakyThrows
    @BeforeAll
    static void setupDatabase() {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:test");
        GenericDAOTestDataUtil.createData(connection);
    }


    @Test
    public void getAllUserData() {
        GenericDAO dao = new GenericDAO<User, Integer>(connection, User.class);
        List userList = dao.findAll();
        assertTrue(userList.size() >= 3);
    }


    @Test
    public void getAllRoleData() {
        GenericDAO dao = new GenericDAO<Role, Integer>(connection, Role.class);
        List roleList = dao.findAll();
        assertEquals(3, roleList.size());
    }

    @Test
    public void findById() {
        GenericDAO<User, Integer> dao = new GenericDAO<>(connection, User.class);
        User dbUser = dao.findById(1);
        assertNotNull(dbUser);
        assertEquals(1, dbUser.getId());
    }

    @Test
    public void update() {
        GenericDAO<User, Integer> dao = new GenericDAO<>(connection, User.class);
        User user = User.builder().id(1).username("user21").password("pass21").build();
        dao.update(user);
        User dbUser = dao.findById(1);
        assertEquals("user21", dbUser.getUsername());
    }

    @Test
    public void insertAndDelete() {
        GenericDAO<User, Integer> dao = new GenericDAO<>(connection, User.class);
        User user = User.builder().username("userToInsertAndDelete").password("userToInsertAndDelete").build();
        dao.insert(user);
        User dbUser = dao.findAll().stream().filter(u -> u.getUsername().equalsIgnoreCase(user.getUsername())).findAny().orElse(null);
        assertNotNull(dbUser);
        dao.delete(dbUser.getId());
        User dbUserDeleted = dao.findById(dbUser.getId());
        assertNull(dbUserDeleted);
    }



}
