package com.wuav.client.di;

import com.wuav.client.bll.services.*;
import com.wuav.client.bll.services.interfaces.*;
import com.wuav.client.bll.strategies.AdminStrategy;
import com.wuav.client.bll.strategies.TechnicianStrategy;
import com.wuav.client.bll.strategies.UserRoleStrategyFactory;
import com.wuav.client.bll.strategies.interfaces.IUserRoleStrategy;
import com.wuav.client.bll.utilities.email.EmailSender;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.engines.CodeEngine;
import com.wuav.client.bll.utilities.engines.EmailEngine;
import com.wuav.client.bll.utilities.engines.ICodesEngine;
import com.wuav.client.bll.utilities.engines.IEmailEngine;
import com.wuav.client.bll.utilities.engines.cryptoEngine.CryptoEngine;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.dal.interfaces.*;
import com.wuav.client.dal.repository.*;

import com.wuav.client.gui.manager.StageManager;
import com.wuav.client.gui.models.DeviceModel;
import com.wuav.client.gui.models.IProjectModel;
import com.wuav.client.gui.models.ProjectModel;
import com.wuav.client.gui.models.user.CurrentUser;
import com.wuav.client.gui.models.user.IUserModel;
import com.wuav.client.gui.models.user.UserModel;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.wuav.client.gui.controllers.controllerFactory.ControllerFactory;
import com.wuav.client.gui.controllers.controllerFactory.IControllerFactory;
import com.google.inject.Singleton;

public class ConfigModule extends AbstractModule {
    @Override
    public void configure() {

        /* *************************************************************************
         *                                                                         *
         * CONTROLLER                                                                  *
         *                                                                         *
         **************************************************************************

        /*
         * Injection of binding
         */
        bind(IControllerFactory.class).to(ControllerFactory.class);

        /* *************************************************************************
         *                                                                         *
         * SERVICES AND REPOSITORIES                                                             *
         *                                                                         *
         **************************************************************************

        /*
         * Injection of the services
         */
        bind(IUserService.class).to(UserService.class);

        bind(IAuthService.class).to(AuthService.class);
        bind(IProjectService.class).to(ProjectService.class);
        bind(IProjectRepository.class).to(ProjectRepository.class);
        bind(IImageRepository.class).to(ImageRepository.class);
        bind(IAddressRepository.class).to(AddressRepository.class);
        bind(ICustomerRepository.class).to(CustomerRepository.class);
        bind(ICustomerService.class).to(CustomerService.class);
        bind(ICryptoEngine.class).to(CryptoEngine.class);
        bind(IRoleService.class).to(RoleService.class);
        bind(IRoleRepository.class).to(RoleRepository.class);

        bind(IDeviceRepository.class).to(DeviceRepository.class);
        bind(IDeviceService.class).to(DeviceService.class);
        bind(DeviceModel.class).asEagerSingleton();

        bind(AdminStrategy.class);
        bind(TechnicianStrategy.class);
        bind(CurrentUser.class).asEagerSingleton();

        bind(UserRoleStrategyFactory.class).asEagerSingleton();

        /*
         * Binds api service
         */

        bind(IUserModel.class).to(UserModel.class).in(Singleton.class);
        bind(StageManager.class).asEagerSingleton();

        bind(IUserRepository.class).to(UserRepository.class);
        bind(IProjectRepository.class).to(ProjectRepository.class);

        bind(ICodesEngine.class).to(CodeEngine.class).in(Singleton.class);
        bind(IEmailSender.class).to(EmailSender.class);
        bind(IEmailEngine.class).to(EmailEngine.class);
        bind(IProjectModel.class).to(ProjectModel.class).asEagerSingleton();
        bind(ImageCache.class).asEagerSingleton();
        bind(IEmailSender.class).to(EmailSender.class);

        /*
         * Bind even bus as in singleton scope
         * As eager singleton to ensure instantiation asap Injector is created
         */
        bind(EventBus.class).asEagerSingleton();
        /* *************************************************************************
        *                                                                         *
        * MODEL                                                                   *
        *                                                                         *
        **************************************************************************
        /*
         * Injection of
         */

    }
}
