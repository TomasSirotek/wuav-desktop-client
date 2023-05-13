package com.wuav.client.di;

import com.wuav.client.bll.services.*;
import com.wuav.client.bll.services.interfaces.*;
import com.wuav.client.bll.utilities.email.EmailSender;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.engines.CodeEngine;
import com.wuav.client.bll.utilities.engines.EmailEngine;
import com.wuav.client.bll.utilities.engines.ICodesEngine;
import com.wuav.client.bll.utilities.engines.IEmailEngine;
import com.wuav.client.bll.utilities.engines.cryptoEngine.CryptoEngine;
import com.wuav.client.bll.utilities.engines.cryptoEngine.ICryptoEngine;
import com.wuav.client.bll.utilities.pdf.IPdfGenerator;
import com.wuav.client.bll.utilities.pdf.PdfGenerator;
import com.wuav.client.cache.ImageCache;
import com.wuav.client.dal.interfaces.*;
import com.wuav.client.dal.repository.*;

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
         * SERVICE                                                                 *
         *                                                                         *
         **************************************************************************

        /*
         * Injection of movie service
         */
        bind(IUserService.class).to(UserService.class);

        bind(IAuthService.class).to(AuthService.class);
        bind(IProjectService.class).to(ProjectService.class);
        bind(IProjectRepository.class).to(ProjectRepository.class);
        bind(IImageRepository.class).to(ImageRepository.class);
        bind(IAddressRepository.class).to(AddressRepository.class);
        bind(IAddressService.class).to(AddressService.class);
        bind(ICustomerRepository.class).to(CustomerRepository.class);
        bind(ICustomerService.class).to(CustomerService.class);
        bind(IPdfGenerator.class).to(PdfGenerator.class);
        bind(ICryptoEngine.class).to(CryptoEngine.class);
        bind(IRoleService.class).to(RoleService.class);
        bind(IRoleRepository.class).to(RoleRepository.class);

        /*
         * Bind even bus as in singleton scope
         * As eager singleton to ensure instantiation asap Injector is created
         */
      //  bind(EventBus.class).asEagerSingleton();

        bind(CurrentUser.class).asEagerSingleton();

        /*
         * Injection of movie service
         */

        /*
         * Binds api service
         */

        bind(IUserModel.class).to(UserModel.class).in(Singleton.class);


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



        /* *************************************************************************
        *                                                                         *
        * DAO                                                                     *
        *                                                                         *
        **************************************************************************
        /*
         * Bind the MovieDAO interface to the implementation
         */
        /*
         * Bind the CategoryDAO interface to the implementation
         */

        /* *************************************************************************
        *                                                                         *
        * EVENT                                                                   *
        *                                                                         *
        **************************************************************************

        /*
         * Bind even bus as in singleton scope
         * As eager singleton to ensure instantiation asap Injector is created
         */
      //  bind(EventBus.class).asEagerSingleton();

        /* *************************************************************************
        *                                                                         *
        * HELPER                                                                   *
        *                                                                         *
        **************************************************************************

        /*
         * Injection of Filter helper
         */
    }
}
