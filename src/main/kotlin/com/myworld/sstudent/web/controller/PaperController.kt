package com.myworld.sstudent.web.controller

import com.myworld.sstudent.data.entity.Paper
import com.myworld.sstudent.web.service.PaperService
import com.myworld.sstudent.common.ApiResult
import com.myworld.sstudent.data.entity.ExamJournal
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/paper")
class PaperController {

    private val log : Logger = LogManager.getRootLogger()

    @Autowired
    private lateinit var paperService: PaperService

    @GetMapping("studyInfo")
    fun getStudyInfo(): ApiResult<Any?> {
        return paperService.getStudyInfo()
    }

    @PostMapping("setPaper")
    fun setPaper(@RequestBody paper: Paper): ApiResult<Any?> {
        return paperService.setPaper(paper)
    }

    @DeleteMapping("delPaper")
    fun delPaper(@RequestParam id: String): ApiResult<Any?> {
        return paperService.delPaper(id)
    }

    @GetMapping("getPaper")
    fun getPaper(@RequestParam id: String): ApiResult<Any?> {
        return paperService.getPaper(id)
    }

    @GetMapping("list")
    fun list(@RequestParam type: String, @RequestParam pageIndex: Int, @RequestParam pageSize: Int): ApiResult<Any?> {
        return paperService.list(type, pageIndex, pageSize)
    }

    @GetMapping("examRange")
    fun getExamRange(@RequestParam timeStart: Long, @RequestParam timeEnd: Long): ApiResult<Any?> {
        return paperService.getExamRange(timeStart, timeEnd)
    }

    @GetMapping("examPaper")
    fun getExamPaper(@RequestParam paperIds: ArrayList<String>, @RequestParam questionTypes: ArrayList<String>): ApiResult<Any?> {
        return paperService.getExamPaper(paperIds, questionTypes)
    }

    @PostMapping("examJournal")
    fun setExamJournal(@RequestParam examScore: Int, @RequestBody examJournal: ExamJournal): ApiResult<Any?> {
        return paperService.setExamJournal(examScore, examJournal)
    }



}
