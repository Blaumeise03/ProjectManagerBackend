package de.blaumeise03.projectmanager;

import de.blaumeise03.projectmanager.data.baseData.ItemService;
import de.blaumeise03.projectmanager.data.baseData.PlayerService;
import de.blaumeise03.projectmanager.utils.POJOConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.function.Predicate;


@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public class BackendApplication {
    static ConfigurableApplicationContext ctx = null;


    /**
     * Main method to start the server.
     *
     * @param args ignored.
     */
    public static void main(String[] args) {
        ctx = SpringApplication.run(BackendApplication.class, args);
        POJOConverter.setPlayerService(ctx.getBean(PlayerService.class));
        POJOConverter.setItemService(ctx.getBean(ItemService.class));
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        loggingFilter.setHeaderPredicate(Predicate.isEqual("cookie"));
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }
}
