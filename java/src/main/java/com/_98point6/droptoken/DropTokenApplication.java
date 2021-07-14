package com._98point6.droptoken;

import com._98point6.droptoken.dao.GetGamesDao;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.dropwizard.Application;
//import io.dropwizard.auth.AuthDynamicFeature;
//import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.jersey.validation.JerseyViolationExceptionMapper;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.skife.jdbi.v2.DBI;

/**
 *
 */
public class DropTokenApplication extends Application<DropTokenConfiguration> {
    private static final String SQL = "sql";
    private static final String DROPWIZARD_BLOG_SERVICE = "Dropwizard blog service";
    private static final String BEARER = "Bearer";

    public static void main(String[] args) throws Exception {
        new DropTokenApplication().run(args);
    }

    @Override
    public String getName() {
        return "98Point6 - Drop Token";
    }

    @Override
    public void initialize(Bootstrap<DropTokenConfiguration> bootstrap) {
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

        // Datasource configuration
//        final DataSource dataSource =
//                configuration.getDataSourceFactory().build(environment.metrics(), SQL);
//        DBI dbi = new DBI(dataSource);

        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        final GetGamesDao getGamesDao = jdbi.onDemand(GetGamesDao.class);

//        // Register Health Check
//        DropTokenApplicationHealthCheck healthCheck =
//                new DropTokenApplicationHealthCheck(dbi.onDemand(GetGamesResponseService.class));
//        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);
//
//        // Register OAuth authentication
//        environment.jersey()
//                .register(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<User>()
//                        .setAuthenticator(new DropTokenAuthenticator())
//                        .setAuthorizer(new DropTokenAuthorizer()).setPrefix(BEARER).buildAuthFilter()));
//        environment.jersey().register(RolesAllowedDynamicFeature.class);

        final DropTokenResource resource = new DropTokenResource(getGamesDao);
        environment.jersey().register(resource);

    }

}
