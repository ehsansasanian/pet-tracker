package demo.tractive.demo.web

import demo.tractive.demo.app.PetService
import demo.tractive.demo.app.dto.PetCreateCommand
import demo.tractive.demo.app.dto.PetResponseDTO
import demo.tractive.demo.app.dto.PetTrackingCommand
import demo.tractive.demo.domain.dao.dto.CountProjection
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/pets")
class PetController(private val service: PetService) {

    @PostMapping
    fun create(@RequestBody @Valid petCreateCommand: PetCreateCommand): ResponseEntity<PetResponseDTO> {
        val saved = service.save(petCreateCommand)
        return ResponseEntity
            .created(URI.create("/api/pets/${saved.id}"))
            .body(saved)
    }

    @GetMapping
    fun list(@RequestParam(required = false) inZone: Boolean?): ResponseEntity<List<PetResponseDTO>> {
        return ResponseEntity.ok(service.findAll(inZone))
    }

    @GetMapping("/{id}")
    fun findPetById(@PathVariable id: Long): PetResponseDTO {
        return service.findById(id)
    }

    @PostMapping("/{id}/tracking")
    fun ingestTracking(@PathVariable id: Long, @RequestBody @Valid command: PetTrackingCommand): PetResponseDTO =
        service.ingestTracking(id, command)

    @GetMapping("/outside-power-saving")
    fun countOutside(): List<CountProjection> = service.countOutsideByType()
}