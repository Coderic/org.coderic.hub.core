package net.coderic.core.api.config;

import com.google.code.ssm.CacheFactory;
import com.google.code.ssm.config.AbstractSSMConfiguration;
import com.google.code.ssm.config.DefaultAddressProvider;
import com.google.code.ssm.providers.xmemcached.MemcacheClientFactoryImpl;
import com.google.code.ssm.providers.xmemcached.XMemcachedConfiguration;
import net.rubyeye.xmemcached.auth.AuthInfo;
import net.rubyeye.xmemcached.utils.AddrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MemCachierConfig extends AbstractSSMConfiguration {
    @Value("${memcached.servers}")
    private String servers;

    @Value("${memcached.username}")
    private String username;

    @Value("${memcached.password}")
    private String password;

    @Bean
    @Override
    public CacheFactory defaultMemcachedClient() {
        String serverString = servers.replace(",", " ");
        List<InetSocketAddress> servers = AddrUtil.getAddresses(serverString);
        AuthInfo authInfo = AuthInfo.plain(username, password);

        Map<InetSocketAddress, AuthInfo> authInfoMap =
                new HashMap<InetSocketAddress, AuthInfo>();
        for(InetSocketAddress server : servers) {
            authInfoMap.put(server, authInfo);
        }

        final XMemcachedConfiguration conf = new XMemcachedConfiguration();
        conf.setUseBinaryProtocol(true);
        conf.setAuthInfoMap(authInfoMap);
        conf.setUseBinaryProtocol(true);
        final CacheFactory cf = new CacheFactory();
        cf.setCacheClientFactory(new MemcacheClientFactoryImpl());
        cf.setAddressProvider(new DefaultAddressProvider(serverString));
        cf.setConfiguration(conf);
        return cf;
    }
}
