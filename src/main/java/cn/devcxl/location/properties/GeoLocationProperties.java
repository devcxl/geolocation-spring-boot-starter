package cn.devcxl.location.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @author devcxl
 */
@ConfigurationProperties(prefix = "geolocation")
public class GeoLocationProperties {
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 许可证
     */
    private String licenseKey;
    /**
     * 文件位置
     */
    private Resource resource = new PathResource("file:/tmp/geolocation.mmdb");
    /**
     * 是否开启缓存
     */
    private boolean cache = true;
    /**
     * 展示的语言
     */
    private List<String> locals;

    public Resource getResource() {

        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public boolean isCache() {
        return cache;
    }

    public void setCache(boolean cache) {
        this.cache = cache;
    }

    public List<String> getLocals() {
        return locals;
    }

    public void setLocals(List<String> locals) {
        this.locals = locals;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }
}
