package fr.insa.vulnerables.VulnerablesApplication.dto;

public class RegisterRequest {

    private Long requestTypeId;
    private Long appUserId;
    private String name;
    private String location;
    private String message;

    public RegisterRequest() {
    }

    public RegisterRequest(Long requestTypeId, Long appUserId, String name, String location, String message) {
        this.requestTypeId = requestTypeId;
        this.appUserId = appUserId;
        this.name = name;
        this.location = location;
        this.message = message;
    }

    public Long getRequestTypeId() {
        return requestTypeId;
    }

    public void setRequestTypeId(Long requestTypeId) {
        this.requestTypeId = requestTypeId;
    }

    public Long getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(Long appUserId) {
        this.appUserId = appUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
