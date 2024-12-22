package fr.insa.vulnerables.VulnerablesApplication.domain;

import jakarta.persistence.*;

@Entity
@Table
public class Activity {

    @Id
    @GeneratedValue
    private Long activityId;

    @ManyToOne
    @JoinColumn(name = "requestId", nullable = false)
    private Request request;

    @ManyToOne
    @JoinColumn(name = "offerId", nullable = false)
    private Request offer;

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getOffer() {
        return offer;
    }

    public void setOffer(Request offer) {
        this.offer = offer;
    }
}
