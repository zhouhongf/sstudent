package com.myworld.sstudent.data.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable

@Document(collection = "english_voice")
class VocabularyVoice (
    @Id
    @Field("_id")
    var id: String? = null,
    var content: ByteArray? = null,
    @Field("file_suffix")
    var fileSuffix: String? = null
): Serializable
