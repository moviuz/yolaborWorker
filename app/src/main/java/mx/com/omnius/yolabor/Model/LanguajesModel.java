package mx.com.omnius.yolabor.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UDIaz on 06/02/18.
 */

public class LanguajesModel {


    public String getIdCatalog() {
        return idCatalog;
    }

    public void setIdCatalog(String idCatalog) {
        this.idCatalog = idCatalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @SerializedName("idCatalog")
    private String idCatalog;

    @SerializedName("name")
    private String name;

    @SerializedName("order")
    private String order;

    @SerializedName("idCategory")
    private String idCategory;

    @SerializedName("category")
    private String category;

}
