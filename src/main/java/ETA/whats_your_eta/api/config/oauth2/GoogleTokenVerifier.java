package ETA.whats_your_eta.api.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Slf4j
@Service
public class GoogleTokenVerifier {

    public Map<String, Object> getGoogleUserInfo(String accessToken) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(userInfoEndpoint, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.error("Invalid Google Access Token: {}", response.getStatusCode());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google Access Token");
            }

            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("Client error while verifying Google token", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Google Access Token", e);
        } catch (RestClientException e) {
            log.error("Error communicating with Google API", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Google API error", e);
        }
    }
}
