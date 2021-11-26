package com.myworld.sstudent.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable


@Document(collection = "english_dict")
class Vocabulary (
    @Id
    @Field("_id")
    var id: String? = null,

    @Field("name_chinese")
    var nameChinese: String? = null,
    @Field("name_english")
    var nameEnglish: String? = null,
    var phonetic: String? = null,

    @JsonIgnore
    var voice: String? = null,
    @JsonIgnore
    var status: String = "undo"
): Serializable
