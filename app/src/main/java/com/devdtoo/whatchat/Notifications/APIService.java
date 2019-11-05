package com.devdtoo.whatchat.Notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA_1K_0pI:APA91bH9Qob_wJgiegkSrCptT1w6q54o0S0w4PYF2wjgM7tk3S1T1QsHzj56pNGti7eSMomDJ0WOHa43TZHAGIX5IiSHkNPj_fmu-5CfzHVrh-Huq91TBiTqRHb6unJIB3whT18tDDoB"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

}
