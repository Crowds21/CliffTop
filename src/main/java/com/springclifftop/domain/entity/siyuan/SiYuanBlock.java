package com.springclifftop.domain.entity.siyuan;

import com.alibaba.fastjson.annotation.JSONField;

public class SiYuanBlock {

    @JSONField(name = "id")
    private String id;
    @JSONField(name = "parent_id")
    private String parent_id;
    @JSONField(name = "root_id")
    private String root_id;
    @JSONField(name = "hash")
    private String hash;
    @JSONField(name = "box")
    private String box;
    @JSONField(name = "hpath")
    private String hpath;
    private String path;
    @JSONField(name = "name")
    private String name;
    @JSONField(name = "alias")
    private String alias;
    @JSONField(name = "memo")
    private String memo;
    @JSONField(name = "content")
    private String content;
    @JSONField(name = "markdown")
    private String markdown;
    @JSONField(name = "length")
    private String length;
    @JSONField(name = "type")
    private String type;
    @JSONField(name = "subtype")
    private String subtype;
    @JSONField(name = "ial")
    private String ial;
    @JSONField(name = "sort")
    private String sort;
    @JSONField(name = "created")
    private String created;
    @JSONField(name = "updated")
    private String updated;

    @Override
    public String toString() {
        return "main.entity.Block{" +
                "id='" + id + '\'' +
                ", parent_id='" + parent_id + '\'' +
                ", root_id='" + root_id + '\'' +
                ", hash='" + hash + '\'' +
                ", box='" + box + '\'' +
                ", hpath='" + hpath + '\'' +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", memo='" + memo + '\'' +
                ", content='" + content + '\'' +
                ", markdown='" + markdown + '\'' +
                ", length='" + length + '\'' +
                ", type='" + type + '\'' +
                ", subtype='" + subtype + '\'' +
                ", ial='" + ial + '\'' +
                ", sort='" + sort + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getBox() {
        return box;
    }

    public void setBox(String box) {
        this.box = box;
    }

    public String getHpath() {
        return hpath;
    }

    public void setHpath(String hpath) {
        this.hpath = hpath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getIal() {
        return ial;
    }

    public void setIal(String ial) {
        this.ial = ial;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
