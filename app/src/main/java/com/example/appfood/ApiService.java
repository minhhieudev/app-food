// ApiService.java
package com.example.appfood;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("frontend-api/customer-auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("frontend-api/customer-auth/google-login")
    Call<LoginResponse> googleLogin(@Body GoogleLoginRequest request);


    @GET("frontend-api/service-packages/customers")
    Call<FoodPackageResponse> getFoodPackages();

    @GET("frontend-api/meal/{orderID}")
    Call<MealResponse> getMeals(@Path("orderID") String orderID);

    @GET("frontend-api/service-packages/customers/{id}")
    Call<FoodPackageResponseDetail> getFoodPackageDetails(@Path("id") String packageId);

    @POST("frontend-api/customer-auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);


    @POST("frontend-api/orders")
    Call<OrderResponse> order(@Body OrderData request);

    @GET("frontend-api/orders/orderCustomer")
    Call<OrderCustomerResponse> getOrder();

    @POST("frontend-api/orders/get-summary")
    Call<SumMaRyResponse> getSumMary();

     // Lấy ví
    @GET("frontend-api/wallets")
    Call<WalletResponse> getWallet();

    // meal api
    @POST("frontend-api/meal/update-favorite-ingredients")
    Call<UpdateFavoriteIngredientsRespone> updateFavoriteIngredients(@Body UpdateFavoriteIngredientsRequest request);
//
    @POST("frontend-api/meal/update-delivery-time")
    Call<UpdateDeliveryTimeResponse> updateDeliveryTime(@Body UpdateDeliveryTimeRequest request);
//
    @POST("frontend-api/meal/review")
    Call<ReviewResponse> review(@Body AddReviewRequest request);
//
    @POST("frontend-api/meal/cancel")
    Call<CancelMealResponse> cancelMeal(@Body CancelMealRequest request);

    @POST("frontend-api/customer-auth/profile")
    Call<UserInfoUpdateResponse> updateUserInfo(@Body UserInfo request);



}
