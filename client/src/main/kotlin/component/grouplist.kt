package component

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
import ru.altmanea.edu.server.model.Config
import ru.altmanea.edu.server.model.Config.Companion.groupsURL
import ru.altmanea.edu.server.model.Flow
import ru.altmanea.edu.server.model.Item
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios
import kotlin.js.json

external interface GroupsListProps : Props {
    var groups: List<Item<Flow>>
    var addGroup: (String) -> Unit
    var deleteGroup: (Int) -> Unit
}
fun fcGroupsList() = fc("GroupList") { props: GroupsListProps ->

    val nameRef = useRef<INPUT>()

    span {
        p {
            +"Группа: "
            input {
                ref = nameRef
            }
        }

        button {
            +"Добавить группу"
            attrs.onClickFunction = {
                nameRef.current?.value?.let {
                            props.addGroup(it)
                        }
                    }
                }
            }

    h3 { +"Группы" }
    ol {
        props.groups.mapIndexed { index, groupItem ->
            li {
                   Link {
                    attrs.to = "/groups/${groupItem.elem.name}"
                    +"${groupItem.elem.name} \t"
                }
                button {
                    +"X"
                    attrs.onClickFunction = {
                        props.deleteGroup(index)
                    }
                }
            }
        }
    }
}

fun fcContainerGroupsList() = fc("QueryGroupsList") { _: Props ->
    val queryClient = useQueryClient()

    val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Flow>>>, Any>(
        "GroupsList",
        {
            axios<Array<Flow>>(jso {
                url = groupsURL
            })
        }
    )

    val addGroupMutation = useMutation<Any, Any, Any, Any>(
        { gr: Flow ->
            axios<String>(jso {
                url = groupsURL
                method = "Post"
                headers = json(
                    "Content-Type" to "application/json"
                )
                data = JSON.stringify(gr)
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(query)
            }
        }
    )

    val deleteGroupMutation = useMutation<Any, Any, Any, Any>(
        { groupItem: Item<Flow> ->
            axios<String>(jso {
                url = "${Config.groupsURL}/${groupItem.uuid}"
                method = "Delete"
            })
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>("GroupsList")
            }
        }
    )

    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items = query.data?.data?.toList() ?: emptyList()
        child(fcGroupsList()) {
            attrs.groups = items
            attrs.addGroup = {
                addGroupMutation.mutate(Flow(it), null)
            }
            attrs.deleteGroup = {
                deleteGroupMutation.mutate(items[it], null)
            }
        }

    }
}
