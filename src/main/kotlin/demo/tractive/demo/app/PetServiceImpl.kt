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
        // todo: handle lostTracker
        return PetResponseDTO.fromEntity(dao.save(pet.toEntity()))
    }

    @Transactional
    override fun ingestTracking(command: PetTrackingCommand): PetResponseDTO {
        val pet = dao.findById(command.id)
        // todo: change this exception
            ?: throw EntityNotFoundException("Pet id=${command.id} not found")

        logger.info("Ingesting tracking for pet: $pet")

        // todo: handle lostTracker
        pet.applyTracking(command.inZone, command.lostTracker)
        return PetResponseDTO.fromEntity(pet)
    }

    override fun countOutsideByType(): List<CountProjection> {
        return dao.countOutsideByType()
    }
}