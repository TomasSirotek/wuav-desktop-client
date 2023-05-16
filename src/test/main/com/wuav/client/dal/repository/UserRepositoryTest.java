package com.wuav.client.dal.repository;

import com.wuav.client.be.user.AppUser;
import com.wuav.client.dal.mappers.IUserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserRepositoryTest {

    @Mock
    private SqlSession sqlSession;

    @Mock
    private IUserMapper iUserMapper;


    private SqlSessionFactory sqlSessionFactory;
    @InjectMocks
    private UserRepository userRepository;

    private static final int EXISTING_USER_ID = 1;
    private static final int EXISTING_PROJECT_ID = 1602978299;
    private static final String EXISTING_USER_NAME = "Michael Jørgensen";
    private static final String EXISTING_USER_EMAIL = "tech@hotmail.com";

    private static final Date EXISTING_USER_CREATION_DATE = Date.from(LocalDate.parse("2024-04-04").atStartOfDay(ZoneId.systemDefault()).toInstant());


    @BeforeEach
    void setUp() {
        sqlSessionFactory = mock(SqlSessionFactory.class);
        MockitoAnnotations.initMocks(this);
        doReturn(sqlSession).when(sqlSessionFactory).openSession();
        doReturn(iUserMapper).when(sqlSession).getMapper(IUserMapper.class);
    }

    @Test
    void testGetAllUsers() {
        // Arrange
        List<AppUser> actualUsers = userRepository.getAllUsers();

        // Assert
        assertFalse(actualUsers.isEmpty(), "The returned user list is empty");
    }

    @Test
    void testGetUserByEmail() {
        AppUser expectedUser = new AppUser();
        expectedUser.setEmail(EXISTING_USER_EMAIL);
        expectedUser.setId(EXISTING_USER_ID);
        expectedUser.setName(EXISTING_USER_NAME);
        expectedUser.setCreatedAt(EXISTING_USER_CREATION_DATE);

        when(iUserMapper.getUserByEmail(EXISTING_USER_EMAIL)).thenReturn(expectedUser);

        // Act
        AppUser actualUser = userRepository.getUserByEmail(EXISTING_USER_EMAIL);

        // Assert
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getCreatedAt(), actualUser.getCreatedAt());
    }


    @Test
    void getUserByEmail() {
    }

    @Test
    void getUserById() {
        AppUser expectedUser = new AppUser();
        expectedUser.setEmail(EXISTING_USER_EMAIL);
        expectedUser.setId(EXISTING_USER_ID);
        expectedUser.setName(EXISTING_USER_NAME);
        expectedUser.setCreatedAt(EXISTING_USER_CREATION_DATE);

        when(iUserMapper.getUserById(EXISTING_USER_ID)).thenReturn(expectedUser);

        // Act
        AppUser actualUser = userRepository.getUserById(EXISTING_USER_ID);

        // Assert
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getCreatedAt(), actualUser.getCreatedAt());
    }

    @Test
    void updateUserById() {
    }

    @Test
    void createUser() {
    }

    @Test
    void getUserByProjectId() {
        AppUser expectedUser = new AppUser();
        expectedUser.setEmail(EXISTING_USER_EMAIL);
        expectedUser.setId(EXISTING_USER_ID);
        expectedUser.setName(EXISTING_USER_NAME);
        expectedUser.setCreatedAt(EXISTING_USER_CREATION_DATE);

        when(iUserMapper.getUserByProjectId(EXISTING_PROJECT_ID)).thenReturn(expectedUser);

        // Act
        AppUser actualUser = userRepository.getUserByProjectId(EXISTING_PROJECT_ID);

        // Assert
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getCreatedAt(), actualUser.getCreatedAt());
    }
}