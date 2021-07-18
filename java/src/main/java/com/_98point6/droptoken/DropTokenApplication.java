package com._98point6.droptoken;

import com._98point6.droptoken.dao.GameDAO;
import com._98point6.droptoken.dao.MoveDAO;
import com._98point6.droptoken.entities.Games;
import com._98point6.droptoken.entities.Moves;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.jersey.validation.JerseyViolationExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 *
 */
public class DropTokenApplication extends Application<DropTokenConfiguration> {
        public static void main(String[] args) throws Exception {
            new DropTokenApplication().run(args);
        }

        @Override
        public String getName() {
            return "98Point6 - Drop Token";
        }

        private final HibernateBundle<DropTokenConfiguration> hibernate = new HibernateBundle<DropTokenConfiguration>(
                Games.class, Moves.class
        ) {
            public DataSourceFactory getDataSourceFactory(DropTokenConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        };

        @Override
        public void initialize(Bootstrap<DropTokenConfiguration> bootstrap) {
            bootstrap.addBundle(hibernate);
        }

        @Override
        public void run(DropTokenConfiguration configuration,
                Environment environment) {
            environment.getObjectMapper()
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .registerModule(new Jdk8Module());
            environment.getObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

            environment.jersey().register(new DropTokenExceptionMapper());
            environment.jersey().register(new JerseyViolationExceptionMapper());
            environment.jersey().register(new JsonProcessingExceptionMapper());
            environment.jersey().register(new EarlyEofExceptionMapper());

            final MoveDAO moveDAO = new MoveDAO(hibernate.getSessionFactory());
            final GameDAO gamedao = new GameDAO(hibernate.getSessionFactory());

            environment.jersey().register(new DropTokenResource(gamedao, moveDAO));

        }

}
