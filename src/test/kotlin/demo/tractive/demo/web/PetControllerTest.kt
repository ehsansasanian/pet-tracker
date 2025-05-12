package demo.tractive.demo.web

import com.fasterxml.jackson.databind.ObjectMapper
import demo.tractive.demo.app.PetService
import demo.tractive.demo.app.dto.PetCreateCommand
import demo.tractive.demo.app.dto.PetResponseDTO
import demo.tractive.demo.domain.model.PetEntity
import demo.tractive.demo.domain.model.PetType
import demo.tractive.demo.domain.model.TrackerType
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class PetControllerStandaloneTest {
    private lateinit var mockMvc: MockMvc
    private lateinit var service: PetService
    private val mapper = ObjectMapper()

    private fun Any.json(): String = mapper.writeValueAsString(this)

    private val catEntity = PetEntity.create(
        petType = PetType.CAT,
        trackerType = TrackerType.CAT_SMALL,
        ownerId = 1L,
        inZone = true,
        lostTracker = false
    ).apply { id = 1L }

    private val catDto = PetResponseDTO.fromEntity(catEntity)

    @BeforeEach
    fun setUp() {
        service = mockk(relaxed = true)
        val controller = PetController(service)
        mockMvc = MockMvcBuilders
            .standaloneSetup(controller)
            .setControllerAdvice(RestExceptionHandler())
            .build()
    }

    @Test
    fun `POST create returns 201 and Location`() {
        val cmd = PetCreateCommand(
            petType = PetType.CAT,
            trackerType = TrackerType.CAT_SMALL,
            ownerId = 1L,
            inZone = true
        )
        every { service.save(cmd) } returns catDto

        mockMvc.perform(
            post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cmd.json())
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/api/pets/1"))
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.petType").value("CAT"))
    }

    // TODO: Implement more tests for the remaining endpoints. Not doing it due to time constraints.
}