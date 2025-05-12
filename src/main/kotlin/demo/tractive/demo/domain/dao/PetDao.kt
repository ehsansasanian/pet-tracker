package demo.tractive.demo.domain.dao

import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType

interface PetDao {
    fun findById(id: Long): PetEntity?

    fun findAll(inZone: Boolean?): List<PetEntity>

    fun save(petEntity: PetEntity): PetEntity

    fun countOutsideByType(): Map<PetType, Map<TrackerType, Long>>
}