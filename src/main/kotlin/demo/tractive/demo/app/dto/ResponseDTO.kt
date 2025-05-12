package demo.tractive.demo.app.dto

import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import java.time.LocalDateTime

data class PetResponseDTO(
    val id: Long?,
    val petType: PetType,
    val trackerType: TrackerType,
    val ownerId: Long,
    val inZone: Boolean,
    val lastUpdate: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: PetEntity) = PetResponseDTO(
            id = entity.id,
            petType = entity.petType,
            trackerType = entity.trackerType,
            ownerId = entity.ownerId,
            inZone = entity.inZone,
            lastUpdate = entity.lastUpdate
        )
    }
}
