package com.sjoner.http

import com.sjoner.http.bean.KuaiDiQueryReq
import com.sjoner.http.bean.QueryResp
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Created by linmq on 17-11-22.
 */

class Main{
    @Test fun main() {

        initRetrofit {
            baseUrl("http://www.kuaidi100.com")
            debug = true
        }

        http<KuaiDiQueryReq, QueryResp>{
            url = "/query"
            method = HttpMethod.GET
            body = body {
                type = "yuantong"
                postid = "11111111111"
            }
            headers{
                put("key","value")
            }
            success{response ->
                val responseBody = response?.body()
                println("responseBody = ${responseBody?.message}")
            }

            error{t->
                t?.printStackTrace()
            }
        }

        assertTrue(true)
    }

}

