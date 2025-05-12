package demo.tractive.demo.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "pets")
class PetEntity(
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    var id: Long? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    var petType: PetType,

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    var trackerType: TrackerType,

    @Column(nullable = false)
    var ownerId: Long,

    @Column(nullable = false)
    var inZone: Boolean,

    @Column(nullable = false, updatable = false)
    var created: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var lastUpdate: LocalDateTime = LocalDateTime.now(),

    /*
    Nullable — only meaningful for cats.

    KISS Pattern:

    For simplicity, I accepted the tradeoff of having a nullable field rather than having a more complex logic.
    In the case of more specific business logic related to either of the pet types, a separate table can be created
    to keep the details related to the pet type.

    DB migration can be done via Flyway or Liquibase.
    */
    var lostTracker: Boolean? = null
) {
    fun applyTracking(inZone: Boolean, lostTracker: Boolean?) {
        if (lostTracker != null && petType != PetType.CAT) {
            throw IllegalArgumentException("lostTracker flag applies only to cats")
        }
        this.inZone = inZone
        if (petType == PetType.CAT && lostTracker != null) {
            this.lostTracker = lostTracker
        }
    }

    companion object {
        fun create(
            petType: PetType,
            trackerType: TrackerType,
            ownerId: Long,
            inZone: Boolean,
            lostTracker: Boolean? = null
        ): PetEntity {
            require(trackerType.isAllowedFor(petType)) {
                "TrackerType $trackerType cannot be used for $petType"
            }
            return PetEntity(
                petType = petType,
                trackerType = trackerType,
                ownerId = ownerId,
                inZone = inZone,
                lostTracker = if (petType == PetType.CAT) lostTracker ?: false else null
            )
        }
    }
}

enum class PetType { CAT, DOG }

enum class TrackerType(val allowedPetType: PetType) {
    CAT_SMALL(PetType.CAT), CAT_BIG(PetType.CAT),
    DOG_SMALL(PetType.DOG), DOG_MEDIUM(PetType.DOG), DOG_BIG(PetType.DOG);

    fun isAllowedFor(petType: PetType) = petType == allowedPetType
}
