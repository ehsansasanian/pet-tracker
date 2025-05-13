package demo.tractive.demo.app.dto

import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType

data class PetSearchCriteria(
    val petType: PetType? = null,
    val trackerType: TrackerType? = null,
    val ownerId: Long? = null,
    val inZone: Boolean? = null
)