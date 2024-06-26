package project.project;

public class Product {
    public int idProduct;
    public String productName;
    public int productPrice;
    public String productDescription;
    public int productAmount;

    public Product(int idProduct, String productName, int productPrice, String productDescription, int productAmount) {
        this.idProduct = idProduct;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.productAmount = productAmount;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }
}
