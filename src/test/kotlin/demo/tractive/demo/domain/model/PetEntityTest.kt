package demo.tractive.demo.domain.model

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class PetEntityTest {

    @Test
    fun `cat applyTracking updates inZone and lostTracker`() {
        val cat = PetEntity.create(
            petType = PetType.CAT,
            trackerType = TrackerType.CAT_SMALL,
            ownerId = 1L,
            inZone = true,
            lostTracker = false
        )

        cat.applyTracking(inZone = false, lostTracker = true)

        assertThat(cat.inZone).isFalse()
        assertThat(cat.lostTracker).isTrue()
    }

    @Test
    fun `dog applyTracking updates only inZone`() {
        val dog = PetEntity.create(
            petType = PetType.DOG,
            trackerType = TrackerType.DOG_BIG,
            ownerId = 2L,
            inZone = true
        )

        dog.applyTracking(inZone = false, lostTracker = null)

        assertThat(dog.inZone).isFalse()
        assertThat(dog.lostTracker).isNull()        // must stay null
    }

    @Test
    fun `dog applyTracking with lostTracker flag throws`() {
        val dog = PetEntity.create(
            petType = PetType.DOG,
            trackerType = TrackerType.DOG_MEDIUM,
            ownerId = 3L,
            inZone = true
        )

        assertThatThrownBy {
            dog.applyTracking(inZone = false, lostTracker = true)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("lostTracker")
    }
}
