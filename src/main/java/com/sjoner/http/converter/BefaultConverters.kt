package com.sjoner.http.converter

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

fun converterString(value: Any):String{
    val fields = value.javaClass.declaredFields
    val builder = StringBuilder()
    val iterator = fields.iterator()
    while (iterator.hasNext()) {
        val field = iterator.next()
        val name = field.name
        val data = field.get(value)
        builder.append(name).append("=").append(data)
        if (iterator.hasNext()) {
            builder.append("&")
        }
    }
    return builder.toString()
}

interface Converter<F,T>{
    fun convert(value:F,returnType:Type):T
}

class DefaultRequestConverter: Converter<Any?, RequestBody> {
    override fun convert(value:Any?, type: Type): RequestBody {
        val contentType = MediaType.parse("@application/text;charset=UTF-8")
        return if (value == null) {
            RequestBody.create(contentType, "")
        } else {
            RequestBody.create(contentType, converterString(value))
        }
    }
}

class DefaultResponseConverter: Converter<ResponseBody?, Any> {
    override fun convert(responseBody: ResponseBody?, returnType: Type): Any {
        return responseBody!!
    }
}

class DefaultQueryMapConverter:Converter<Any?,Map<String,String>>{
    override fun convert(body: Any?, returnType: Type): Map<String, String> {
        val map = HashMap<String, String>()
        if (body is Map<*, *>) {
            body.map { entry ->
                val keyValue = entry.value
                if (keyValue != null) {
                    map.put(entry.key as String, keyValue.toString())
                }
            }
        }else if (body != null) {
            val fields = body.javaClass.declaredFields
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
        return map
    }

}