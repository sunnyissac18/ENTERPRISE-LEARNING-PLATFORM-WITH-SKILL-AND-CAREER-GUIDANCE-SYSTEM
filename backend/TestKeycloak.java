package test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import java.util.List;
import java.util.Map;

public class TestKeycloak {
    public static void main(String[] args) {
        String keycloakUrl = "http://localhost:8081";
        String realm = "skillsphere";
        String clientId = "employee-sync";
        String clientSecret = "mcOBK6GCW2vdLE5UMFJMJGNyewEFTznY";

        RestClient restClient = RestClient.create();
        
        // 1. Get Token
        String tokenUrl = keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        Map response = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(Map.class);
                
        String token = (String) response.get("access_token");
        System.out.println("Got token.");

        // 2. Get Users
        List users = restClient.get()
                .uri(keycloakUrl + "/admin/realms/" + realm + "/users")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .body(List.class);
                
        System.out.println("Fetched " + users.size() + " users.");
        for (Object obj : users) {
            Map user = (Map) obj;
            System.out.println(user.get("username"));
        }
    }
}
