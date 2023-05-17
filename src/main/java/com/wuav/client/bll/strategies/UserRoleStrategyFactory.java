package com.wuav.client.bll.strategies;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.wuav.client.be.user.AppUser;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.config.StartUp;
import com.wuav.client.gui.utils.enums.UserRoleType;

public class UserRoleStrategyFactory {

    private final Provider<AdminStrategy> adminStrategyProvider;
    private final Provider<TechnicianStrategy> technicianStrategyProvider;

    @Inject
    public UserRoleStrategyFactory(Provider<AdminStrategy> adminStrategyProvider, Provider<TechnicianStrategy> technicianStrategyProvider) {
        this.adminStrategyProvider = adminStrategyProvider;
        this.technicianStrategyProvider = technicianStrategyProvider;
    }

    public IUserRoleStrategy getStrategy(AppUser user) {
        String roleName = user.getRoles().get(0).getName();
        switch (roleName) {
            case "ADMIN":
                return adminStrategyProvider.get();
            case "TECHNICIAN":
                return technicianStrategyProvider.get();
            default:
                throw new IllegalArgumentException("Invalid role type: " + roleName);
        }
    }
}
