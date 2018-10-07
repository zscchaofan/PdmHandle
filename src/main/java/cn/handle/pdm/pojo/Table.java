package cn.handle.pdm.pojo;

import java.util.List;

public class Table {
    private String creationDate;
    private String comment;
    private String objectID;
    private String modificationDate;
    private String id;
    private String creator;
    private String code;
    private String modifier;
    private String totalSavingCurrency;
    private String name;
    private List<Key> keys;
    private List<Column> columns;
    private List<Key> primaryKey;


    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getTotalSavingCurrency() {
        return totalSavingCurrency;
    }

    public void setTotalSavingCurrency(String totalSavingCurrency) {
        this.totalSavingCurrency = totalSavingCurrency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Key> getKeys() {
        return keys;
    }

    public void setKeys(List<Key> keys) {
        this.keys = keys;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Key> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<Key> primaryKey) {
        this.primaryKey = primaryKey;
    }
}