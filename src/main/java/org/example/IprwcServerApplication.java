package org.example;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.example.core.*;
import org.example.resources.AccountResource;
import org.example.resources.AuthenticationResource;
import org.example.resources.OrderResource;
import org.example.resources.ProductResource;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.jdbi.v3.core.Jdbi;

public class IprwcServerApplication extends Application<IprwcServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new IprwcServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "IprwcServer";
    }

    @Override
    public void initialize(final Bootstrap<IprwcServerConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(final IprwcServerConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        final Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

        JWTManager.getInstance(configuration.getSecret(), configuration.getIssuer());

        PasswordManager passwordManager = new PasswordManager();

        environment.jersey().register(new AuthDynamicFeature(
                new OAuthCredentialAuthFilter.Builder<JWTTokenPrincipal>()
                .setAuthenticator(new OauthAuthenticator(jdbi))
                .setAuthorizer(new OauthAuthorizer(jdbi))
                .setPrefix("Bearer")
                .buildAuthFilter()
        ));

        environment.jersey().register(RolesAllowedDynamicFeature.class);

        environment.jersey().register(new AccountResource(jdbi));
        environment.jersey().register(new AuthenticationResource(jdbi));
        environment.jersey().register(new ProductResource(jdbi));
        environment.jersey().register(new OrderResource(jdbi));
    }
}
