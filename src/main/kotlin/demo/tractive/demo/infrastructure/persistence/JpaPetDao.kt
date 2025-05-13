package demo.tractive.demo.infrastructure.persistence

import demo.tractive.demo.app.dto.PetSearchCriteria
import demo.tractive.demo.domain.dao.PetDao
import demo.tractive.demo.domain.dao.dto.CountProjection
import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Component

@Component
class JpaPetDao(private val repository: JpaPetRepository) : PetDao {
    override fun findById(id: Long): PetEntity? {
        return repository.findById(id).orElse(null)
    }

    override fun findAll(inZone: Boolean?): List<PetEntity> {
        return if (inZone == null) {
            repository.findAll()
        } else {
            repository.findByInZone(inZone)
        }
    }

    override fun search(criteria: PetSearchCriteria): MutableList<PetEntity> {
        val spec = Specification<PetEntity> { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            criteria.petType?.let  { predicates += cb.equal(root.get<PetType>("petType"), it) }
            criteria.trackerType?.let { predicates += cb.equal(root.get<TrackerType>("trackerType"), it) }
            criteria.ownerId?.let { predicates += cb.equal(root.get<Long>("ownerId"), it) }
            criteria.inZone?.let   { predicates += cb.equal(root.get<Boolean>("inZone"), it) }

            cb.and(*predicates.toTypedArray())
        }
        return repository.findAll(spec)
    }

    override fun save(petEntity: PetEntity): PetEntity {
        return repository.save(petEntity)
    }

    override fun countOutsideByType(): List<CountProjection> {
        return repository.groupedCounts()
    }
}

interface JpaPetRepository : JpaRepository<PetEntity, Long>, JpaSpecificationExecutor<PetEntity> {

    fun findByInZone(inZone: Boolean): List<PetEntity>

    @Query(
        """
      SELECT p.petType as petType,
             p.trackerType as trackerType, 
             COUNT(p) as count 
      FROM PetEntity p WHERE p.inZone = false 
      GROUP BY p.petType, p.trackerType
      """
    )
    fun groupedCounts(): List<CountProjection>
}