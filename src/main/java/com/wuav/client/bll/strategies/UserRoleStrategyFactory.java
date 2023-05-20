package com.wuav.client.bll.strategies;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;

public class UserRoleStrategyFactory {

    private final Provider<AdminStrategy> adminStrategyProvider;
    private final Provider<TechnicianStrategy> technicianStrategyProvider;
    private final Provider<SalesStrategy> salesStrategyProvider;
    private final Provider<ManagerStrategy> managerStrategyProvider;

    @Inject
    public UserRoleStrategyFactory(Provider<AdminStrategy> adminStrategyProvider, Provider<TechnicianStrategy> technicianStrategyProvider, Provider<SalesStrategy> salesStrategyProvider, Provider<ManagerStrategy> managerStrategyProvider) {
        this.adminStrategyProvider = adminStrategyProvider;
        this.technicianStrategyProvider = technicianStrategyProvider;
        this.salesStrategyProvider = salesStrategyProvider;
        this.managerStrategyProvider = managerStrategyProvider;
    }

    public IUserRoleStrategy getStrategy(AppUser user) {
        String roleName = user.getRoles().get(0).getName();
        switch (roleName) {
            case "ADMIN":
                return adminStrategyProvider.get();
            case "TECHNICIAN":
                return technicianStrategyProvider.get();
            case "MANAGER":
                return managerStrategyProvider.get();
            case "SALE":
                return salesStrategyProvider.get();
            default:
                throw new IllegalArgumentException("Invalid role type: " + roleName);
        }
    }

}
