package com.myworld.sstudent.data.repository

import com.myworld.sstudent.data.entity.VocabularyVoice
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface VocabularyVoiceRepository : MongoRepository<VocabularyVoice, String> {
}
