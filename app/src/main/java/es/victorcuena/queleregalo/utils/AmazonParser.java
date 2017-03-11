package es.victorcuena.queleregalo.utils;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import es.victorcuena.queleregalo.models.AmazonAnswer;
import es.victorcuena.queleregalo.models.AmazonProduct;

public class AmazonParser extends DefaultHandler {


    /**
     * ---------------------  Search TAGS ---------------------
     */
    private static final String KEY_MORE_RESULTS_URL = "MoreSearchResultsUrl";
    private static final String KEY_ITEM = "Item";
    private static final String KEY_ITEM_URL = "DetailPageURL";
    private static final String KEY_IMAGE_ROOT = "MediumImage";
    private static final String KEY_IMAGE_CONTAINER = "URL";
    private static final String KEY_ITEM_ATTR_TITLE = "Title";
    private static final String KEY_ITEM_ATTR_PRICE = "ListPrice";
    private static final String KEY_ITEM_ATTR_PRICE_AMOUNT = "Amount";
    private static final String KEY_ITEM_ATTR_PRICE_AMOUNT_FORMATTED = "FormattedPrice";


    private List<AmazonProduct> items;
    private String moreProductsUrl;
    private AmazonProduct currentProduct;

    private StringBuilder sbText; //Text acumulated till ending label

    private boolean isTagImage = false;
    private boolean isTagListPrice = false;

    public static AmazonAnswer parse(String xml) {
        SAXParserFactory factory = SAXParserFactory.newInstance();


        Log.e("XML", xml);

        try {
            SAXParser parser = factory.newSAXParser();
            AmazonParser handler = new AmazonParser();
            parser.parse(new InputSource(new StringReader(xml)), handler);

            return new AmazonAnswer(handler.getItems(), handler.getMoreProductsUrl());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<AmazonProduct> getItems() {
        return items;
    }

    public String getMoreProductsUrl() {
        return moreProductsUrl;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        super.characters(ch, start, length);

        if (this.currentProduct != null)
            sbText.append(ch, start, length);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        items = new ArrayList<>();
        sbText = new StringBuilder();

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equals(KEY_ITEM)) {
            currentProduct = new AmazonProduct();
        }


    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);

        if (localName.equals(KEY_MORE_RESULTS_URL)) {
            moreProductsUrl = sbText.toString();
        }


        if (currentProduct != null) {

            if (localName.equals(KEY_ITEM_URL)) {
                currentProduct.setHyperlink(sbText.toString());
            }

            //Getting image
            else if (localName.equals(KEY_IMAGE_ROOT)) {
                isTagImage = true;
            } else if (localName.equals(KEY_IMAGE_CONTAINER) && isTagImage) {

                currentProduct.setUrlImage(sbText.toString());
                isTagImage = false;

            } else if (localName.equals(KEY_ITEM_ATTR_PRICE)) {
                isTagListPrice = true;
            } else if (localName.equals(KEY_ITEM_ATTR_PRICE_AMOUNT) && isTagListPrice) {
                currentProduct.setPrice(sbText.toString());
            } else if (localName.equals(KEY_ITEM_ATTR_PRICE_AMOUNT_FORMATTED) && isTagListPrice) {
                currentProduct.setFormatedPrice(sbText.toString());
                isTagListPrice = false;

            } else if (localName.equals(KEY_ITEM_ATTR_TITLE)) {
                currentProduct.setTitle(sbText.toString());
            } else if (localName.equals(KEY_ITEM)) {
                items.add(currentProduct);
            }

            //Restart the string builder
            sbText.setLength(0);


        }

    }
}