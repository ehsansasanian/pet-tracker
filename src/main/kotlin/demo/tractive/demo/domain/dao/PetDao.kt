package demo.tractive.demo.domain.dao

import demo.tractive.demo.app.dto.PetSearchCriteria
import demo.tractive.demo.domain.dao.dto.CountProjection
import demo.tractive.demo.domain.model.PetEntity

interface PetDao {
    fun findById(id: Long): PetEntity?

    fun findAll(inZone: Boolean?): List<PetEntity>

    fun save(petEntity: PetEntity): PetEntity

    fun countOutsideByType(): List<CountProjection>

    fun search(criteria: PetSearchCriteria): MutableList<PetEntity>
}