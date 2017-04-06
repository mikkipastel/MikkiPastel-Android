
package co.meappdev.mikkipastel.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Replies {

    @SerializedName("totalitems")
    @Expose
    private String totalitems;
    @SerializedName("selfLink")
    @Expose
    private String selfLink;

    /**
     * 
     * @return
     *     The totalitems
     */
    public String getTotalitems() {
        return totalitems;
    }

    /**
     * 
     * @param totalitems
     *     The totalitems
     */
    public void setTotalitems(String totalitems) {
        this.totalitems = totalitems;
    }

    /**
     * 
     * @return
     *     The selfLink
     */
    public String getSelfLink() {
        return selfLink;
    }

    /**
     * 
     * @param selfLink
     *     The selfLink
     */
    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

}
