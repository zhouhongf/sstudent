package com.myworld.sstudent.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable


@Document(collection = "exam_journal")
class ExamJournal (
    @Id
    @Field("_id")
    var id: String? = null,
    @JsonIgnore
    var examinee: String? = null,

    @Field("exam_type")
    var examType: String,
    var questions: MutableList<String>,
    @Field("answers_wrong")
    var answersWrong: MutableList<String>,
    var score: Int? = null,

    @Field("time_start")
    var timeStart: Long,
    @Field("time_over")
    var timeOver: Long,
    @Field("time_end")
    var timeEnd: Long
): Serializable
