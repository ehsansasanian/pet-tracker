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
    val lostTracker: Boolean? = null
)

enum class PetType { CAT, DOG }

enum class TrackerType {
    CAT_SMALL, CAT_BIG,
    DOG_SMALL, DOG_MEDIUM, DOG_BIG
}
