package com.sjoner.http

import com.sjoner.http.converter.DefaultConverterFactory
import com.sjoner.http.converter.DefaultRequestConverter
import com.sjoner.http.converter.DefaultResponseConverter
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                if (body != null) {
                    var fields = body.javaClass.declaredFields
                    for (field in fields) {
                        var name = field.name
                        var value = field.get(body) as String
                        map.put(name,value)
                    }
                }
                call = server.get(url, map)
            }
            else ->{
                if (body != null) {
                    call = server.post(url, body)
                } else {
                    call = server.post(url)
                }
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

inline fun initRetrofit(init: Retrofit.Builder.() -> Unit) {
    val builder = Retrofit.Builder()
    builder.requestConverter = DefaultRequestConverter()
    builder.responseConverter = DefaultResponseConverter()
    builder.init()
    builder.addConverterFactory(DefaultConverterFactory())

    HttpClient.instance().retrofit = createRetrofit(builder)
}