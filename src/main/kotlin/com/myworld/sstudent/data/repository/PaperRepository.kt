package com.myworld.sstudent.data.repository

import com.myworld.sstudent.data.entity.Paper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository



@Repository
interface PaperRepository : MongoRepository<Paper, String> {

    fun findByWriterAndType(writer: String, type: String, pageable: Pageable) : Page<Paper>
}
