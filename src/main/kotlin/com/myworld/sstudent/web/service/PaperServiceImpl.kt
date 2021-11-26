package com.myworld.sstudent.web.service

import com.myworld.sstudent.data.entity.Paper
import com.myworld.sstudent.data.repository.PaperRepository
import com.myworld.sstudent.common.ApiResult
import com.myworld.sstudent.common.ResultUtil
import com.myworld.sstudent.common.UserContextHolder
import com.myworld.sstudent.data.entity.ExamJournal
import com.myworld.sstudent.data.repository.ExamJournalRepository
import com.myworld.sstudent.data.repository.VocabularyRepository
import com.myworld.sstudent.data.vo.ExamVo
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation
import org.springframework.data.mongodb.core.aggregation.AggregationOperation
import org.springframework.data.mongodb.core.aggregation.Fields
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.ceil
import kotlin.math.roundToInt


@Service
class PaperServiceImpl : PaperService {

    private val log : Logger = LogManager.getRootLogger()

    @Autowired
    private lateinit var paperRepository: PaperRepository
    @Autowired
    private lateinit var vocabularyRepository: VocabularyRepository
    @Autowired
    private lateinit var examJournalRepository: ExamJournalRepository
    @Autowired
    private lateinit var mongoTemplate: MongoTemplate
    @Autowired
    private lateinit var chatFeignService: ChatFeignService


    override fun list(type: String, pageIndex: Int, pageSize: Int): ApiResult<Any?> {
        val simpleUser = UserContextHolder.getUserContext()
        val userWid = simpleUser.wid
        val pageable = PageRequest.of(pageIndex, pageSize, Sort(Sort.Direction.DESC, "createTime"))
        val paperPage = paperRepository.findByWriterAndType(writer = userWid, type = type, pageable = pageable)
        val paperList: MutableList<Any> = ArrayList()
        for (one in paperPage.content) {
            val map: MutableMap<String, Any> = HashMap()
            map["id"] = one.id!!
            map["titleChinese"] = one.titleChinese!!
            map["createTime"] = one.createTime
            map["status"] = one.status
            paperList.add(map)
        }
        return ResultUtil.success(num = paperPage.totalElements, data = paperList)
    }

    override fun getStudyInfo(): ApiResult<Any?> {
        val simpleUser = UserContextHolder.getUserContext()
        val userWid = simpleUser.wid

        val query = Query()
        query.addCriteria(Criteria.where("writer").`is`(userWid).and("status").`is`("YES"))
        // 找出该userWid名下所有发布过的文章的数量
        val paperCount = mongoTemplate.count(query, Paper::class.java)
        // 找出该userWid名最近发布的5篇文章的中英文标题
        query.fields().include("title_english").include("title_chinese").exclude("_id")
        val papers = mongoTemplate.find(query.with(Sort(Sort.Direction.DESC, "create_time")).limit(5), Paper::class.java)
        val paperTitles: MutableList<String> = ArrayList()
        for (paper in papers) {
            paperTitles.add(paper.titleEnglish!!)
        }


        // 找出该userWid名下的考试记录概况
        val operations: MutableList<AggregationOperation> = java.util.ArrayList()
        operations.add(Aggregation.match(Criteria.where("examinee").`is`(userWid)))
        operations.add(
            Aggregation.group("exam_type").max("score").`as`("max").min("score").`as`("min").avg("score").`as`("avg").sum("score").`as`("sum")
        )
        val aggregation = Aggregation.newAggregation(operations)
        val results = mongoTemplate.aggregate(aggregation, "exam_journal", java.util.HashMap::class.java)
        val result = results.mappedResults

        val map: MutableMap<String, Any> = HashMap()
        map["paperCount"] = paperCount
        map["paperTitles"] = paperTitles
        map["examJournal"] = result

        return ResultUtil.success(data = map)
    }




    override fun setPaper(paper: Paper): ApiResult<Any?> {
        val simpleUser = UserContextHolder.getUserContext()
        val userWid = simpleUser.wid
        val createTime = Date().time
        if (paper.id.isNullOrEmpty()) {
            paper.id = "$userWid=$createTime"
            paper.createTime = createTime
        }

        // 发布文章，即将文章同时发布到社交动态板块中去
        if (paper.status == "YES") {
            val titleEnglish = paper.titleEnglish
            val contentEnglish = paper.contentEnglish
            val content = "<div><strong>学习课文：</strong><span class='text-info' style=\"font-size: 20px\">《$titleEnglish》</span><div><br><div><strong>课文内容：</strong>$contentEnglish<div>"
            chatFeignService.createShowBlog("ALL", content)
        }
        paper.writer = userWid
        paperRepository.save(paper)
        return ResultUtil.success(data=paper.id)
    }

    override fun delPaper(id: String): ApiResult<Any?> {
        paperRepository.deleteById(id)
        return ResultUtil.success()
    }

    override fun getPaper(id: String): ApiResult<Any?> {
        val paper = paperRepository.findById(id)
        return if (paper.isPresent) {
            ResultUtil.success(data = paper.get())
        } else {
            ResultUtil.failure(-2, "没能找到相关数据")
        }
    }

    // 每次考试范围不超过20篇文章的内容,
    override fun getExamRange(timeStart: Long, timeEnd: Long): ApiResult<Any?> {
        val simpleUser = UserContextHolder.getUserContext()
        val userWid = simpleUser.wid
        val operations: MutableList<AggregationOperation> = java.util.ArrayList()
        operations.add(Aggregation.match(Criteria.where("writer").`is`(userWid).and("create_time").gte(timeStart).lt(timeEnd)))
        operations.add(Aggregation.match(Criteria.where("status").`is`("YES")))
        operations.add(Aggregation.sample(20))
        operations.add(Aggregation.project(Fields.fields("title_chinese", "title_english")))
        val aggregation = Aggregation.newAggregation(operations)
        val results = mongoTemplate.aggregate(aggregation, "paper", java.util.HashMap::class.java)
        val result = results.mappedResults
        return if (result.size > 0) ResultUtil.success(data=result) else ResultUtil.failure(-2, "未能找到符合条件的数据")
    }


    override fun getExamPaper(paperIds: ArrayList<String>, questionTypes: ArrayList<String>): ApiResult<Any?> {
        val paperList = paperRepository.findAllById(paperIds)
        if (paperList.count() == 0) {
            return ResultUtil.failure(-2, "没有找到相关的数据")
        }

        val examVoList: MutableList<ExamVo> = ArrayList()
        for (one in paperList) {
            val examVo = ExamVo(contentEnglish = one.contentEnglish, contentChinese = one.contentChinese, vocabularyList = one.vocabularies)
            examVoList.add(examVo)
        }
        return ResultUtil.success(data = examVoList)
    }

    override fun setExamJournal(examScore: Int, examJournal: ExamJournal): ApiResult<Any?> {
        val simpleUser = UserContextHolder.getUserContext()
        val userWid = simpleUser.wid

        examJournal.examinee = userWid
        examJournal.id = userWid + '=' + examJournal.timeStart
        examJournal.score = examScore
        examJournalRepository.save(examJournal)

        // 考试成绩超过80分，才发布到朋友圈
        if (examScore > 80) {
            val timeUse = ceil((examJournal.timeOver.toDouble() - examJournal.timeStart.toDouble()) / 60000.0)
            val questions = examJournal.questions.joinToString(",")
            val examType = examJournal.examType
            val content = "<div><strong>考试项目：</strong><span style=\"font-size: 20px\">$examType</span><div><br><div><strong>考试用时：</strong><span>$timeUse</span>分钟</div><br><div><strong>考试成绩：</strong><span class='text-info' style='font-size: 20px'>$examScore</span>分<div><br><div><strong>考试内容：</strong>$questions<div>"
            chatFeignService.createShowBlog("ALL", content)
        }
        return ResultUtil.success()
    }

}
