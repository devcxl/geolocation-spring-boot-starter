package cn.devcxl.location;

import cn.devcxl.location.properties.GeoLocationProperties;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author devcxl
 */
public class DownloadGeoLocationDatabase {

    private static final Logger log = LoggerFactory.getLogger(DownloadGeoLocationDatabase.class);
    private final GeoLocationProperties geoLocationProperties;
    private final RestTemplate restTemplate;
    public static final String DOWNLOAD_URL = "https://download.maxmind.com/geoip/databases/GeoLite2-City/download?suffix=tar.gz";

    public DownloadGeoLocationDatabase(GeoLocationProperties geoLocationProperties, RestTemplateBuilder restTemplateBuilder) {
        this.geoLocationProperties = geoLocationProperties;
        this.restTemplate = restTemplateBuilder.basicAuthentication(geoLocationProperties.getAccountId(), geoLocationProperties.getLicenseKey()).build();
    }

    public void downloadGeoLocation(Resource resource) {
        log.info("Downloading geolocation database from network");
        // 使用 RestTemplate 下载文件
        restTemplate.execute(DOWNLOAD_URL, HttpMethod.GET, null, clientHttpResponse -> {
            // 创建目标文件
            File file = resource.getFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
            }
            Path path = file.toPath();

            // 将下载的文件流复制到目标路径
            try (InputStream inputStream = clientHttpResponse.getBody();
                 BufferedInputStream bis = new BufferedInputStream(inputStream);
                 GzipCompressorInputStream gis = new GzipCompressorInputStream(bis);
                 TarArchiveInputStream tis = new TarArchiveInputStream(gis)
            ) {
                TarArchiveEntry entry;
                while ((entry = tis.getNextEntry()) != null) {
                    if (entry.getName().endsWith(".mmdb")) {
                        log.debug("{}", entry.getName());
                        try (OutputStream out = Files.newOutputStream(path)) {
                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = tis.read(buffer)) > 0) {
                                out.write(buffer, 0, len);
                            }
                        }
                    }
                }
            }
            return null;
        });
        log.info("Download completed！| {}", resource);
    }


}
