package project.project;

public class UserCart {
    int idCart;
    String pesel;
    int idProduct;
    int amount;
    int fullPrice;
    String name;

    public UserCart(int idCart, String pesel, int idProduct, int amount) {
        this.idCart = idCart;
        this.pesel = pesel;
        this.idProduct = idProduct;
        this.amount = amount;
    }
    public UserCart(int amount, String name, int price) {
        this.name = name;
        this.amount = amount;
        this.fullPrice = price;
    }

    public int getIdCart() {
        return idCart;
    }

    public void setIdCart(int idCart) {
        this.idCart = idCart;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFullPrice() {
        return fullPrice;
    }

    public void setFullPrice(int fullPrice) {
        this.fullPrice = fullPrice;
    }
}
