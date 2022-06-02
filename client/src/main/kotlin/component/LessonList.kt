package component


import kotlinext.js.jso
import kotlinx.css.*
import kotlinx.html.INPUT
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.Serializable
import kotlinx.html.*
import react.Props
import react.dom.*
import react.fc
import react.query.useMutation
import react.query.useQuery
import react.query.useQueryClient
import react.router.dom.Link
import react.router.useParams
import react.useRef
import ru.altmanea.edu.server.model.*
import ru.altmanea.edu.server.model.Config.Companion.lessonURL
import styled.css
import styled.styledTable
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios

import kotlin.js.json

external interface LessonListProps : Props {
    var lessons: List<Item<Lesson>>
    var deleteLesson: (Int) -> Unit
    var addLesson: (String, String, String, String, String, String ,String, Int?) -> Unit
}
fun fcLessonList() = fc("LessonList") { props: LessonListProps ->

    val nameRef = useRef<INPUT>()
    val typeRef = useRef<INPUT>()
    val numRef = useRef<INPUT>()
    val dayRef = useRef<INPUT>()
    val flowsRef = useRef<INPUT>()
    val lectRef = useRef<INPUT>()
    val typeweekRef = useRef<INPUT>()
    val audRef = useRef<INPUT>()
    h3 { +"Элементы расписания" }
    styledTable {
        css {
            descendants("tr", "td","th") {
                border = "1px solid black"
            }
        }
        tr{
            th{+"Время"}
            th{+"День недели, тип"}
            th{+"Занятие, тип"}
            th{+"Группа"}
            th{+"Преподаватель"}
            th{+"Аудитория"}
            th{+" "}
        }
        props.lessons.mapIndexed { index, item ->

            tr {
                td { +item.elem.time }
                td {
                    +item.elem.dayOfWeek
                    br {}
                    +item.elem.typeOfWeek
                }
                td {
                    Link {
                        attrs.to = "/lesson/${item.uuid}"
                    +item.elem.name
                    br {}
                    +item.elem.type}
                }
                td { +item.elem.flows }
                td { +item.elem.lecturers }
                td { +"${item.elem.aud!!}" }
                td {
                    button {
                        +"X"
                        attrs.onClickFunction = {
                            props.deleteLesson(index)
                        }
                    }
                }
            }

        }
    }
    span {
        p {
            +"Занятие: "
            input {
                ref = nameRef
            }
        }
        p {
            +"Группа: "
            input {
                ref = flowsRef
            }
        }
        p {
            +"День недели: "
            input {
                ref = dayRef
            }
        }
        p {
            +"Преподаватель: "
            input {
                ref = lectRef
            }
        }
        p {
            +"Тип занятия: "
            input {
                ref = typeRef
            }
        }
        p {
            +"Тип недели: "
            input {
                ref = typeweekRef
            }
        }
        p {
            +"Время пары: "
            input {
                ref = numRef
            }
        }
        p {
            +"Аудитория: "
            input {
                ref = audRef
            }
        }
        button {
            +"Добавить урок"
            attrs.onClickFunction = {
                nameRef.current?.value?.let { name ->
                    typeRef.current?.value?.let { type ->
                        numRef.current?.value?.let { num ->
                            dayRef.current?.value?.let { day ->
                                typeweekRef.current?.value?.let { week ->
                                    flowsRef.current?.value?.let { flow ->
                                        lectRef.current?.value?.let { lect ->
                                            audRef.current?.value?.let { aud ->
                                                props.addLesson(name, type, num, day, week, flow, lect, aud.toInt())
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


    @Serializable
    class ClientItemLesson(
        override val elem: Lesson,
        override val uuid: String,
        override val etag: Long
    ) : Item<Lesson>

    fun fcContainerLessonList() = fc("Lesson") { _: Props ->
        val queryClient = useQueryClient()
        val asl = useParams()
        val id = asl["id"] ?: ""


        val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Lesson>>>, Any>(
            "Lesson",
            {
                axios<Array<Lesson>>(jso {
                    url = lessonURL
                })
            }
        )

        val deleteLessonMutation = useMutation<Any, Any, Any, Any>(
            { lessonItem: Item<Lesson> ->
                axios<String>(jso {
                    url = "$lessonURL/${lessonItem.uuid}"
                    method = "Delete"
                })
            },
            options = jso {
                onSuccess = { _: Any, _: Any, _: Any? ->
                    queryClient.invalidateQueries<Any>(query)
                }
            }
        )

        val addLessonMutation = useMutation<Any, Any, Any, Any>(
            { lesson: Lesson ->
                axios<String>(jso {
                    url = lessonURL
                    method = "Post"
                    headers = json(
                        "Content-Type" to "application/json"
                    )
                    data = JSON.stringify(lesson)
                })
            },
            options = jso {
                onSuccess = { _: Any, _: Any, _: Any? ->
                    queryClient.invalidateQueries<Any>(query)
                }
            }
        )

        if (query.isLoading) div { +"Loading .." }
        else if (query.isError) div { +"Error!" }
        else {
            val items = query.data?.data?.toList() ?: emptyList()
            child(fcLessonList()) {
                attrs.lessons = items
                attrs.deleteLesson = {
                    deleteLessonMutation.mutate(items[it], null)
                }
                attrs.addLesson = { n, t, num, d, w, f, l, a ->
                    addLessonMutation.mutate(Lesson( n, t, num, d, w, f, l,a), null)
                }
            }

        }
    }




