package demo.tractive.demo.infrastructure.persistence

import demo.tractive.demo.domain.dao.PetDao
import demo.tractive.demo.domain.dao.dto.CountProjection
import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(JpaPetDao::class)
class JpaPetDaoTest @Autowired constructor(
    private val dao: PetDao,
    private val em: TestEntityManager,
    private val repository: JpaPetRepository
) {

    private lateinit var cat: PetEntity
    private lateinit var dog: PetEntity

    @BeforeEach
    fun setUp() {
        cat = dao.save(
            PetEntity.create(
                petType = PetType.CAT,
                trackerType = TrackerType.CAT_SMALL,
                ownerId = 1L,
                inZone = true,
                lostTracker = false
            )
        )
        dog = dao.save(
            PetEntity.create(
                petType = PetType.DOG,
                trackerType = TrackerType.DOG_BIG,
                ownerId = 2L,
                inZone = false
            )
        )
        em.flush()
        em.clear()
    }

    @Test
    fun `findById returns the persisted entity`() {
        val found = dao.findById(cat.id!!)
        assertThat(found).isNotNull
        assertThat(found?.petType).isEqualTo(PetType.CAT)
        assertThat(found?.trackerType).isEqualTo(TrackerType.CAT_SMALL)
    }

    @Test
    fun `findById returns null for non-existing entity`() {
        val found = dao.findById(999L)
        assertThat(found).isNull()
    }

    @Test
    fun `findAll filters correctly by inZone`() {
        val all = dao.findAll(null)
        assertThat(all).hasSize(2)

        val outside = dao.findAll(false)
        assertThat(outside).hasSize(1)
        assertThat(outside.first().id).isEqualTo(dog.id)
    }

    @Test
    fun `countOutsideByType aggregates as expected`() {
        repository.deleteAll()
        assertThat(repository.findAll()).isEmpty()

        // 1 - cat - inZone
        dao.save(
            PetEntity.create(
                petType = PetType.CAT,
                trackerType = TrackerType.CAT_SMALL,
                ownerId = 1L,
                inZone = true,
                lostTracker = false
            )
        )
        // 2 - cat - outside
        dao.save(
            PetEntity.create(
                petType = PetType.CAT,
                trackerType = TrackerType.CAT_BIG,
                ownerId = 1L,
                inZone = false,
                lostTracker = true
            )
        )

        // 3 - dog - outside
        dao.save(
            PetEntity.create(
                petType = PetType.DOG,
                trackerType = TrackerType.DOG_BIG,
                ownerId = 2L,
                inZone = false
            )
        )

        val counts: List<CountProjection> = dao.countOutsideByType()
        assertThat(counts).hasSize(2)

        assertThat(counts)
            .extracting("petType", "trackerType", "count")
            .containsExactlyInAnyOrder(
                tuple(PetType.CAT, TrackerType.CAT_BIG, 1L),
                tuple(PetType.DOG, TrackerType.DOG_BIG, 1L)
            )
    }
}