package kpop.kpopGeneration.security.oauth2.provider;

public interface Oauth2UserInfo {
    String getProviderId();
    String getProvider();
    String getEmail();
}
