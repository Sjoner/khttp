package com.sjoner.http

import com.sjoner.http.converter.Converter
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.lang.reflect.Type
import kotlin.properties.Delegates

var Retrofit.builder:Retrofit.Builder by Delegates.notNull()
inline fun createRetrofit(builder: Retrofit.Builder):Retrofit {
    var retrofit = builder.build()
    retrofit.builder = builder
    return retrofit
}

var Retrofit.Builder.responseConverter: Converter<ResponseBody, Any> by Delegates.notNull()

var Retrofit.Builder.requestConverter:Converter<Any,RequestBody> by Delegates.notNull()

var Retrofit.Builder.stringConverter:Converter<Any,String> by Delegates.notNull()

