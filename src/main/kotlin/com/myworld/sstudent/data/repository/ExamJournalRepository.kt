package com.myworld.sstudent.data.repository

import com.myworld.sstudent.data.entity.ExamJournal
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ExamJournalRepository : MongoRepository<ExamJournal, String> {
}
