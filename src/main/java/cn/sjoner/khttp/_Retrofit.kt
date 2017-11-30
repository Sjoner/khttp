package cn.sjoner.khttp

import cn.sjoner.khttp.converter.*
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

var Retrofit.builder:Retrofit.Builder by Delegates.notNull()

var Retrofit.Builder.responseConverter: Converter<ResponseBody?, Any> by Delegates.notNull()

var Retrofit.Builder.requestConverter: Converter<Any?, RequestBody> by Delegates.notNull()

var Retrofit.Builder.queryMapConverter: Converter<Any?, Map<String, String>> by Delegates.notNull()

var Retrofit.Builder.debug:Boolean by Delegates.notNull()

var Retrofit.Builder.okBuilder:OkHttpClient.Builder by Delegates.notNull()

private var Retrofit.Builder._connectTimeout:Long by Delegates.notNull()
fun Retrofit.Builder.connectTimeout(timeout: Long) {
    _connectTimeout = timeout
    okBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
}
fun Retrofit.Builder.connectTimeout():Long{
    return _connectTimeout
}


private var Retrofit.Builder._logger:(String)->Unit by Delegates.notNull()

fun Retrofit.Builder.logger(logger: (String) -> Unit) {
    _logger = logger
}

fun Retrofit.Builder.logger(): (String) -> Unit {
    return _logger
}

fun Retrofit.Builder.okBuilder(init: OkHttpClient.Builder.() -> Unit) {
    okBuilder.init()
}


inline fun initRetrofit(init: Retrofit.Builder.() -> Unit) {
    val builder = Retrofit.Builder()
    builder.debug = true
    builder.requestConverter = DefaultRequestConverter()
    builder.responseConverter = DefaultResponseConverter()
    builder.queryMapConverter = DefaultQueryMapConverter()
    builder.okBuilder = OkHttpClient.Builder()

    builder.logger {message ->
        HttpLoggingInterceptor.Logger.DEFAULT.log(message)
    }
    builder.init()

    if (builder.debug) {
        var interceptor = HttpLoggingInterceptor(builder.logger()).setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.okBuilder {
            addInterceptor(interceptor)
        }
    }
    builder.client(builder.okBuilder.build())

    builder.addConverterFactory(DefaultConverterFactory())
    HttpClient.instance().retrofit = builder.build()
    HttpClient.instance().retrofit.builder = builder
}


