# khttp

## 添加依赖

```
allprojects {
    repositories {
        ...
	maven { url 'https://jitpack.io' }
    }
}
dependencies {
    compile 'com.github.Sjoner:khttp:{version}'
}

```

## 在项目的Application中初始化

```
class App:Application() {
    override fun onCreate() {
        super.onCreate()
        initRetrofit {
            baseUrl("http://www.kuaidi100.com")
            debug = true
        }
    }
}
```

## 发起请求

```
http<HashMap<String,String>, QueryResp>{
            url = "/query"
            method = HttpMethod.GET
            body = body {
                put("type","yuantong")
                put("postid","11111111111")
            }
            headers{
                put("key","value")
            }
            success{response ->
                val responseBody = response?.body()
                println("responseBody = ${responseBody?.message}")
            }

            error{t->
                t?.printStackTrace()
            }
        }
```
