package com.myworld.sstudent.data.repository

import com.myworld.sstudent.data.entity.Vocabulary
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface VocabularyRepository : MongoRepository<Vocabulary, String> {
}
