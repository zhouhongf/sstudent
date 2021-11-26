package com.myworld.sstudent.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.myworld.sstudent.data.vo.VocabularyVo
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


@Document(collection = "paper")
class Paper (
    @Id
    @Field("_id")
    var id: String? = null,
    var type: String? = null,
    @JsonIgnore
    var writer: String? = null,

    @Field("title_chinese")
    var titleChinese: String? = null,
    @Field("author_chinese")
    var authorChinese: String? = null,
    @Field("content_chinese")
    var contentChinese: String? = null,

    @Field("title_english")
    var titleEnglish: String? = null,
    @Field("author_english")
    var authorEnglish: String? = null,
    @Field("content_english")
    var contentEnglish: String? = null,

    var vocabularies: MutableList<VocabularyVo> = ArrayList(),

    var status: String = "NO",
    @Field("create_time")
    var createTime: Long = Date().time
    ): Serializable
