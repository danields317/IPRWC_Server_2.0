package org.example;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import javax.validation.Valid;
import javax.validation.constraints.*;

public class IprwcServerConfiguration extends Configuration {
    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private String secret;

    @NotNull
    private String issuer;

    @NotNull
    private String productfolder;

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory factory) {
        this.database = factory;
    }

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("secret")
    public void setSecret(String secret){
        this.secret = secret;
    }

    @JsonProperty("secret")
    public String getSecret(){
        return secret;
    }

    @JsonProperty("issuer")
    public void setIssuer(String issuer) { this.issuer = issuer; }

    @JsonProperty("issuer")
    public String getIssuer() { return issuer; }

    @JsonProperty("productfolder")
    public String getProductFolder() { return productfolder; }

    @JsonProperty("productfolder")
    public void setProductfolder(String productFolder) { this.productfolder = productFolder; }
}
