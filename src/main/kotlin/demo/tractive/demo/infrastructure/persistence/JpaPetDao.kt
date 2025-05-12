package demo.tractive.demo.infrastructure.persistence

import demo.tractive.demo.domain.dao.PetDao
import demo.tractive.demo.domain.dao.dto.CountProjection
import demo.tractive.demo.domain.model.PetEntity
import org.springframework.data.jpa.repository.JpaRepository
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

    override fun save(petEntity: PetEntity): PetEntity {
        return repository.save(petEntity)
    }

    override fun countOutsideByType(): List<CountProjection> {
        return repository.groupedCounts()
    }
}

interface JpaPetRepository : JpaRepository<PetEntity, Long> {

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