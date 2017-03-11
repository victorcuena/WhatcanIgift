package es.victorcuena.queleregalo.models;

/**
 * Created by victorcuenagarcia on 22/02/2017.
 */

public class AmazonProduct {

    private String title;
    private String urlImage;
    private String price;
    private String formatedPrice;
    private String hyperlink;


    public AmazonProduct() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFormatedPrice() {
        return formatedPrice;
    }

    public void setFormatedPrice(String formatedPrice) {
        this.formatedPrice = formatedPrice;
    }

    public String getHyperlink() {
        return hyperlink;
    }

    public void setHyperlink(String hyperlink) {
        this.hyperlink = hyperlink;
    }


    @Override
    public String toString() {
        return "AmazonProduct{" +
                "title='" + title + '\'' +
                ", urlImage='" + urlImage + '\'' +
                ", price='" + price + '\'' +
                ", formatedPrice='" + formatedPrice + '\'' +
                ", hyperlink='" + hyperlink + '\'' +
                '}';
    }
}
