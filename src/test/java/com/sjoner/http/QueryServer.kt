package com.sjoner.http

import com.sjoner.http.bean.KuaiDiQueryReq
import retrofit2.Call
import retrofit2.http.GET

interface QueryServer {
    @GET("/query")
    fun query(params: KuaiDiQueryReq): Call<String>
}

interface PostServer{
    fun post(x: Any): Unit
}
