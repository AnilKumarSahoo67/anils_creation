package com.example.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MultipleArticle implements Serializable {
   @SerializedName("articles")
   @Expose
   public List<Articles> articles;

   public List<Articles> getArticles() {
      return articles;
   }

   public void setArticles(List<Articles> articles) {
      this.articles = articles;
   }
}
