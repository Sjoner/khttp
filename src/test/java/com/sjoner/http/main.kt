package com.sjoner.http

import com.sjoner.http.bean.KuaiDiQueryReq
import com.sjoner.http.bean.QueryResp
import com.sjoner.http.converter.DefaultResponseConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by linmq on 17-11-22.
 */

class Main{
    @Test fun main() {

        initRetrofit {
            baseUrl("http://www.kuaidi100.com")
            var okClient = OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build()
            client(okClient)
        }

        http<KuaiDiQueryReq, QueryResp>{
            url = "/query"
            method = HttpMethod.GET
            body = body {
                type = "yuantong"
                postid = "11111111111"
            }
            success{response ->
                val responseBody = response?.body()
                println("responseBody = ${responseBody?.message}")
            }

            error{
            }
        }

        assertTrue(true)
    }
}

