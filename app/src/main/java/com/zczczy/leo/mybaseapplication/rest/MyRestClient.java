package com.zczczy.leo.mybaseapplication.rest;

import com.zczczy.leo.mybaseapplication.model.BaseModelJson;
import com.zczczy.leo.mybaseapplication.model.PagerResult;
import com.zczczy.leo.mybaseapplication.model.TestModel;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientErrorHandling;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.androidannotations.rest.spring.api.RestClientRootUrl;
import org.androidannotations.rest.spring.api.RestClientSupport;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

/**
 * Created by Leo on 2016/3/2.
 * http://wcapia.zczczy.com/
 * http://218.61.203.50:8018/
 */
@Rest(rootUrl = "http://wcapia.zczczy.com/", requestFactory = MyOkHttpClientHttpRequestFactory.class, interceptors = {MyInterceptor.class},
        converters = {StringHttpMessageConverter.class, GsonHttpMessageConverter.class, FormHttpMessageConverter.class, ByteArrayHttpMessageConverter.class},
        responseErrorHandler = MyResponseErrorHandlerBean.class)
public interface MyRestClient extends RestClientRootUrl, RestClientSupport, RestClientHeaders, RestClientErrorHandling {

    /**
     * 查询推荐商品
     *
     * @param PageIndex 当前页面
     * @param PageSize  页面大小
     * @return
     */
    @Get("api/Content/GetRecommendedGoods?PageIndex={PageIndex}&PageSize={PageSize}")
    BaseModelJson<PagerResult<TestModel>> getRecommendedGoods(@Path int PageIndex, @Path int PageSize);


    @Get("api/Content/GetGoodsInfoLikeWord?PageIndex={PageIndex}&PageSize={PageSize}&SearchWord={SearchWord}&OB={OB}")
    BaseModelJson<PagerResult<TestModel>> getGoodsInfoLikeWord(@Path int PageIndex, @Path int PageSize, @Path String SearchWord, @Path String OB);


}

