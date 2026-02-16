package com.example.demo.domain.concert.dao

import com.example.demo.domain.concert.model.Concert
import com.example.demo.domain.concert.model.ConcertStatus
import org.springframework.data.jpa.repository.JpaRepository

/**
 * PackageName : com.example.demo.domain.concert.dao
 * FileName    : ConcertRepository
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description : 콘서트 DAO
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
interface ConcertRepository : JpaRepository<Concert, Long> {

    fun findByName(name: String): Concert?
    fun findByStatus(status: ConcertStatus): List<Concert>

}