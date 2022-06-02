package ru.altmanea.edu.server.repo

import Teachers
import ru.altmanea.edu.server.model.Config

val teachersRepo = ListRepo<Teachers>()

val teachersRepoTestData = listOf(
    Teachers("Альтман", "Евгений", "Анатольевич"),
    Teachers("Елизаров", "Дмитрий", "Александрович"),
    Teachers("Малютин", "Андрей", "Геннадьевич"),
    Teachers("Каштанов","Алексей","Леонидович"),
    Teachers("Кладов", "Эдуард", "Владимирович"),
    Teachers("Окишев", "Андрей", "Сергеевич")

)
