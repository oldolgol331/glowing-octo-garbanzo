package com.example.demo.domain.concert.scheduler

import com.example.demo.domain.concert.constant.ConcertConst.LIMIT
import com.example.demo.domain.concert.dao.ConcertQueueRepository
import com.example.demo.domain.concert.dao.ConcertRepository
import com.example.demo.domain.concert.model.ConcertStatus.OPEN
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*

/**
 * PackageName : com.example.demo.domain.concert.scheduler
 * FileName    : QueueScheduler
 * Author      : oldolgol331
 * Date        : 26. 2. 15.
 * Description :
 * =====================================================================================================================
 * DATE          AUTHOR               DESCRIPTION
 * ---------------------------------------------------------------------------------------------------------------------
 * 26. 2. 15.    oldolgol331          Initial creation
 */
@Component
class QueueScheduler(
    private val concertRepository: ConcertRepository,
    private val concertQueueRepository: ConcertQueueRepository
) {

    private val log = KotlinLogging.logger {}

    @Scheduled(fixedDelay = 1000)
    fun scheduleAllowUsers() {
        val activeConcerts = concertRepository.findByStatus(OPEN)

        if (activeConcerts.isEmpty()) return

        activeConcerts.forEach { concert ->
            val concertId = concert.id
            val limit = LIMIT

            val currentActiveSize = concertQueueRepository.getActiveSize(concertId)
            val availableSlots = LIMIT - currentActiveSize

            if (availableSlots > 0) {
                val allowedUserIds = concertQueueRepository.popMinFromWaitingQueue(concertId, availableSlots)
                allowedUserIds.forEach { userId ->
                    concertQueueRepository.addToActiveQueue(
                        concertId,
                        UUID.fromString(userId)
                    )
                }
            }
        }
    }

}