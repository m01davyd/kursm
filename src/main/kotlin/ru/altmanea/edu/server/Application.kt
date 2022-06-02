package ru.altmanea.edu.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.altmanea.edu.server.model.Config
import ru.altmanea.edu.server.repo.*
import ru.altmanea.edu.server.rest.aud
import ru.altmanea.edu.server.rest.group

//import ru.altmanea.edu.server.repo.groupsRepo

import ru.altmanea.edu.server.rest.lesson
import ru.altmanea.edu.server.rest.teacher

fun main() {
    embeddedServer(
        Netty,
        port = Config.serverPort,
        host = Config.serverDomain,
        watchPaths = listOf("classes", "resources")
    ) {
        main()
    }.start(wait = true)
}

fun Application.main(test: Boolean = true) {
    if(test) {
        lessonsRepoTestData.forEach { lessonsRepo.create(it) }
        flowsRepoTestData.forEach{ flowsRepo.create(it)}
        teachersRepoTestData.forEach { teachersRepo.create(it) }
        AudRepoTestData.forEach { AudsRepo.create(it) }
    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        lesson()
        teacher()
        index()
        group()
        aud( )

    }
}