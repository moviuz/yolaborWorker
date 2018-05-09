package mx.com.omnius.yolabor.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by David on 04/05/18.
 */

public class SkillModel {public String names;
    public String comments;
    public boolean swiped = false;


    public SkillModel() {
    }

    public SkillModel( String names, String comments) {
        this.names = names;
        this.comments = comments;
    }








    public String getIdSkill() {
        return idSkill;
    }

    public void setIdSkill(String idSkill) {
        this.idSkill = idSkill;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @SerializedName("idSkill")
    private String idSkill;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("order")
    private String order;

    @SerializedName("idCategory")
    private String idCategory;

    @SerializedName("category")
    private String category;
}