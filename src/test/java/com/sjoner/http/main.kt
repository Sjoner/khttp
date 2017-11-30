package com.sjoner.http

import com.sjoner.http.bean.KuaiDiQueryReq
import okhttp3.ResponseBody
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

/**
 * Created by linmq on 17-11-22.
 */

class Main{
    @Test fun main() {

        initRetrofit {
            baseUrl("http://www.kuaidi100.com")
            queryMapConverter
            debug = true
        }
//
//        val response = testHttp<HashMap<String, String>, ResponseBody> {
//            url = "/query"
//            method = HttpMethod.GET
//            body = body {
//                put("type", "yuantong")
//                put("postid", "11111111111")
//            }
//            headers {
//                put("key", "value")
//            }
//        }
//        val responseBody = response?.body()
//        println("responseBody = ${responseBody?.string()}")


        val response = testHttp<KuaiDiQueryReq, ResponseBody>{
            url = "/query"
            method = HttpMethod.GET
            body = body {
                type = "yuantong"
                postid = "11111111111"
            }
            headers{
                put("key","value")
            }
        }
        val responseBody = response?.body()
        println("responseBody = ${responseBody?.string()}")

        assertTrue(true)
    }

}


inline fun <F:Any,reified T:Any> testHttp(init: Request<F,T>.() -> Unit): Response<T> {

    val request = Request<F,T>()

    request.init()

    val client = HttpClient.instance()
    val call = client.createCall(request)
    val simpleCall = SimpleCall<T>(T::class.java,call)
    return simpleCall.execute()
}
