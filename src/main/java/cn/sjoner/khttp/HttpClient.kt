package cn.sjoner.khttp

import retrofit2.Response
import retrofit2.Retrofit
import kotlin.properties.Delegates

open class HttpClient private constructor() {
    var retrofit:Retrofit by Delegates.notNull()

    companion object {
        private var instance: HttpClient?=null
        fun instance(): HttpClient {
            if (instance == null) {
                instance = HttpClient()
            }
            return instance!!
        }
    }

    inline fun <F:Any,reified T:Any> createCall(request: Request<F, T>):SimpleCall<T>{
        val server = retrofit.create(IServer::class.java)
        val url = request.url
        val method = request.method
        val body = request.body
        return when (method) {
            HttpMethod.GET ->{
                val map = retrofit.builder.queryMapConverter.convert(body,HashMap<String,String>().javaClass)
                val call = server.get(url, map,request.headers)
                SimpleCall(T::class.java,call,request)
            }
            else ->{
                val call = server.post(url, body,request.headers)
                SimpleCall(T::class.java,call,request)
            }
        }
    }
}

inline fun <F:Any,reified T:Any> http(init: Request<F, T>.() -> Unit){
    val request = Request<F, T>()
    request.init()
    val client = HttpClient.instance()
    val call = client.createCall(request)
    call.enqueue()
}

inline fun <F:Any,reified T:Any> syncHttp(init:Request<F,T>.()->Unit):Response<T>{
    val request = Request<F, T>()
    request.init()
    val client = HttpClient.instance()
    val call = client.createCall(request)
    return call.execute()
}
