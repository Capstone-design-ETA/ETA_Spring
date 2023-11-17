package ETA.whats_your_eta.api.config.oauth2;

import ETA.whats_your_eta.api.domain.user.Role;
import ETA.whats_your_eta.api.domain.user.User;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@ToString
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String provider;


    public static OAuthAttributes of(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(provider, userNameAttributeName, attributes);
            case "kakao":
                return ofKakao(provider, "email", attributes);
            default:
                throw new RuntimeException();
        }
    }


    private static OAuthAttributes ofGoogle(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .email((String) attributes.get("email"))
                .provider(provider)
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    private static OAuthAttributes ofKakao(String provider, String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) attributes.get("profile");

        return OAuthAttributes.builder()
                .email((String) kakaoAccount.get("email"))
                .provider(provider)
                .attributes(kakaoAccount)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //    // OAuth에서 엔티티를 생성하는 시점: 처음 가입할 때
//    public User toEntity() {
//        return User.builder()
//                .name(name)
//                .email(email)
//                .role(Role.USER)
//                .build();
    Map<String, Object> convertToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", nameAttributeKey);
        map.put("key", nameAttributeKey);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}