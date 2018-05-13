package lt.mif.ise.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import java.io.Serializable;

public class CardInformation implements Serializable {
    @SerializedName("number")
    @JsonProperty("number")
    public String CardNumber;

    @SerializedName("holder")
    @JsonProperty("holder")
    public String CardHolder;

    @SerializedName("exp_year")
    @JsonProperty("exp_year")
    public int ExpirationYear;

    @SerializedName("exp_month")
    @JsonProperty("exp_month")
    public int ExpirationMonth;

    @SerializedName("cvv")
    @JsonProperty("cvv")
    public String Cvv;
}
