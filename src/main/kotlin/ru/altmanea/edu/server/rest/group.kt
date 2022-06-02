package ru.altmanea.edu.server.rest


import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import ru.altmanea.edu.server.model.*
import ru.altmanea.edu.server.model.Config.Companion.groupsPath
import ru.altmanea.edu.server.model.Lesson
import ru.altmanea.edu.server.repo.flowsRepo
import ru.altmanea.edu.server.repo.lessonsRepo

fun Route.group() {
    route(groupsPath) {
        get {
            if (!flowsRepo.isEmpty()) {
                call.respond(flowsRepo.findAll())
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
                lessonsRepo.find { it.flows == name }
            call.response.etag(lessonItem.toString())
            call.respond(lessonItem)
        }
         post {
            val gr = call.receive<Flow>()
            flowsRepo.create(gr)
            call.respondText("Group stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (flowsRepo.delete(id)) {
                call.respondText("Group removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
        }

    }


