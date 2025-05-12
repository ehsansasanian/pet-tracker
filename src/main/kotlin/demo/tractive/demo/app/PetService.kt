package demo.tractive.demo.app

import demo.tractive.demo.app.dto.PetCreateCommand
import demo.tractive.demo.app.dto.PetResponseDTO
import demo.tractive.demo.app.dto.PetTrackingCommand
import demo.tractive.demo.domain.dao.dto.CountProjection

/**
 * Service interface for managing pets and their tracking information.
 * Provides methods for CRUD operations and business logic related to pets.
 */
interface PetService {

    /**
     * Retrieves a list of all pets, optionally filtered by their in-zone status.
     *
     * @param inZone Optional filter to retrieve pets based on their in-zone status.
     * @return A list of `PetResponseDTO` representing the pets.
     */
    fun findAll(inZone: Boolean?): List<PetResponseDTO>

    /**
     * Saves a new pet entity based on the provided creation command.
     *
     * @param pet The `PetCreateCommand` containing the details of the pet to be created.
     * @return A `PetResponseDTO` representing the saved pet.
     */
    fun save(pet: PetCreateCommand): PetResponseDTO

    /**
     * Updates the tracking information of an existing pet.
     *
     * @param command The `PetTrackingCommand` containing the tracking details to be updated.
     * @return A `PetResponseDTO` representing the updated pet.
     */
    fun ingestTracking(command: PetTrackingCommand): PetResponseDTO

    /**
     * Counts the number of pets outside their designated zones, grouped by pet type and tracker type.
     *
     * @return A map where the key is the `PetType` and the value is another map
     *         with `TrackerType` as the key and the count as the value.
     */
    fun countOutsideByType(): List<CountProjection>
}