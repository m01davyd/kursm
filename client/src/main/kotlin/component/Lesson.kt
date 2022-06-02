package component

import kotlinext.js.jso
import kotlinx.html.INPUT
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.w3c.dom.events.Event
import react.*
import react.dom.*
import react.query.useMutation
import react.query.useQuery
import react.query.useQueryClient
import react.router.useParams
import ru.altmanea.edu.server.model.Config
import ru.altmanea.edu.server.model.Item
import ru.altmanea.edu.server.model.Lesson
import wrappers.QueryError
import wrappers.axios
import wrappers.fetchText

import kotlin.js.json

external interface LessonProps : Props {
    var lessons: Item<Lesson>
    var update: (String, String, String, String, String ,String, String, Int?) -> Unit
}


fun fcLesson() = fc("Lesson") { props: LessonProps ->

    val nameRef = useRef<INPUT>()
    val typeRef = useRef<INPUT>()
    val numRef = useRef<INPUT>()
    val dayRef = useRef<INPUT>()
    val typeweekRef = useRef<INPUT>()
    val flowsRef = useRef<INPUT>()
    val lectRef = useRef<INPUT>()
    val audRef=useRef<INPUT>()


    val (name, setName) = useState(props.lessons.elem.name)
    val (type, setType) = useState(props.lessons.elem.type)
    val (num, setNum) = useState(props.lessons.elem.time)
    val (day, setDay) = useState(props.lessons.elem.dayOfWeek)
    val (flow, setFlow) = useState(props.lessons.elem.flows)
    val (lect, setLect) = useState(props.lessons.elem.lecturers)
    val (week, setWeek) = useState(props.lessons.elem.typeOfWeek)
    val (aud, setAud) = useState(props.lessons.elem.aud.toString())


    fun onInputEdit(setter: StateSetter<String>, ref: MutableRefObject<INPUT>) =
        { _: Event ->
            setter(ref.current?.value ?: "ERROR!")
        }

    span {

        p {
            +"Название дисциплины:"
            input {
                ref = nameRef
                attrs.value = name
                attrs.onChangeFunction = onInputEdit(setName, nameRef)
            }
        }
        p {
            +"Группа:"
            input {
                ref = flowsRef
                attrs.value = flow
                attrs.onChangeFunction = onInputEdit(setFlow, flowsRef)
            }
        }
        p {
            +"День недели"
            input {
                ref = dayRef
                attrs.value = day
                attrs.onChangeFunction = onInputEdit(setDay, dayRef)
            }
        }
        p {
            +"Преподаватель:"
            input {
                ref = lectRef
                attrs.value = lect
                attrs.onChangeFunction = onInputEdit(setLect, lectRef)
            }
        }
        p {
            +"Тип занятия:"
            input {
                ref = typeRef
                attrs.value = type
                attrs.onChangeFunction = onInputEdit(setType, typeRef)
            }
        }
        p {
            +"тип недели"
            input {
                ref = typeweekRef
                attrs.value = week
                attrs.onChangeFunction = onInputEdit(setWeek, typeweekRef)
            }
        }
        p {
            +"Время пары:"
            input {
                ref = numRef
                attrs.value = num
                attrs.onChangeFunction = onInputEdit(setNum, numRef)
            }
        }
        p {
            +"Аудитория:"
            input {
                ref = audRef
                attrs.value = aud
                attrs.onChangeFunction = onInputEdit(setAud, audRef)
            }
        }


    }
    button {
        +"Обновить"
        attrs.onClickFunction = {
            nameRef.current?.value?.let { name ->
                typeRef.current?.value?.let { type ->
                    numRef.current?.value?.let { num ->
                        dayRef.current?.value?.let { day ->
                            typeweekRef.current?.value?.let { week ->
                                flowsRef.current?.value?.let { flow ->
                                    lectRef.current?.value?.let { lect ->
                                        audRef.current?.value?.let { aud ->
                                            props.update(name, type, num, day, week, flow, lect, aud.toInt())
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

class MutationDataL(
    val oldLesson: Item<Lesson>,
    val newLesson: Lesson,
)

fun fcContainerLesson() = fc("ContainerLesson") { _: Props ->
    val lParams = useParams()
    val queryClient = useQueryClient()

    val lId = lParams["id"] ?: "Route param error"

    val query = useQuery<String, QueryError, String, String>(
        lId,
        { fetchText("${Config.lessonURL}/${lId}") }
    )

    val updateLessonMutation = useMutation<Any, Any, MutationDataL, Any>(
        { mutationDataL ->
            axios<String>(jso {
                url = "${Config.lessonURL}/${mutationDataL.oldLesson.uuid}"
                method = "Put"
                headers = json(
                    "Content-Type" to "application/json",
                )
                data = JSON.stringify(mutationDataL.newLesson)
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(lId)
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val lItem: ClientItemLesson =
            Json.decodeFromString(query.data ?: "")
        child(fcLesson()) {
            attrs.lessons =lItem
            attrs.update = {n, t, num, d, w, f, l,a  ->
                updateLessonMutation.mutate(MutationDataL(lItem, Lesson(n, t, num, d, w, f, l,a)), null)
            }
        }
    }
}


