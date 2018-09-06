package cn.sjoner.khttp

import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class SimpleCall<T:Any>(private val returnType: Type,private val  call: Call<ResponseBody>,private val info: cn.sjoner.khttp.Request<*, T>):Call<T> {
    override fun clone(): Call<T> {
        return SimpleCall(returnType, call,info)
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    override fun cancel() {
        call.cancel()
    }

    override fun isExecuted(): Boolean {
        return call.isExecuted
    }

    override fun request(): Request {
        return call.request()
    }

    fun enqueue(){
        enqueue(object :Callback<T>{
            override fun onFailure(call: Call<T>?, t: Throwable?) {
                info._fail(t)
                info._complete()
            }

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                info._success(response)
                info._complete()
            }
        })
    }

    override fun enqueue(callback: Callback<T>) {
        val back = object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                callback.onFailure(this@SimpleCall,t)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                val converter = HttpClient.instance().retrofit.builder.responseConverter
                val returnResp = if (response.isSuccessful) {
                    val body = converter.convert(response.body(),returnType)
                    Response.success(body, response.raw())
                }else{
                    response
                }
                callback.onResponse(this@SimpleCall,returnResp as Response<T>)
            }

        }
        call.enqueue(back)
    }

    override fun execute(): Response<T> {
        val response = call.execute()
        val converter = HttpClient.instance().retrofit.builder.responseConverter
        val returnResp = if (response.isSuccessful) {
            val body = converter.convert(response.body(),returnType)
            Response.success(body, response.raw())
        }else{
            response
        }
        return returnResp as Response<T>
    }
}