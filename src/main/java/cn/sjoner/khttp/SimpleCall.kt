package cn.sjoner.khttp

import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class SimpleCall<T>:Call<T> {
    var call:Call<ResponseBody>
    var returnType:Type
    constructor(type: Type, call: Call<ResponseBody>) {
        this.call = call
        this.returnType = type
    }

    override fun clone(): Call<T> {
        return SimpleCall(returnType, call)
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

    override fun enqueue(callback: Callback<T>) {
        var cback = object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                callback.onFailure(this@SimpleCall,t)
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>) {
                var converter = HttpClient.instance().retrofit.builder.responseConverter
                var returnResp:Response<Any>?=null
                if (response.isSuccessful) {
                    var body = converter.convert(response.body(),returnType)
                    returnResp = Response.success(body, response.raw())
                }else{
                    var errorBody = response.errorBody()
                    returnResp = Response.error(response.code(),errorBody)
                }
                callback.onResponse(this@SimpleCall,returnResp as Response<T>)
            }

        }
        call.enqueue(cback)
    }

    override fun execute(): Response<T> {
        var response = call.execute()
        var converter = HttpClient.instance().retrofit.builder.responseConverter
        var returnResp:Response<Any>?=null
        if (response.isSuccessful) {
            var body = converter.convert(response.body(),returnType)
            returnResp = Response.success(body, response.raw())
        }else{
            var errorBody = response.errorBody()
            returnResp = Response.error(response.code(),errorBody)
        }
        return returnResp as Response<T>
    }
}