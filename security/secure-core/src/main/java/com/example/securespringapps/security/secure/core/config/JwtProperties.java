package com.example.securespringapps.security.secure.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@ConfigurationProperties(prefix = "jwt")
@Component
@Data
public class JwtProperties {

    private Secret secret;
    private Access access;
    private Refresh refresh;

    @Data
    public static class Secret {
        private Key key;

        @Data
        public static class Key {
            private String access;
            private String refresh;
        }
    }

    @Data
    public static class Access {
        private Validity validity;

        @Data
        public static class Validity {
            private int seconds;
        }
    }

    @Data
    public static class Refresh {
        private Validity validity;

        @Data
        public static class Validity {
            private int seconds;
        }
    }
}
