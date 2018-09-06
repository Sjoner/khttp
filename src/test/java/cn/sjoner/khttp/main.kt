package cn.sjoner.khttp

import cn.sjoner.khttp.bean.KuaiDiQueryReq
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

/**
 * Created on 17-11-22.
 */

class Main{
    @Test fun main() {

        initRetrofit {
            baseUrl("http://www.kuaidi100.com")
            debug = true
        }

        val response = testHttp<KuaiDiQueryReq, ResponseBody> {
            url = "/query"
            method = HttpMethod.GET
            body = body {
                type = "yuantong"
                postid = "11111111111"
            }
            headers {
                put("key", "value")
            }
        }
        val responseBody = response.body()
        println("responseBody = ${responseBody?.string()}")

        assertEquals(4,2+2)
    }
}


inline fun <F:Any,reified T:Any> testHttp(init: Request<F, T>.() -> Unit): Response<T> {

    val request = Request<F, T>()

    request.init()

    val client = HttpClient.instance()
    val call = client.createCall(request)
    return call.execute()
}
