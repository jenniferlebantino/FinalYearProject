package com.example.finalyearproject;

public class Upload {
    private String entityName;
    private String entityImageUrl;

    public Upload()
    {
        //Keep
    }

    public Upload(String name, String imageUrl)
    {
        if(name.trim().equals(""))
        {
            name = "No Name";
        }

        entityName = name;
        entityImageUrl = imageUrl;
    }

    public String getName()
    {
        return entityName;
    }

    public void setName(String name)
    {
        entityName = name;
    }

    public String getEntityImageUrl() {
        return entityImageUrl;
    }

    public void setEntityImageUrl(String entityImageUrl) {
        this.entityImageUrl = entityImageUrl;
    }

}
