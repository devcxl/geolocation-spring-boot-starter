package cn.devcxl.location;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import com.maxmind.geoip2.model.IspResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

/**
 * 获取地理位置信息
 *
 * @author devcxl
 */
public class GeoLocationService {

    private final DatabaseReader databaseReader;

    public GeoLocationService(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public CityResponse city(String ip) throws GeoIp2Exception, IOException {
        InetAddress ipAddress = InetAddress.getByName(ip);
        return databaseReader.city(ipAddress);
    }

    public CountryResponse country(String ip) throws GeoIp2Exception, IOException {
        InetAddress ipAddress = InetAddress.getByName(ip);
        return databaseReader.country(ipAddress);
    }

    public IspResponse isp(String ip) throws GeoIp2Exception, IOException {
        InetAddress ipAddress = InetAddress.getByName(ip);
        return databaseReader.isp(ipAddress);
    }

}
