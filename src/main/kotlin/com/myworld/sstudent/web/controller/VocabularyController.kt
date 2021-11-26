package com.myworld.sstudent.web.controller

import com.myworld.sstudent.common.ApiResult
import com.myworld.sstudent.web.service.VocabularyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/vocabulary")
class VocabularyController {

    @Autowired
    private lateinit var vocabularyService: VocabularyService

    @GetMapping("getVocabulary")
    fun getVocabulary(@RequestParam id: String): ApiResult<Any?> {
        return vocabularyService.getVocabulary(id)
    }

    @GetMapping("/voice/{id}")
    @Throws(IOException::class)
    fun getVocabularyVoice(@PathVariable id: String, response: HttpServletResponse) {
        vocabularyService.getVocabularyVoice(id, response)
    }
}
