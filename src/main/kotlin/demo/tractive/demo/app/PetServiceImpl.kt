package demo.tractive.demo.app

import demo.tractive.demo.app.dto.PetCreateCommand
import demo.tractive.demo.app.dto.PetResponseDTO
import demo.tractive.demo.app.dto.PetTrackingCommand
import demo.tractive.demo.domain.dao.PetDao
import demo.tractive.demo.domain.dao.dto.CountProjection
import jakarta.persistence.EntityNotFoundException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PetServiceImpl(private val dao: PetDao) : PetService {
    private val logger: Logger = LoggerFactory.getLogger(PetServiceImpl::class.java)

    override fun findAll(inZone: Boolean?): List<PetResponseDTO> {
        logger.info("Finding all pets with inZone: $inZone")

        return dao.findAll(inZone)
            .map { PetResponseDTO.fromEntity(it) }
    }

    @Transactional
    override fun save(pet: PetCreateCommand): PetResponseDTO {
        logger.info("Saving pet: $pet")
        return PetResponseDTO.fromEntity(dao.save(pet.toEntity()))
    }

    @Transactional
    override fun ingestTracking(id: Long, command: PetTrackingCommand): PetResponseDTO {
        val pet = dao.findById(id)
        // Ideal here would be to use a 'service layer' dedicated exception with custom exception handler.
        // Due to time constraints I used something simple with a default exception handling
            ?: throw EntityNotFoundException("Pet id=${id} not found")

        logger.info("Ingesting tracking for pet: $pet")

        pet.applyTracking(command.inZone, command.lostTracker)
        return PetResponseDTO.fromEntity(pet)
    }

    override fun findById(id: Long): PetResponseDTO {
        return dao.findById(id)
            ?.let { PetResponseDTO.fromEntity(it) }
            ?: throw EntityNotFoundException("Pet id=$id not found")
    }

    override fun countOutsideByType(): List<CountProjection> {
        return dao.countOutsideByType()
    }
}