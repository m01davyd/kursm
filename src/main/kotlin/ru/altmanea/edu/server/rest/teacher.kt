package ru.altmanea.edu.server.rest

import Teachers
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import ru.altmanea.edu.server.model.Config
import ru.altmanea.edu.server.model.Config.Companion.teachersPath
import ru.altmanea.edu.server.repo.lessonsRepo
import ru.altmanea.edu.server.repo.teachersRepo


fun Route.teacher() =
    route(teachersPath) {
        get {
            if (!teachersRepo.isEmpty()) {
                call.respond(teachersRepo.findAll())
            } else {
                call.respondText("No students found", status = HttpStatusCode.NotFound)
            }
        }

        get("{lastname}") {
            val lastname = call.parameters["lastname"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )

            val lessonItem =
                lessonsRepo.find { it.lecturers.substringBefore(" ") == lastname }
            call.response.etag(lessonItem.toString())
            call.respond(lessonItem)
        }
        post {
            val teacher = call.receive<Teachers>()
            teachersRepo.create(teacher)
            call.respondText("Student stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (teachersRepo.delete(id)) {
                call.respondText("Student removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            teachersRepo[id] ?: return@put call.respondText(
                "No teacher with id $id",
                status = HttpStatusCode.NotFound
            )
            val newTeacher = call.receive<Teachers>()
            teachersRepo.update(id, newTeacher)
            call.respondText("Teacher updates correctly", status = HttpStatusCode.Created)
        }

    }
