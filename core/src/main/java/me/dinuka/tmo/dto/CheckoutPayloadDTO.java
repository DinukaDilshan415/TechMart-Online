package me.dinuka.tmo.dto;

import com.google.gson.JsonObject;
import me.dinuka.tmo.entity.GiftAddress;
import me.dinuka.tmo.entity.User;
import me.dinuka.tmo.entity.UserAddress;

public class CheckoutPayloadDTO implements java.io.Serializable{
    private UserAddress userAddress;
    private GiftAddress giftAddress;
    private User user;

    public CheckoutPayloadDTO() {
    }

    public CheckoutPayloadDTO(UserAddress userAddress, GiftAddress giftAddress, User user) {
        this.userAddress = userAddress;
        this.giftAddress = giftAddress;
        this.user = user;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public GiftAddress getGiftAddress() {
        return giftAddress;
    }

    public void setGiftAddress(GiftAddress giftAddress) {
        this.giftAddress = giftAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
