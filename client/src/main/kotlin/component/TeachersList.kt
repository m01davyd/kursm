package component

import Teachers
import kotlinext.js.jso
import kotlinx.html.INPUT
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.Serializable
import react.Props
import react.dom.*
import react.fc
import react.query.useMutation
import react.query.useQuery
import react.query.useQueryClient
import react.router.dom.Link
import react.router.useParams
import react.useRef
import ru.altmanea.edu.server.model.Item
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios
import ru.altmanea.edu.server.model.Config.Companion.teachersURL
import kotlin.js.json

external interface TeacherListProps : Props {
    var teachers: List<Item<Teachers>>
    var addTeacher: (String, String, String) -> Unit
    var deleteTeacher: (Int) -> Unit
}

fun fcTeachersList() = fc("TeachersList") { props: TeacherListProps ->
    val lastnameRef = useRef<INPUT>()
    val firstnameRef = useRef<INPUT>()
    val middlenameRef = useRef<INPUT>()

    span {
        p {
            +"Фамилия: "
            input {
                ref = lastnameRef
            }
        }
        p {
            +"Имя: "
            input {
                ref = firstnameRef
            }
        }
        p {
            +"Отчество: "
            input {
                ref = middlenameRef
            }
        }

        button {
            +"Добавить преподавателя"
            attrs.onClickFunction = {
                lastnameRef.current?.value?.let { lastname ->
                    firstnameRef.current?.value?.let { firstname ->
                        middlenameRef.current?.value?.let { middle ->
                            props.addTeacher(lastname, firstname, middle)
                        }
                    }
                }
            }
        }
    }

    h3 { +"Преподаватели" }
    ol {
        props.teachers.mapIndexed { index, teacherItem ->
            li {
                val teacher = Teachers(teacherItem.elem.firstname, teacherItem.elem.lastname,teacherItem.elem.middleName)
                Link {
                    attrs.to = "/teachers/${teacherItem.elem.lastname}"
                   +"${teacherItem.elem.fullname} \t"
                }
                button {
                    +"X"
                    attrs.onClickFunction = {
                        props.deleteTeacher(index)
                    }
                }
            }
        }
    }
}

@Serializable
class ClientItemTeacher(
    override val elem: Teachers,
    override val uuid: String,
    override val etag: Long
) : Item<Teachers>

fun fcContainerTeachersList() = fc("QueryTeachersList") { _: Props ->
    val queryClient = useQueryClient()
    val asl = useParams()
    val id = asl["id"] ?: ""
    val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Teachers>>>, Any>(
        "TeachersList",
        {
            axios<Array<Teachers>>(jso {
                url = teachersURL
            })
        }
    )

    val addTeacherMutation = useMutation<Any, Any, Any, Any>(
        { teachers: Teachers ->
            axios<String>(jso {
                url = teachersURL
                method = "Post"
                headers = json(
                    "Content-Type" to "application/json"
                )
                data = JSON.stringify(teachers)
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>("TeachersList")
            }
        }
    )

    val deleteTeacherMutation = useMutation<Any, Any, Any, Any>(
        { teacherItem: Item<Teachers> ->
            axios<String>(jso {
                url = "$teachersURL/${teacherItem.uuid}"
                method = "Delete"
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>("TeachersList")
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items = query.data?.data?.toList() ?: emptyList()
        child(fcTeachersList()) {
            attrs.teachers = items
            attrs.addTeacher = { l, f, m ->
                addTeacherMutation.mutate(Teachers(l, f, m), null)
            }
            attrs.deleteTeacher = {
                deleteTeacherMutation.mutate(items[it], null)
            }
        }
    }
}
