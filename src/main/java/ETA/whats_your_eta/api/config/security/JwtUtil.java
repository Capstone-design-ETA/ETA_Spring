package ETA.whats_your_eta.api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
//    private final RefreshTokenService tokenService;
    private Key secretKey;


    // 시크릿 키 초기화
    @PostConstruct
    protected void init() {
        byte[] byteSecretKey = Base64.getEncoder().encode(jwtProperties.getSecret().getBytes());
        secretKey = new SecretKeySpec(byteSecretKey, "HmacSHA256");
    }

    // refreshToken & accessToken 생성
    public GeneratedToken generateToken(String email, String role) {
        //String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        return new GeneratedToken(accessToken);
    }

//    // RefreshToken 발급
//    public String generateRefreshToken(String email, String role) {
//        // 토큰의 유효 기간을 밀리초 단위로 설정
//        long refreshPeriod = 1000L * 60L * 60L * 24L * 14; // 2주
//
//        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한) 세팅
//        Claims claims = Jwts.claims().setSubject(email);
//        claims.put("role", role);
//
//        // 현재 시간과 날짜 가져옴
//        Date now = new Date();
//
//        return Jwts.builder()
//                // Payload를 구성하는 속성 정의
//                .setClaims(claims)
//                // 발행 일자
//                .setIssuedAt(now)
//                // 토큰 만료 일시 설정
//                .setExpiration(new Date(now.getTime() + refreshPeriod))
//                // 지정된 서명 알고리즘과 시크릿 키를 사용하여 토큰을 서명함
//                .signWith(secretKey)
//                .compact();
//    }

    // AccessToken 발급
    public String generateAccessToken(String email, String role) {
        long tokenPeriod = 1000L * 60L * 60L * 24L * 14; // 2주

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenPeriod))
                .signWith(secretKey)
                .compact();
    }

    // 토큰 검증
    public boolean verityToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build() // 시크릿 키를 설정하여 파싱
                    .parseClaimsJws(token); // 주어진 토큰을 파싱하여 Claims 객체 얻음
            // 토큰의 만료 시간과 현재 시간 비교
            return claims.getBody()
                    .getExpiration()
                    .after(new Date()); // 만료 시간이 현재 시간 이후인지 확인하여 유효성 검사 결과 반환
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰에서 이메일을 추출함
    public String getUid(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }
}
