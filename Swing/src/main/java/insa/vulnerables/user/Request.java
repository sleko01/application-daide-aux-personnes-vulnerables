package insa.vulnerables.user;

public class Request {

    private Long requestId;
    private RequestType requestType;
    private AppUser appUser;
    private Status status;
    private int rating;
    private String name;
    private String location;
    private String message;
    private AppUser userAccepted;

    public record Status(int statusId, String statusName) {}
    public record RequestType(int requestTypeId, String requestTypeName) {}

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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

    public AppUser getUserAccepted() {
        return userAccepted;
    }

    public void setUserAccepted(AppUser userAccepted) {
        this.userAccepted = userAccepted;
    }
}
