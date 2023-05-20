package com.wuav.client.bll.strategies;

import com.google.inject.Provider;
import com.wuav.client.be.user.AppRole;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.gui.utils.enums.UserRoleType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRoleStrategyFactoryTest {

    @Mock
    private Provider<AdminStrategy> mockAdminStrategyProvider;
    @Mock
    private Provider<TechnicianStrategy> mockTechnicianStrategyProvider;
    @Mock
    private Provider<SalesStrategy> mockSalesStrategyProvider;
    @Mock
    private Provider<ManagerStrategy> mockManagerStrategyProvider;
    private UserRoleStrategyFactory userRoleStrategyFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userRoleStrategyFactory = new UserRoleStrategyFactory(
                mockAdminStrategyProvider,
                mockTechnicianStrategyProvider,
                mockSalesStrategyProvider,
                mockManagerStrategyProvider
        );
    }

    @Test
    void testGetStrategyWithAdminRole() {
        // Arrange
        AppUser user = new AppUser();
        List<AppRole> roles = new ArrayList<>();
        AppRole role = new AppRole();
        role.setName(UserRoleType.ADMIN.name());
        roles.add(role);
        user.setRoles(roles);

        // Mock the expected strategy
        AdminStrategy mockAdminStrategy = mock(AdminStrategy.class);
        // Set up the mockAdminStrategyProvider to return the mockAdminStrategy
        when(mockAdminStrategyProvider.get()).thenReturn(mockAdminStrategy);
        // Act
        IUserRoleStrategy strategy = userRoleStrategyFactory.getStrategy(user);
        // Assert
        assertTrue(strategy instanceof AdminStrategy);
        assertEquals(mockAdminStrategy, strategy);
    }

    @Test
    void testGetStrategyWithTechnicianRole() {
        // Arrange
        AppUser user = new AppUser();
        List<AppRole> roles = new ArrayList<>();
        AppRole role = new AppRole();
        role.setName(UserRoleType.TECHNICIAN.name());
        roles.add(role);
        user.setRoles(roles);

        // Mock the expected strategy
        TechnicianStrategy mockTechnicianStrategy = mock(TechnicianStrategy.class);
        // Set up the mockAdminStrategyProvider to return the mockAdminStrategy
        when(mockTechnicianStrategyProvider.get()).thenReturn(mockTechnicianStrategy);
        // Act
        IUserRoleStrategy strategy = userRoleStrategyFactory.getStrategy(user);
        // Assert
        assertTrue(strategy instanceof TechnicianStrategy);
        assertEquals(mockTechnicianStrategy, strategy);
    }

    @Test
    void testGetStrategyWithSalesRole() {
        // Arrange
        AppUser user = new AppUser();
        List<AppRole> roles = new ArrayList<>();
        AppRole role = new AppRole();
        role.setName(UserRoleType.SALES.name());
        roles.add(role);
        user.setRoles(roles);

        // Mock the expected strategy
        SalesStrategy mockSalesStrategy = mock(SalesStrategy.class);
        // Set up the mockAdminStrategyProvider to return the mockAdminStrategy
        when(mockSalesStrategyProvider.get()).thenReturn(mockSalesStrategy);
        // Act
        IUserRoleStrategy strategy = userRoleStrategyFactory.getStrategy(user);
        // Assert
        assertTrue(strategy instanceof SalesStrategy);
        assertEquals(mockSalesStrategy, strategy);
    }

    @Test
    void testGetStrategyWithManagerRole() {
        // Arrange
        AppUser user = new AppUser();
        List<AppRole> roles = new ArrayList<>();
        AppRole role = new AppRole();
        role.setName(UserRoleType.MANAGER.name());
        roles.add(role);
        user.setRoles(roles);

        // Mock the expected strategy
        ManagerStrategy mockManagerStrategy = mock(ManagerStrategy.class);
        // Set up the mockAdminStrategyProvider to return the mockAdminStrategy
        when(mockManagerStrategyProvider.get()).thenReturn(mockManagerStrategy);
        // Act
        IUserRoleStrategy strategy = userRoleStrategyFactory.getStrategy(user);
        // Assert
        assertTrue(strategy instanceof ManagerStrategy);
        assertEquals(mockManagerStrategy, strategy);
    }
    
    @Test
    void testGetStrategyWithInvalidRole() {
        // Arrange
        AppUser user = new AppUser();
        List<AppRole> roles = new ArrayList<>();
        AppRole role = new AppRole();
        role.setName("INVALID_ROLE");
        roles.add(role);
        user.setRoles(roles);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> userRoleStrategyFactory.getStrategy(user));
    }

}
