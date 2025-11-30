package data.models;
import java.time.LocalDateTime;

public class ParkingViolation {
    private LocalDateTime timestamp;
    private double fine;
    private String description;
    private String vehicleId;
    private String state;
    private String violationId;
    private String zipCode;

    // constructor
    public ParkingViolation(LocalDateTime timestamp, double fine, String description,
                            String vehicleId, String state, String violationId, String zipCode) {
        this.timestamp = timestamp;
        this.fine = fine;
        this.description = description;
        this.vehicleId = vehicleId;
        this.state = state;
        this.violationId = violationId;
        this.zipCode = zipCode;
    }

    // getters & setters
    public LocalDateTime getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }

    public double getFine(){return fine;
    }
    public void setFine(double fine){
        this.fine = fine;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public String getVehicleId(){
        return vehicleId;
    }
    public void setVehicleId(String vehicleId){
        this.vehicleId = vehicleId;
    }

    public String getState(){
        return state;
    }
    public void setState(String state){
        this.state = state;
    }

    public String getViolationId(){
        return violationId;
    }
    public void setViolationId(String violationId){
        this.violationId = violationId;
    }

    public String getZipCode(){
        return zipCode;
    }
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "ParkingViolation{" +
                "timestamp=" + timestamp +
                ", fine=" + fine +
                ", description='" + description + '\'' +
                ", vehicleId='" + vehicleId + '\'' +
                ", state='" + state + '\'' +
                ", violationId='" + violationId + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
