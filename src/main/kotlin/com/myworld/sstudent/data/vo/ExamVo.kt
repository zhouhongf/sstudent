package com.myworld.sstudent.data.vo

data class ExamVo (
    var contentEnglish: String? = null,
    var contentChinese: String? = null,
    var vocabularyList: MutableList<VocabularyVo>? = null
)
