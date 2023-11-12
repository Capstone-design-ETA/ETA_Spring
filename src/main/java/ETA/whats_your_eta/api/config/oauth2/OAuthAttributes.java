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
                return ofGoogle(userNameAttributeName, attributes);
            case "kakao":
                return ofKakao("email", attributes);
            default:
                throw new RuntimeException();
        }
    }


    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> account = (Map<String, Object>) attributes.get("profile");

        return OAuthAttributes.builder()
                .name((String) account.get("nickname"))
                .email((String) response.get("email"))
                .attributes(response)
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
        map.put("name", name);
        map.put("email", email);
        map.put("provider", provider);

        return map;
    }
}
