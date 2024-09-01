package cn.devcxl.location;

import cn.devcxl.location.properties.GeoLocationProperties;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author devcxl
 */
@Configuration
@EnableConfigurationProperties({GeoLocationProperties.class})
public class GeoLocationAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(GeoLocationAutoConfiguration.class);

    @Bean
    DownloadGeoLocationDatabase downloadGeoLocationDatabase(GeoLocationProperties geoLocationProperties, RestTemplateBuilder restTemplateBuilder) {
        return new DownloadGeoLocationDatabase(geoLocationProperties, restTemplateBuilder);
    }


    @Bean
    DatabaseReader databaseReader(GeoLocationProperties geoLocationProperties, DownloadGeoLocationDatabase downloadGeoLocationDatabase) throws IOException, URISyntaxException {
        Resource resource = geoLocationProperties.getResource();
        if (!resource.exists()) {
            downloadGeoLocationDatabase.downloadGeoLocation(resource);
        }
        log.info("GeoLocationDatabase {}", resource);
        DatabaseReader.Builder builder = new DatabaseReader.Builder(resource.getFile());
        if (geoLocationProperties.isCache()) {
            builder.withCache(new CHMCache());
        }
        if (!geoLocationProperties.getLocals().isEmpty()) {
            builder.locales(geoLocationProperties.getLocals());
        }
        return builder.build();

    }


    @Bean
    GeoLocationService geoLocationService(DatabaseReader databaseReader) {
        return new GeoLocationService(databaseReader);
    }

}
