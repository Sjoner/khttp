package com.sjoner.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import kotlin.properties.Delegates

open class HttpClient {
    var retrofit:Retrofit by Delegates.notNull()
    private constructor()
    companion object {
        private var instance:HttpClient?=null
        fun instance():HttpClient{
            if (instance == null) {
                instance = HttpClient()
            }
            return instance!!
        }
    }

    fun <F:Any,T:Any>createCall(request: Request<F,T>):Call<ResponseBody>{
        var server = retrofit.create(IServer::class.java)
        var call: Call<ResponseBody>? = null
        var url = request.url
        var method = request.method
        var body = request.body
        when (method) {
            HttpMethod.GET->{
                var map = HashMap<String, String>()
                if (body is Map<*, *>) {
                    body.map {entry ->
                        map.put(entry.key.toString(),entry.value.toString())
                    }
                }else if (body != null) {
                    var fields = body.javaClass.declaredFields
                    for (field in fields) {
                        var name = field.name
                        var value = field.get(body) as String
                        map.put(name,value)
                    }
                }
                call = server.get(url, map,request.headers)
            }
            else ->{
                call = server.post(url, body?:Any(),request.headers)
            }
        }
        return call
    }
}

inline fun <F:Any,reified T:Any> http(init: Request<F,T>.() -> Unit){

    val request = Request<F,T>()

    request.init()

    val client = HttpClient.instance()
    val call = client.createCall(request)
    val simpleCall = SimpleCall<T>(T::class.java,call)

    request.enqueue(simpleCall)
}
