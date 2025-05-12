package demo.tractive.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TrackingApp

fun main(args: Array<String>) {
	runApplication<TrackingApp>(*args)
}
