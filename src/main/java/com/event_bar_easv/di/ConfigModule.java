package com.event_bar_easv.di;

import com.event_bar_easv.bll.services.EventService;
import com.event_bar_easv.bll.services.UserService;
import com.event_bar_easv.bll.services.interfaces.IEventService;
import com.event_bar_easv.bll.services.interfaces.IUserService;
import com.event_bar_easv.bll.utilities.email.EmailConnectionFactory;
import com.event_bar_easv.bll.utilities.email.EmailSender;
import com.event_bar_easv.bll.utilities.email.IEmailSender;
import com.event_bar_easv.bll.utilities.engines.CodeEngine;
import com.event_bar_easv.bll.utilities.engines.ICodesEngine;
import com.event_bar_easv.bll.utilities.pdf.IPdfGenerator;
import com.event_bar_easv.bll.utilities.pdf.PdfGenerator;
import com.event_bar_easv.dal.interfaces.IEventRepository;
import com.event_bar_easv.dal.interfaces.IUserRepository;
import com.event_bar_easv.dal.reporitory.EventRepository;
import com.event_bar_easv.dal.reporitory.UserRepository;
import com.event_bar_easv.gui.models.event.EventModel;
import com.event_bar_easv.gui.models.event.IEventModel;
import com.event_bar_easv.gui.models.user.IUserModel;
import com.event_bar_easv.gui.models.user.UserModel;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.event_bar_easv.gui.controllers.controllerFactory.ControllerFactory;
import com.event_bar_easv.gui.controllers.controllerFactory.IControllerFactory;
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
        bind(IEventService.class).to(EventService.class);


        /*
         * Injection of movie service
         */

        /*
         * Binds api service
         */

        bind(IUserModel.class).to(UserModel.class).in(Singleton.class);
        bind(IEventModel.class).to(EventModel.class).in(Singleton.class);

        bind(IUserRepository.class).to(UserRepository.class);
        bind(IEventRepository.class).to(EventRepository.class);

        bind(ICodesEngine.class).to(CodeEngine.class).in(Singleton.class);
        bind(IPdfGenerator.class).to(PdfGenerator.class).in(Singleton.class);
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
