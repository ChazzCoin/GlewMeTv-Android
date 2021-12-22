import io.ktor.server.netty.*
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.server.engine.*

fun main(args: Array<String>) {

    //Init
    embeddedServer(Netty, 8080) {
        routing {

            get("/") {
                call.respondText("Hello, world!", ContentType.Text.Html)
            }

            get("/aoki") {
                call.respondText("AOKI!", ContentType.Text.Html)
            }

        }
    }.start(wait = true)

}