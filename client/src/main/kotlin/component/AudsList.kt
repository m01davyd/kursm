package component
import Aud
import kotlinext.js.jso
import kotlinx.html.INPUT
import kotlinx.html.js.onClickFunction
import react.Props
import react.dom.*
import react.*
import react.query.*
import react.router.dom.Link
import react.useRef
import ru.altmanea.edu.server.model.Config
import ru.altmanea.edu.server.model.Config.Companion.audsURL
import ru.altmanea.edu.server.model.Item
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios
import kotlin.js.json

external interface AudsListProps : Props {
    var auds: List<Item<Aud>>
    var addAud: (Int?) -> Unit
    var deleteAud: (Int) -> Unit
}
fun fcAudsList() = fc("AudsList") { props: AudsListProps ->

    val numRef = useRef<INPUT>()
    span {
        p {
            +"Аудитория: "
            input {
                ref = numRef
            }
        }
        button {
            +"Добавить аудиторию"
            attrs.onClickFunction = {
                numRef.current?.value?.let {
                    props.addAud(it.toInt())
                }
            }
        }
    }
    h3 { +"Аудитории" }
    ol {
        props.auds.mapIndexed { index, audItem ->
            li {
                Link {
                    attrs.to = "/auds/${audItem.elem.classroom}"
                    +"${audItem.elem.classroom} \t"
                }
                button {
                    +"X"
                    attrs.onClickFunction = {
                        props.deleteAud(index)
                    }
                }
            }
        }
    }
}

fun fcContainerAudList() = fc("QueryAudList") { _: Props ->
    val queryClient = useQueryClient()
    val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Aud>>>, Any>(
        "AudList",
        {
            axios<Array<Aud>>(jso {
                url = audsURL
            })
        }
    )
    val addAudMutation = useMutation<Any, Any, Any, Any>(
        { aud: Aud ->
            axios<String>(jso {
                url = audsURL
                method = "Post"
                headers = json(
                    "Content-Type" to "application/json"
                )
                data = JSON.stringify(aud)
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(query)
            }
        }
    )

    val deleteAudMutation = useMutation<Any, Any, Any, Any>(
        { audItem: Item<Aud> ->
            axios<String>(jso {
                url = "${Config.audsURL}/${audItem.uuid}"
                method = "Delete"
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>("AudList")
            }
        }
    )
    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items = query.data?.data?.toList() ?: emptyList()
        child(fcAudsList()) {
            attrs.auds = items
            attrs.addAud = {
                addAudMutation.mutate(Aud(it!!), null)
            }
            attrs.deleteAud = {
                deleteAudMutation.mutate(items[it], null)
            }
        }
    }
}
