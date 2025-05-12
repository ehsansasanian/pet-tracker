package demo.tractive.demo.app

import demo.tractive.demo.app.dto.PetCreateCommand
import demo.tractive.demo.app.dto.PetResponseDTO
import demo.tractive.demo.app.dto.PetTrackingCommand
import demo.tractive.demo.domain.dao.PetDao
import demo.tractive.demo.domain.dao.dto.CountProjection
import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import jakarta.persistence.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PetServiceImplTest {

    private val dao: PetDao = mockk(relaxed = true)
    private lateinit var service: PetServiceImpl

    private lateinit var catEntity: PetEntity
    private lateinit var catDto: PetResponseDTO

    @BeforeEach
    fun setUp() {
        service = PetServiceImpl(dao)

        catEntity = PetEntity.create(
            petType = PetType.CAT,
            trackerType = TrackerType.CAT_SMALL,
            ownerId = 1L,
            inZone = true,
            lostTracker = false
        ).apply { id = 1L }

        catDto = PetResponseDTO.fromEntity(catEntity)
    }

    @Test
    fun `findAll delegates to DAO and maps to DTO`() {
        every { dao.findAll(null) } returns listOf(catEntity)

        val result = service.findAll(null)

        assertThat(result).containsExactly(catDto)
        verify(exactly = 1) { dao.findAll(null) }
    }

    @Test
    fun `save maps command to entity, delegates to DAO, returns DTO`() {
        val cmd = PetCreateCommand(
            petType = PetType.CAT,
            trackerType = TrackerType.CAT_SMALL,
            ownerId = 1L,
            inZone = true,
        )

        every { dao.save(any()) } returns catEntity

        val saved = service.save(cmd)

        assertThat(saved).isEqualTo(catDto)
        assertThat(saved.lastUpdate).isNotNull()
        verify { dao.save(match { it.petType == PetType.CAT && it.ownerId == 1L }) }
    }

    @Test
    fun `ingestTracking applies update and returns updated DTO`() {
        val entitySpy = spyk(catEntity)

        every { dao.findById(1L) } returns entitySpy

        val cmd = PetTrackingCommand(inZone = false, lostTracker = true)

        val updated = service.ingestTracking(1L, cmd)

        verify { entitySpy.applyTracking(false, true) }
        assertThat(updated.inZone).isFalse()
        assertThat(updated.petType).isEqualTo(PetType.CAT)
    }

    @Test
    fun `ingestTracking throws when pet not found`() {
        every { dao.findById(42L) } returns null

        val cmd = PetTrackingCommand(inZone = true)

        assertThatThrownBy { service.ingestTracking(42L, cmd) }
            .isInstanceOf(EntityNotFoundException::class.java)
            .hasMessageContaining("42")
    }

    @Test
    fun `countOutsideByType is pass-through`() {
        val projection = mockk<CountProjection> {
            every { petType } returns PetType.DOG
            every { trackerType } returns TrackerType.DOG_BIG
            every { count } returns 1L
        }
        every { dao.countOutsideByType() } returns listOf(projection)

        val result = service.countOutsideByType()

        assertThat(result).containsExactly(projection)
        verify { dao.countOutsideByType() }
    }
}