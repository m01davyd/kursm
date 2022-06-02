package ru.altmanea.edu.server.model

import kotlinx.serialization.Serializable

@Serializable
data class Lesson(
    val name: String,
    val type: String,
    val time: String,
    val dayOfWeek: String,
    val typeOfWeek: String,
    val flows: String ,
    val lecturers: String,
    val aud: Int?=null
)
{
    val full: String
        get()="$name \t" +
                "$type \t" +
                "$time\t" +
                "$dayOfWeek\t" +
                "$typeOfWeek\t" +
                "$flows \t" +
                "$lecturers"+
                "$aud"
}


enum class LessonType(private val value: String) {
    LECTURE("Лекция"),
    LABORATORY("Лабораторная"),
    PRACTICE("Практика"),
    KSR("КСР"),
    KRB("КРБ");

    override fun toString() = value
}
enum class DayOfWeek(private val value: String){
    Mon("Понедельник"),
    Tue("Вторник"),
    Wen("Среда"),
    Thu("Четверг"),
    Fri("Пятница"),
    Sat("Суббота");

    override fun toString() = value
}
val dayOfWeek= listOf(DayOfWeek.Mon,DayOfWeek.Tue,DayOfWeek.Wen,DayOfWeek.Thu,DayOfWeek.Fri,DayOfWeek.Sat)
enum class Number(private val type: String){
    First("08:00-09:30"),
    Second("09:45-11:15"),
    Third("11:30-13:00"),
    Fourth("13:55-15:25"),
    Fifth("15:40-17:10");

    override fun toString() = type
}
val number= listOf(Number.First,Number.Second,Number.Third,Number.Fourth,Number.Fifth)
enum class TypeOfWeek(private val type:String){
    ODD("Нечетная"),
    EVEN("Четная");

    override fun toString() = type
}
val typeOfWeek= listOf(TypeOfWeek.ODD,TypeOfWeek.EVEN)
