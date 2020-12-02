package com.tradegenie.android.tradegenieapp.Models;

import java.util.ArrayList;

public class AddSpecificationModel {
    String specification, type, specificationvalue;
    ArrayList<AttributeTypeModel> AttributeTypeModelList;
    boolean shouldAddToJsonArray = true;

    public boolean isShouldAddToJsonArray() {
        return shouldAddToJsonArray;
    }

    public void setShouldAddToJsonArray(boolean shouldAddToJsonArray) {
        this.shouldAddToJsonArray = shouldAddToJsonArray;
    }

    public ArrayList<AttributeTypeModel> getAttributeTypeModelList() {
        return AttributeTypeModelList;
    }

    public void setAttributeTypeModelList(ArrayList<AttributeTypeModel> attributeTypeModelList) {
        AttributeTypeModelList = attributeTypeModelList;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSpecificationvalue() {
        return specificationvalue;
    }

    public void setSpecificationvalue(String specificationvalue) {
        this.specificationvalue = specificationvalue;
    }
}
