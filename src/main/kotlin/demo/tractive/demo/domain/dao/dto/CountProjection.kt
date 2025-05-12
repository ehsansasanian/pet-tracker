package demo.tractive.demo.domain.dao.dto

import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType

interface CountProjection {
    val petType: PetType
    val trackerType: TrackerType
    val count: Long
}
