package com.myworld.sstudent.web.service

import com.myworld.sstudent.common.ApiResult
import com.myworld.sstudent.common.ResultUtil
import com.myworld.sstudent.data.repository.VocabularyRepository
import com.myworld.sstudent.data.repository.VocabularyVoiceRepository
import org.apache.commons.io.IOUtils
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.servlet.http.HttpServletResponse

@Service
class VocabularyService {

    private val log : Logger = LogManager.getRootLogger()

    @Autowired
    private lateinit var vocabularyRepository: VocabularyRepository
    @Autowired
    private lateinit var vocabularyVoiceRepository: VocabularyVoiceRepository


    fun getVocabulary(id: String): ApiResult<Any?> {
        val vocabulary = vocabularyRepository.findById(id)
        return if (vocabulary.isPresent) {
            ResultUtil.success(data = vocabulary.get())
        } else {
            ResultUtil.failure(-2, "没有找到相关数据")
        }
    }

    @Throws(IOException::class)
    fun getVocabularyVoice(id: String, response: HttpServletResponse) {
        val vocabularyVoice = vocabularyVoiceRepository.findById(id)
        log.info("找到的数据为：{}", vocabularyVoice.get().id)
        if (vocabularyVoice.isPresent) {
            IOUtils.copy(ByteArrayInputStream(vocabularyVoice.get().content), response.outputStream)
            response.contentType = "audio/mpeg3"
        }
    }


}
