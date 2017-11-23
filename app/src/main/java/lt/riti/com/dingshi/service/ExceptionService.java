package lt.riti.com.dingshi.service;

import lt.riti.com.dingshi.entity.ResultCode;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by brander on 2017/11/13.
 */

public interface ExceptionService {
    @POST("rfidException.do")
    @FormUrlEncoded
    Call<ResultCode<String>> call(@Field("exception") String exception);
}