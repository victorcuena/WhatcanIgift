package es.victorcuena.queleregalo.models;

import java.util.List;


public class AmazonAnswer {

    private List<AmazonProduct> products;
    private String urlMoreProducts;

    public AmazonAnswer(List<AmazonProduct> products, String urlMoreProducts) {
        this.products = products;
        this.urlMoreProducts = urlMoreProducts;
    }


    public List<AmazonProduct> getProducts() {
        return products;
    }

    public String getUrlMoreProducts() {
        return urlMoreProducts;
    }

    @Override
    public String toString() {
        return "AmazonAnswer{" +
                "products=" + products +
                ", urlMoreProducts='" + urlMoreProducts + '\'' +
                '}';
    }
}
