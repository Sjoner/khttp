package cn.sjoner.khttp.converter

import cn.sjoner.khttp.builder
import cn.sjoner.khttp.requestConverter
import cn.sjoner.khttp.responseConverter
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class DefaultConverterFactory: Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>?, retrofit: Retrofit): Converter<ResponseBody, *> {
        return Converter<ResponseBody,Any>{value ->
            val converter = retrofit.builder.responseConverter
            converter.convert(value, type)
        }
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit): Converter<*, RequestBody> {
        return Converter<Any, RequestBody>{data->
            val converter = retrofit.builder.requestConverter
            converter.convert(data, type)
        }
    }
}