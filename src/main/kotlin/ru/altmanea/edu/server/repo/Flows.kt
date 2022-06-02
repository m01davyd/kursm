package ru.altmanea.edu.server.repo


import ru.altmanea.edu.server.model.*

val flowsRepo = ListRepo<Flow>()

val flowsRepoTestData=listOf(
    Flow("29з"),
    Flow("29м"),
    Flow("29и"),
    Flow("29зм"),
    Flow("29зим")
)
