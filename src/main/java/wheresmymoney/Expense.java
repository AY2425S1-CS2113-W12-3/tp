package wheresmymoney;

public class Expense {
    protected Float price;
    protected String description;
    protected String category;


    public Expense(Float price, String description, String category) {
        this.price = price;
        this.description = description;
        this.category = category;
    }

    
    public Float getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }
    public String getCategory() {
        return category;
    }
    public void setPrice(Float price) {
        this.price = price;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setCategory(String category) {
        this.category = category;
    }

}
