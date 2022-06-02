package ru.altmanea.edu.server.rest

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import ru.altmanea.edu.server.model.Config.Companion.groupsPath
import ru.altmanea.edu.server.model.Config.Companion.lessonPath
import ru.altmanea.edu.server.model.Lesson
import ru.altmanea.edu.server.repo.lessonsRepo

fun Route.lesson() {
    route(lessonPath) {
        get {
            if (!lessonsRepo.isEmpty()) {
                call.respond(lessonsRepo.findAll())
            } else {
                call.respondText("No groups found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val lesson =
                lessonsRepo[id] ?: return@get call.respondText(
                    "No student with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(lesson)
        }
        post {
            val student = call.receive<Lesson>()
            lessonsRepo.create(student)
            call.respondText("Student stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (lessonsRepo.delete(id)) {
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
            lessonsRepo[id] ?: return@put call.respondText(
                "No student with id $id",
                status = HttpStatusCode.NotFound
            )
            val newLesson = call.receive<Lesson>()
            lessonsRepo.update(id, newLesson)
            call.respondText("Student updates correctly", status = HttpStatusCode.Created)
        }
    }
}