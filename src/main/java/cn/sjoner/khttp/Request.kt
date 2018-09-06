package cn.sjoner.khttp

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class Request<F:Any,T:Any> {
    var url:String by Delegates.notNull()
    var method: HttpMethod = HttpMethod.GET
    var body: F? = null
    var headers: HashMap<String, String> = HashMap()

    internal var _success: (Response<T>?) -> Unit = {}

    internal var _fail:(Throwable?) ->Unit= {}

    internal var _complete:()->Unit = {}

    inline fun <reified T:Any> body(init: T.() -> Unit):T{
        val data = T::class.java.newInstance()
        data.init()
        return data
    }

    fun error(onError: (Throwable?) -> Unit) {
        _fail = onError
    }

    fun complete(onComplete:()->Unit) {
        _complete = onComplete
    }

    fun success(onSuccess: (Response<T>?) -> Unit) {
        _success = onSuccess
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

    fun headers(init: HashMap<String, String>.() -> Unit) {
        headers.init()
    }
}