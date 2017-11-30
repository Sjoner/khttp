package com.sjoner.http

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import kotlin.properties.Delegates

open class HttpClient private constructor() {
    var retrofit:Retrofit by Delegates.notNull()

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
        val server = retrofit.create(IServer::class.java)
        var call: Call<ResponseBody>? = null
        val url = request.url
        val method = request.method
        val body = request.body
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
                        val isAccessible = field.isAccessible
                        field.isAccessible = true
                        val name = field.name
                        val value = field.get(body)
                        if (value != null) {
                            map.put(name,value as String)
                        }
                        field.isAccessible = isAccessible
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
