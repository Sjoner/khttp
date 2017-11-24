package com.sjoner.http

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class Request<F:Any,T:Any> {
    var url:String by Delegates.notNull()
    var method:HttpMethod = HttpMethod.GET
    var body: F? = null
    var connectTimeout:Long = 10

    internal var _success: (Response<T>?) -> Unit = {}

    internal var _fail:(Throwable?) ->Unit= {}

    internal var _complete:()->Unit = {}

    inline fun <reified T:Any> body(init: T.() -> Unit):T{
        var data = T::class.java.newInstance()
        data.init()
        return data
    }

    fun success(onSuccess: (Response<T>?) -> Unit) {
        _success = onSuccess
    }

    fun error(onError: (Throwable?) -> Unit) {
        _fail = onError
    }

    fun complete(onComplete:()->Unit) {
        _complete = onComplete
    }

    fun enqueue(call: SimpleCall<T>) {
        call.enqueue(object :Callback<T>{
            override fun onFailure(call: Call<T>?, t: Throwable?) {
                _fail(t)
                _complete()
            }

            override fun onResponse(call: Call<T>?, response: Response<T>?) {
                _success(response)
                _complete()
            }
        })
    }
}