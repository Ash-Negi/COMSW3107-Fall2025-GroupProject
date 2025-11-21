package parkanalysis.data.models;

public class Property {
    private double marketValue;
    private double totalLivableArea;
    private String zipCode;

    // constructor
    public Property(double marketValue, double totalLivableArea, String zipCode){
        this.marketValue = marketValue;
        this.totalLivableArea = totalLivableArea;
        this.zipCode = zipCode;
    }

    // getters & setters
    public double getMarketValue(){
        return marketValue;
    }
    public void setMarketValue(double marketValue){
        this.marketValue = marketValue;
    }

    public double getTotalLivableArea(){
        return totalLivableArea;
    }
    public void setTotalLivableArea(double totalLivableArea){
        this.totalLivableArea = totalLivableArea;
    }

    public String getZipCode(){
        return zipCode;
    }
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return "Property{" +
                "marketValue=" + marketValue +
                ", totalLivableArea=" + totalLivableArea +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
