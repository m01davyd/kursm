package component

import kotlinext.js.jso
import kotlinx.css.border
import react.*
import react.dom.*
import react.query.useQuery
import react.router.useParams
import ru.altmanea.edu.server.model.*
import wrappers.AxiosResponse
import wrappers.QueryError
import wrappers.axios
import styled.css
import styled.styledTable

external interface AudProps : Props {
    var lessons: List<Item<Lesson>>
}

fun fcAud() = fc("Aud") { props: AudProps ->

    val groupsParams = useParams()
    val name = groupsParams["name"] ?: "Route param error"
    h3{
        +"Список занятий для $name аудитории"
    }
    div {
        styledTable {
            css {
                //width="60px"

                descendants("tr", "td", "th") {
                    border = "1px solid black"
                }
            }
            tr {
                th { +"Время" }
                th { +"День недели, тип" }
                th { +"Занятие, тип" }
                th { +"Группа" }
                th { +"Преподаватель" }
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
                    td { +item.elem.flows }
                    td { +item.elem.lecturers }
                }
            }
        }
    }
}
fun fcContainerAud() = fc("ContainerAud") { _: Props ->
    val groupsParams = useParams()
    val name = groupsParams["name"] ?: "Route param error"
    val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Lesson>>>, Any>(
        "aud",
        {
            axios<Array<Lesson>>(jso {
                url = Config.audsPath + name
            })
        }
    )
    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items = query.data?.data?.toList() ?: emptyList()
         child(fcAud()) {
            attrs.lessons = items
        }
    }
}