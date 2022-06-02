package ru.altmanea.edu.server.rest


import Aud
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import ru.altmanea.edu.server.model.*
import ru.altmanea.edu.server.model.Config.Companion.audsPath
import ru.altmanea.edu.server.repo.AudsRepo
import ru.altmanea.edu.server.repo.lessonsRepo

fun Route.aud() {
    route(audsPath) {
        get {
            if (!AudsRepo.isEmpty()) {
                call.respond(AudsRepo.findAll())
            } else {
                call.respondText("No groups found", status = HttpStatusCode.NotFound)
            }
        }
        get("{name}") {
            val name = call.parameters["name"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val lessonItem =
                lessonsRepo.find { it.aud.toString() == name }
            call.response.etag(lessonItem.toString())
            call.respond(lessonItem)
        }
        post {
            val gr = call.receive<Aud>()
            AudsRepo.create(gr)
            call.respondText("Group stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (AudsRepo.delete(id)) {
                call.respondText("Group removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
    }

}


