package ru.altmanea.edu.server.repo

import ru.altmanea.edu.server.model.*
import ru.altmanea.edu.server.model.Number
val lessonsRepo = ListRepo<Lesson>()

val teach= teachersRepoTestData
val fl=flowsRepoTestData
val a=AudRepoTestData
val lessonsRepoTestData = listOf(
//    Lesson("Физическая культура", LessonType.LECTURE.toString(),
//        Number.First.toString(), DayOfWeek.Mon.toString(), TypeOfWeek.ODD.toString(),
//        fl[4].name,teach[4].fullname,a[10].classroom),//,
    Lesson("Тестирование программ", LessonType.LABORATORY.toString(),
        Number.Second.toString(), DayOfWeek.Mon.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
        teach[1].fullname,a[9].classroom),//,
    Lesson("Инфокоммуникационные системы и сети", LessonType.LABORATORY.toString(),
        Number.Third.toString(), DayOfWeek.Mon.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
        teach[2].fullname,a[3].classroom),//,
    Lesson("Компьютерные комплексы и сети", LessonType.LABORATORY.toString(),
        Number.Second.toString(), DayOfWeek.Tue.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
        teach[5].fullname,a[3].classroom),
//    Lesson("Инфокоммуникационные системы и сети", LessonType.LECTURE.toString(),
//        Number.First.toString(), DayOfWeek.Wen.toString(), TypeOfWeek.ODD.toString(),
//        fl[3].name,teach[2].fullname,a[3].classroom),
//    Lesson("Инженерия инфокоммуникационных систем", LessonType.LABORATORY.toString(),
//        Number.Second.toString(), DayOfWeek.Wen.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
//        teach[3].fullname,a[8].classroom),
//    Lesson("Тестирование программ", LessonType.LECTURE.toString(),
//        Number.First.toString(), DayOfWeek.Thu.toString(), TypeOfWeek.ODD.toString(),fl[3].name,
//        teach[1].fullname,a[9].classroom),
//    Lesson("Компьютерные комплексы и сети", LessonType.LECTURE.toString(),
//        Number.Second.toString(), DayOfWeek.Thu.toString(), TypeOfWeek.ODD.toString(),fl[3].name,
//        teach[5].fullname,a[3].classroom),
//    Lesson("Тестирование программ", LessonType.LABORATORY.toString(),
//        Number.Third.toString(), DayOfWeek.Thu.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
//        teach[1].fullname,a[9].classroom),
    Lesson("Прикладное программирование", LessonType.LECTURE.toString(),
        Number.Fourth.toString(), DayOfWeek.Thu.toString(), TypeOfWeek.ODD.toString(),
        fl[0].name,teach[0].fullname,a[4].classroom),
//    Lesson("Инфокоммуникационные системы и сети", LessonType.LABORATORY.toString(),
//        Number.Fifth.toString(), DayOfWeek.Thu.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
//        teach[2].fullname,a[3].classroom),
//    Lesson("Прикладное программирование", LessonType.LECTURE.toString(),
//        Number.First.toString(), DayOfWeek.Sat.toString(), TypeOfWeek.ODD.toString(),fl[3].name,
//        teach[0].fullname,a[3].classroom),
    Lesson("Инфокоммуникационные системы и сети", LessonType.KSR.toString(),
        Number.Second.toString(), DayOfWeek.Sat.toString(), TypeOfWeek.ODD.toString(),fl[0].name,
        teach[2].fullname,a[8].classroom)
)
