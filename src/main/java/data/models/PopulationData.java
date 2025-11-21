package parkanalysis.data.models;

public class PopulationData {
    private String zipCode;
    private int population;

    // constructor
    public PopulationData(String zipCode, int population){
        this.zipCode = zipCode;
        this.population = population;
    }

    // getters & setters
    public String getZipCode(){
        return zipCode;
    }
    public void setZipCode(String zipCode){
        this.zipCode = zipCode;
    }

    public int getPopulation(){
        return population;
    }
    public void setPopulation(int population){
        this.population = population;
    }

    @Override
    public String toString() {
        return "PopulationData{" +
                "zipCode='" + zipCode + '\'' +
                ", population=" + population +
                '}';
    }
}
