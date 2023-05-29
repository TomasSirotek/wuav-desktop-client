package com.wuav.client.bll.strategies;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;

/**
 * The factory for the user role strategies
 */
public class UserRoleStrategyFactory {

    private final Provider<AdminStrategy> adminStrategyProvider;
    private final Provider<TechnicianStrategy> technicianStrategyProvider;
    private final Provider<SalesStrategy> salesStrategyProvider;
    private final Provider<ManagerStrategy> managerStrategyProvider;

    /**
     * Constructor
     *
     * @param adminStrategyProvider the admin strategy provider
     * @param technicianStrategyProvider the technician strategy provider
     * @param salesStrategyProvider the sales strategy provider
     * @param managerStrategyProvider the manager strategy provider
     */
    @Inject
    public UserRoleStrategyFactory(Provider<AdminStrategy> adminStrategyProvider, Provider<TechnicianStrategy> technicianStrategyProvider, Provider<SalesStrategy> salesStrategyProvider, Provider<ManagerStrategy> managerStrategyProvider) {
        this.adminStrategyProvider = adminStrategyProvider;
        this.technicianStrategyProvider = technicianStrategyProvider;
        this.salesStrategyProvider = salesStrategyProvider;
        this.managerStrategyProvider = managerStrategyProvider;
    }

    /**
     * Gets the strategy for the given user
     *
     * @param user the user to get the strategy for
     * @return the strategy
     */
    public IUserRoleStrategy getStrategy(AppUser user) {
        String roleName = user.getRoles().get(0).getName();
        switch (roleName) {
            case "ADMIN":
                return adminStrategyProvider.get();
            case "TECHNICIAN":
                return technicianStrategyProvider.get();
            case "MANAGER":
                return managerStrategyProvider.get();
            case "SALES":
                return salesStrategyProvider.get();
            default:
                throw new IllegalArgumentException("Invalid role type: " + roleName);
        }
    }

}
