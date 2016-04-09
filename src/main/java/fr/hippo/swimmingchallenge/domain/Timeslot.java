package fr.hippo.swimmingchallenge.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Timeslot.
 */
@Entity
@Table(name = "timeslot")
public class Timeslot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "start_time", nullable = false, updatable = false)
    private String startTime;

    @NotNull
    @Column(name = "end_time", nullable = false, updatable = false)
    private String endTime;

    @NotNull
    @Column(name = "payed", nullable = false)
    private Boolean payed;

    @NotNull
    @Column(name = "reserved", nullable = false)
    private Boolean reserved;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "swimmer1")
    private String swimmer1;

    @Column(name = "swimmer2")
    private String swimmer2;

    @Column(name = "swimmer3")
    private String swimmer3;

    @Column(name = "swimmer4")
    private String swimmer4;

    @Column(name = "reserved_date")
    private LocalDate reservedDate;

    @NotNull
    @Column(name = "line", nullable = false, updatable = false)
    private Integer line;

    @NotNull
    @Column(name = "version", nullable = false)
    @Version
    private Long version;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Boolean getPayed() {
        return payed;
    }

    public void setPayed(Boolean payed) {
        this.payed = payed;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getSwimmer1() {
        return swimmer1;
    }

    public void setSwimmer1(String swimmer1) {
        this.swimmer1 = swimmer1;
    }

    public String getSwimmer2() {
        return swimmer2;
    }

    public void setSwimmer2(String swimmer2) {
        this.swimmer2 = swimmer2;
    }

    public String getSwimmer3() {
        return swimmer3;
    }

    public void setSwimmer3(String swimmer3) {
        this.swimmer3 = swimmer3;
    }

    public String getSwimmer4() {
        return swimmer4;
    }

    public void setSwimmer4(String swimmer4) {
        this.swimmer4 = swimmer4;
    }

    public LocalDate getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(LocalDate reservedDate) {
        this.reservedDate = reservedDate;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Timeslot timeslot = (Timeslot) o;
        if(timeslot.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, timeslot.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Timeslot{" +
            "id=" + id +
            ", startTime='" + startTime + "'" +
            ", endTime='" + endTime + "'" +
            ", payed='" + payed + "'" +
            ", reserved='" + reserved + "'" +
            ", teamName='" + teamName + "'" +
            ", swimmer1='" + swimmer1 + "'" +
            ", swimmer2='" + swimmer2 + "'" +
            ", swimmer3='" + swimmer3 + "'" +
            ", swimmer4='" + swimmer4 + "'" +
            ", line='" + line + "'" +
            ", version='" + version + "'" +
            '}';
    }

    public void erase() {
        payed = false;
        reserved = false;
        teamName = null;
        swimmer1 = null;
        swimmer2 = null;
        swimmer3 = null;
        swimmer4 = null;
        user = null;
    }
}
