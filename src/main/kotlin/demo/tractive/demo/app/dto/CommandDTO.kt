package demo.tractive.demo.app.dto

import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import jakarta.annotation.Nonnull
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

data class PetCreateCommand(
    @field:Nonnull val petType: PetType,
    @field:Nonnull val trackerType: TrackerType,
    @field:Positive val ownerId: Long,
    @field:Nonnull val inZone: Boolean
) {
    fun toEntity(): PetEntity {
        require(trackerType.isAllowedFor(petType)) {
            "TrackerType $trackerType cannot be used for $petType"
        }
        return PetEntity(
            petType = petType,
            trackerType = trackerType,
            ownerId = ownerId,
            inZone = inZone,
            created = LocalDateTime.now(),
            lastUpdate = LocalDateTime.now(),
            lostTracker = if (petType == PetType.CAT) false else null
        )
    }
}

data class PetTrackingCommand(
    @field:Nonnull val inZone: Boolean,
    val lostTracker: Boolean? = null
)

