package de.blaumeise03.projectmanager;

import de.blaumeise03.projectmanager.accounting.PlayerService;
import de.blaumeise03.projectmanager.utils.POJOConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;


@EntityScan
@EnableJpaRepositories
@SpringBootApplication
public class BackendApplication {
//DROP TABLE IF EXISTS corps, items, prices, users, transactions;
    static ConfigurableApplicationContext ctx = null;
    public static void main(String[] args) {
        ctx = SpringApplication.run(BackendApplication.class, args);
        POJOConverter.setService(ctx.getBean(PlayerService.class));
    }

    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setMaxPayloadLength(64000);
        return loggingFilter;
    }
}
