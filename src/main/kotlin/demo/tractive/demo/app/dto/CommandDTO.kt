package demo.tractive.demo.app.dto

import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import java.time.LocalDateTime

data class PetCreateCommand(
    val petType: PetType,
    val trackerType: TrackerType,
    val ownerId: Long,
    val inZone: Boolean
) {
    fun toEntity(): PetEntity = PetEntity(
        petType = petType,
        trackerType = trackerType,
        ownerId = ownerId,
        inZone = inZone,
        created = LocalDateTime.now(),
        lastUpdate = LocalDateTime.now()
    )
}

data class PetTrackingCommand(
    val id: Long,
    val inZone: Boolean,
    val lostTracker: Boolean? = null
)

