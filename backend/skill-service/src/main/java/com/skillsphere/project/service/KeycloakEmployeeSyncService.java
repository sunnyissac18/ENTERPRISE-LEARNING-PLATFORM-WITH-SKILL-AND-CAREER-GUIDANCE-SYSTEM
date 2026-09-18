package com.skillsphere.project.service;

import com.skillsphere.project.entity.Employee;
import com.skillsphere.project.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KeycloakEmployeeSyncService {

    private final EmployeeRepository employeeRepository;

    @Value("${keycloak.url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.sync-client-id}")
    private String clientId;

    @Value("${keycloak.sync-client-secret}")
    private String clientSecret;

    private final RestClient restClient = RestClient.create();

    public void syncUsers() {

        String token = getAccessToken();

        List<Map<String, Object>> users =
                restClient.get()
                        .uri(keycloakUrl
                                + "/admin/realms/"
                                + realm
                                + "/users")
                        .headers(headers ->
                                headers.setBearerAuth(token))
                        .retrieve()
                        .body(List.class);

        if (users == null) {
            return;
        }

        for (Map<String, Object> user : users) {

            String keycloakId = (String) user.get("id");

            if (keycloakId == null) {
                continue;
            }

            String firstName =
                    (String) user.getOrDefault("firstName", "");

            String lastName =
                    (String) user.getOrDefault("lastName", "");

            String username =
                    (String) user.getOrDefault("username", "");

            String fullName =
                    (firstName + " " + lastName).trim();

            if (fullName.isBlank()) {
                fullName = username;
            }

            // Extract custom attributes from Keycloak
            Map<String, List<String>> attributes = (Map<String, List<String>>) user.get("attributes");
            
            String dept = "Unassigned";
            String roleStr = "DEVELOPER";

            if (attributes != null) {
                if (attributes.containsKey("department") && !attributes.get("department").isEmpty()) {
                    dept = attributes.get("department").get(0);
                }
                if (attributes.containsKey("role") && !attributes.get("role").isEmpty()) {
                    roleStr = attributes.get("role").get(0).toUpperCase();
                }
            }

            Employee.Role roleEnum;
            try {
                roleEnum = Employee.Role.valueOf(roleStr);
            } catch (Exception e) {
                roleEnum = Employee.Role.DEVELOPER; // fallback
            }

            Employee employee = employeeRepository.findByKeycloakId(keycloakId).orElse(null);
            if (employee == null) {
                employee = Employee.builder()
                        .keycloakId(keycloakId)
                        .fullName(fullName)
                        .role(roleEnum)
                        .dept(dept)
                        .build();
            } else {
                employee.setFullName(fullName);
                // Only update if not already set or if explicitly set in Keycloak attributes
                if (attributes != null && attributes.containsKey("department")) {
                     employee.setDept(dept);
                }
                if (attributes != null && attributes.containsKey("role")) {
                     employee.setRole(roleEnum);
                }
                // If it's empty, give it a default so it doesn't show N/A
                if (employee.getDept() == null || employee.getDept().isBlank() || employee.getDept().equals("N/A")) {
                    employee.setDept("Engineering");
                }
                if (employee.getRole() == null) {
                    employee.setRole(Employee.Role.DEVELOPER);
                }
            }

            employeeRepository.save(employee);
        }
    }

    private String getAccessToken() {

        String tokenUrl =
                keycloakUrl
                        + "/realms/"
                        + realm
                        + "/protocol/openid-connect/token";

        MultiValueMap<String, String> formData =
                new LinkedMultiValueMap<>();

        formData.add("grant_type", "client_credentials");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);

        Map<String, Object> response =
                restClient.post()
                        .uri(tokenUrl)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(formData)
                        .retrieve()
                        .body(Map.class);

        if (response == null || response.get("access_token") == null) {
            throw new IllegalStateException(
                    "Unable to obtain Keycloak access token"
            );
        }

        return (String) response.get("access_token");
    }
}
