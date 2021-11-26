package com.myworld.sstudent.web.service

import com.myworld.sstudent.common.ApiResult
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.*

@Component
@FeignClient(name = "schat-server", url = "\${feign.schat.url}")
interface ChatFeignService {

    @RequestMapping(value = ["/createShowBlog"], method = [RequestMethod.POST])
    fun createShowBlog(@RequestParam contactTag: String, @RequestBody content: String): ApiResult<*>?
}


