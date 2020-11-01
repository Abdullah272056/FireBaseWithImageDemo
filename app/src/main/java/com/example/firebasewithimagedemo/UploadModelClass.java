package com.example.firebasewithimagedemo;

public class UploadModelClass {
    String imageName;
    String imageUri;

    public UploadModelClass() {
    }

    public UploadModelClass(String imageName, String imageUri) {
        this.imageName = imageName;
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
