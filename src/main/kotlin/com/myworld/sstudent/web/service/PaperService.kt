package com.myworld.sstudent.web.service

import com.myworld.sstudent.data.entity.Paper
import com.myworld.sstudent.common.ApiResult
import com.myworld.sstudent.data.entity.ExamJournal


interface PaperService {
    fun getStudyInfo(): ApiResult<Any?>
    fun list(type: String, pageIndex: Int, pageSize: Int): ApiResult<Any?>

    fun setPaper(paper: Paper): ApiResult<Any?>
    fun delPaper(id: String): ApiResult<Any?>
    fun getPaper(id: String): ApiResult<Any?>

    fun getExamRange(timeStart: Long, timeEnd: Long): ApiResult<Any?>
    fun getExamPaper(paperIds: ArrayList<String>, questionTypes: ArrayList<String>): ApiResult<Any?>

    fun setExamJournal(examScore: Int, examJournal: ExamJournal): ApiResult<Any?>
}
