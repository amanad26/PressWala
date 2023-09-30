package com.abmtech.presswala.Apis;


import static com.abmtech.presswala.Apis.BaseUrls.APP_BLOCK_URL;
import static com.abmtech.presswala.Apis.BaseUrls.AddOrder;
import static com.abmtech.presswala.Apis.BaseUrls.app_block;
import static com.abmtech.presswala.Apis.BaseUrls.assignDeliveryBoy;
import static com.abmtech.presswala.Apis.BaseUrls.getAllOrder;
import static com.abmtech.presswala.Apis.BaseUrls.getOrder;
import static com.abmtech.presswala.Apis.BaseUrls.getOrderById;
import static com.abmtech.presswala.Apis.BaseUrls.updateOrder;
import static com.abmtech.presswala.Apis.BaseUrls.verifyCustomerBoyOtp;
import static com.abmtech.presswala.Apis.BaseUrls.verifyDeliveryBoyOtp;

import com.abmtech.presswala.Models.AppBlockModel;
import com.abmtech.presswala.Models.MyOrderModel;
import com.abmtech.presswala.Models.SingleOrderModel;
import com.abmtech.presswala.Models.UpdateModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST(AddOrder)
    Call<UpdateModel> addOrder(
            @Field("data") String data,
            @Field("userId") String userId,
            @Field("userName") String userName,
            @Field("userMobile") String userMobile,
            @Field("orderDate") String orderDate,
            @Field("time") String time,
            @Field("orderId") String orderId,
            @Field("totalPrice") String totalPrice
    );


    @FormUrlEncoded
    @POST(getOrder)
    Call<MyOrderModel> getMyOrders(
            @Field("user_id") String user_id
    );

    @GET(getAllOrder)
    Call<MyOrderModel> getAllOrders();

    @FormUrlEncoded
    @POST(updateOrder)
    Call<UpdateModel> updateStatus(
            @Field("orderId") String orderId,
            @Field("orderStatus") String orderStatus,
            @Field("orderDesc") String orderDesc
    );

    @FormUrlEncoded
    @POST(getOrderById)
    Call<SingleOrderModel> getOrderById(
            @Field("orderId") String orderId
    );

    @FormUrlEncoded
    @POST(verifyCustomerBoyOtp)
    Call<UpdateModel> verifyCustomerOtp(
            @Field("orderId") String orderId,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST(verifyDeliveryBoyOtp)
    Call<UpdateModel> verifyDeliveryOtp(
            @Field("orderId") String orderId,
            @Field("otp") String otp
    );


    @FormUrlEncoded
    @POST(assignDeliveryBoy)
    Call<UpdateModel> assignDeliveryBoy(
            @Field("order_id") String order_id,
            @Field("delivery_boy_name") String delivery_boy_name,
            @Field("delivery_boy_id") String delivery_boy_id,
            @Field("delivery_boy_mobile") String delivery_boy_mobile
    );

    @FormUrlEncoded
    @POST(app_block)
    Call<AppBlockModel> checkAppBlock(
            @Field("id") String id
    );


}
