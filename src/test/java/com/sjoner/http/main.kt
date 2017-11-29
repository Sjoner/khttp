package com.sjoner.http

import com.sjoner.http.bean.KuaiDiQueryReq
import com.sjoner.http.bean.QueryResp
import okhttp3.ResponseBody
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

        http<HashMap<String,String>, ResponseBody>{
            url = "/query"
            method = HttpMethod.GET
            body = body {
                put("type","yuantong")
                put("postid","11111111111")
            }
            headers{
                put("key","value")
            }
            success{response ->
                val responseBody = response?.body()
                println("responseBody = ${responseBody?.string()}")
            }

            error{t->
                t?.printStackTrace()
            }
        }
//        http<KuaiDiQueryReq, ResponseBody>{
//            url = "/query"
//            method = HttpMethod.GET
//            body = body {
//                type = "yuantong"
//                postid = "11111111111"
//            }
//            headers{
//                put("key","value")
//            }
//            success{response ->
//                val responseBody = response?.body()
//                println("responseBody = ${responseBody?.string()}")
//            }
//
//            error{t->
//                t?.printStackTrace()
//            }
//        }
        assertTrue(true)
    }

}

