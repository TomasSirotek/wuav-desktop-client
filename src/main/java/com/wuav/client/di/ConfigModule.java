package com.wuav.client.di;

import com.wuav.client.bll.services.UserService;
import com.wuav.client.bll.services.interfaces.IEventService;
import com.wuav.client.bll.services.interfaces.IUserService;
import com.wuav.client.bll.utilities.email.EmailConnectionFactory;
import com.wuav.client.bll.utilities.email.EmailSender;
import com.wuav.client.bll.utilities.email.IEmailSender;
import com.wuav.client.bll.utilities.engines.CodeEngine;
import com.wuav.client.bll.utilities.engines.ICodesEngine;
import com.wuav.client.dal.interfaces.IEventRepository;
import com.wuav.client.dal.interfaces.IUserRepository;
import com.wuav.client.dal.reporitory.EventRepository;
import com.wuav.client.dal.reporitory.UserRepository;

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



        /*
         * Injection of movie service
         */

        /*
         * Binds api service
         */

        bind(IUserModel.class).to(UserModel.class).in(Singleton.class);


        bind(IUserRepository.class).to(UserRepository.class);
        bind(IEventRepository.class).to(EventRepository.class);

        bind(ICodesEngine.class).to(CodeEngine.class).in(Singleton.class);

        bind(IEmailSender.class).to(EmailSender.class);

        bind(EmailConnectionFactory.class).asEagerSingleton();
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
        bind(EventBus.class).asEagerSingleton();

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
