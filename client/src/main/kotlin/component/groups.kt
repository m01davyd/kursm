
package component

import kotlinext.js.jso
import kotlinx.css.border
import react.*
import react.dom.*
import react.query.useQuery
import react.query.useQueryClient
import react.router.useParams
import ru.altmanea.edu.server.model.Config
import ru.altmanea.edu.server.model.Item
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios
import ru.altmanea.edu.server.model.Lesson
import styled.css
import styled.styledTable

external interface GroupProps : Props {
    var lessons: List<Item<Lesson>>
    }

fun fcGroup() = fc("Group") { props: GroupProps ->

    val groupsParams = useParams()
    val name = groupsParams["name"] ?: "Route param error"
    h3{
        +"Список занятий для $name"
    }
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
            th{+"Преподаватель"}
            th{+"Аудитория"}
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
                    +item.elem.name
                    br {}
                    +item.elem.type
                }
                td { +item.elem.lecturers }
                td { +"${item.elem.aud!!}" }
            }
        }
    }
}
fun fcContainerGroup() = fc("ContainerGroup") { _: Props ->
    val groupsParams = useParams()
    val name = groupsParams["name"] ?: "Route param error"

    val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Lesson>>>, Any>(
       "group",
        {
            axios<Array<Lesson>>(jso {
                url = Config.groupsPath + name
            })
        }
    )
    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items = query.data?.data?.toList() ?: emptyList()
         child(fcGroup()) {
            attrs.lessons = items
        }
    }
}