package insa.vulnerables.httpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import insa.vulnerables.user.AppUser;
import insa.vulnerables.user.Request;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public final class VulnerablesHttpClient {

    private static final String SERVER_URL = "http://127.0.0.1:8080/api";
    private static final String LOGIN_URL = "/login";
    private static final String REGISTER_URL = "/users/register";
    private static volatile VulnerablesHttpClient instance;
    private static String jSessionId;


    // singleton pattern to ensure we only have one instance of http client running
    private VulnerablesHttpClient() {
    }

    public static VulnerablesHttpClient getInstance() {
        VulnerablesHttpClient result = instance;
        if (result != null) {
            return result;
        }

        // synchronized block to ensure only one instance of http client is created
        synchronized (VulnerablesHttpClient.class) {
            if (instance == null) {
                instance = new VulnerablesHttpClient();
            }
            return instance;
        }
    }

    public boolean loginUser(String username, String password) {
        // these are needed when sending a POST request with multipart/form-data
        String boundary = UUID.randomUUID().toString();
        String lineSeparator = "\r\n";
        String twoHyphens = "--";

        try {
            URL url = new URL(SERVER_URL + LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {

                // username part of the request
                outputStream.writeBytes(twoHyphens + boundary + lineSeparator);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"username\"" + lineSeparator);
                outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineSeparator + lineSeparator);
                outputStream.writeBytes(username + lineSeparator);

                // password part of the request
                outputStream.writeBytes(twoHyphens + boundary + lineSeparator);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"password\"" + lineSeparator);
                outputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineSeparator + lineSeparator);
                outputStream.writeBytes(password + lineSeparator);

                // combine all
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineSeparator);
                outputStream.flush();
            }

            if (connection.getResponseCode() == 200) {
                // set the cookie header so user is authenticated each time they make a request
                String setCookieHeader = connection.getHeaderField("Set-Cookie");
                if (setCookieHeader != null) {
                    for (String cookie : setCookieHeader.split(";")) {
                        if (cookie.trim().startsWith("JSESSIONID")) {
                            jSessionId = cookie.trim();
                            break;
                        }
                    }
                }
                getUserDetails(username);
                return true;
            } else {
                System.out.println("Login failed. Status code: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public HttpResponse<String> sendGetRequest(String url) {
        String fullUrl = SERVER_URL + url;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder;

        try {
            requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .GET();

            // adds the cookie session ID to the request header if available
            if (jSessionId != null) {
                requestBuilder.header("Cookie", jSessionId);
            }
            HttpRequest request = requestBuilder.build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> sendPostRequest(String url) {
        String fullUrl = SERVER_URL + url;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder;

        try {
            requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .POST(HttpRequest.BodyPublishers.noBody());

            // adds the cookie session ID to the request header if available
            if (jSessionId != null) {
                requestBuilder.header("Cookie", jSessionId);
            }
            HttpRequest request = requestBuilder.build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpResponse<String> sendPutRequest(String url) {
        String fullUrl = SERVER_URL + url;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest.Builder requestBuilder;

        try {
            requestBuilder = HttpRequest.newBuilder()
                    .uri(new URI(fullUrl))
                    .PUT(HttpRequest.BodyPublishers.noBody());

            // adds the cookie session ID to the request header if available
            if (jSessionId != null) {
                requestBuilder.header("Cookie", jSessionId);
            }
            HttpRequest request = requestBuilder.build();

            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    This method sends a POST request to the server to create a new user which will be mapped to the RegisterUser entity
    in the Spring Boot application which will then create the user in the database
     */
    public boolean createUser(String firstName, String lastName, String username, String password, String role) {

        String allRoles = sendGetRequest("/role").body();

        ObjectMapper objectMapper = new ObjectMapper();

        List<Map<String, Object>> list;
        try {
            list = objectMapper.readValue(allRoles, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Map<String, Integer> roleMap = list.stream()
                .collect(Collectors.toMap(
                        item -> (String) item.get("roleName"),
                        item -> (Integer) item.get("roleId")
                ));

        Map<String, Object> formData = Map.of(
                "username", username,
                "firstName", firstName,
                "lastName", lastName,
                "password", password,
                "roleId", roleMap.get(role)
        );

        HttpURLConnection connection = null;
        try {
            URL url = new URL(SERVER_URL + REGISTER_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            String jsonInputString = objectMapper.writeValueAsString(formData);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int statusCode = connection.getResponseCode();
            if (statusCode == 200 || statusCode == 201) {
                return true;
            } else {
                System.out.println("Error: " + statusCode);
                System.out.println("Message: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }

    private void getUserDetails(String username) {
        HttpResponse<String> response = sendGetRequest("/users/username/" + username);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AppUser.setLoggedInUser(objectMapper.readValue(response.body(), AppUser.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean addRequest(String type, String name, String location, String message) {
        long requestTypeId;
        Map<String, Object> formData;
        if (Objects.equals(type, "request")) {
            requestTypeId = 1L; // requests have an ID of 1, whereas offers have an ID of 2
            if (AppUser.getLoggedInUser().getRole().roleId() == 1L) {
                formData = Map.of(
                        "requestTypeId", requestTypeId,
                        "appUserId", AppUser.getLoggedInUser().getUserId(),
                        "name", name,
                        "location", location,
                        "message", message
                );
            } else {
                throw new RuntimeException("Only logged in Vulnerable people can add a request.");
            }
        } else if (Objects.equals(type, "offer")) {
            requestTypeId = 2L;
            if (AppUser.getLoggedInUser().getRole().roleId() == 2L) {
                formData = Map.of(
                        "requestTypeId", requestTypeId,
                        "appUserId", AppUser.getLoggedInUser().getUserId(),
                        "name", name,
                        "location", location,
                        "message", message
                );
            } else {
                throw new RuntimeException("Only logged in volunteers can add an offer.");
            }
        } else {
            throw new RuntimeException("Invalid request type.");
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(SERVER_URL + "/requests/add");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            if (jSessionId != null) {
                connection.setRequestProperty("Cookie", jSessionId);
            }
            String jsonInputString = new ObjectMapper().writeValueAsString(formData);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Check the response
            int statusCode = connection.getResponseCode();
            if (statusCode == 200 || statusCode == 201) {
                return true;
            } else {
                System.out.println("Error: " + statusCode);
                System.out.println("Message: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Request> getUserRequests() {
        HttpResponse<String> response = sendGetRequest("/requests/user/" + AppUser.getLoggedInUser().getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), new TypeReference<List<Request>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getAllOtherOffers() {
        HttpResponse<String> response = sendGetRequest("/requests/all-active-apart-from-current-user/" + AppUser.getLoggedInUser().getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), new TypeReference<List<Request>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AppUser> getPendingUsers() {
        HttpResponse<String> response = sendGetRequest("/users/pending");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), new TypeReference<List<AppUser>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getPendingRequests() {
        HttpResponse<String> response = sendGetRequest("/requests/pending-requests-offers");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), new TypeReference<List<Request>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Request> getAllMyAcceptedRequests() {
        HttpResponse<String> response = sendGetRequest("/requests/all-accepted-in-progress/" + AppUser.getLoggedInUser().getUserId());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response.body(), new TypeReference<List<Request>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean changeUserStatus(Long userId, String status) {
        String url = "/users/change-user-status/" + userId + "/" + status;
        HttpResponse<String> response = sendPostRequest(url);
        return response.statusCode() == 200;
    }

    public boolean changeRequestStatus(Long requestId, String requestStatus) {
        String url = "/requests/change-request-status/" + requestId + "/" + requestStatus;
        HttpResponse<String> response = sendPostRequest(url);
        return response.statusCode() == 200;
    }

    public boolean acceptRequest(Long requestId) {
        String url = "/requests/accept/" + requestId + "/" + AppUser.getLoggedInUser().getUserId();
        HttpResponse<String> response = sendPutRequest(url);
        return response.statusCode() == 200;
    }

    public boolean rateRequest(Long requestId, int value) {
        String url = "/requests/rate/" + requestId + "/" + value;
        HttpResponse<String> response = sendPutRequest(url);
        return response.statusCode() == 200;
    }

    public boolean logout() {
        jSessionId = null;
        AppUser.setLoggedInUser(null);
        return true;
    }
}
