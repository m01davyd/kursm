
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
import react.router.dom.Link
import ru.altmanea.edu.server.model.Lesson
import styled.css
import styled.styledTable

external interface TeacherProps : Props {
    var lessons: List<Item<Lesson>>
}

fun fcTeacher() = fc("Teacher") { props: TeacherProps ->

    styledTable {
        css {
            //width="60px"
            descendants("tr", "td","th") {
                border = "1px solid black"
            }
        }
        tr{
            th{+"Время"}
            th{+"День недели, тип"}
            th{+"Занятие, тип"}
            th{+"Группа"}
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
                td { +item.elem.flows }
                td { +"${item.elem.aud!!}" }
            }
        }
    }
}
fun fcContainerTeacher() = fc("ContainerTeacher") { _: Props ->
    val tParams = useParams()
    val lastname = tParams["lastname"] ?: "Route param error"
    val query = useQuery<Any, QueryError, AxiosResponse<Array<Item<Lesson>>>, Any>(
        "teacher",
        {
            axios<Array<Lesson>>(jso {
                url = Config.teachersPath + lastname
            })
        }
    )
    if (query.isLoading) div { +"Loading .." }
    else if (query.isError) div { +"Error!" }
    else {
        val items = query.data?.data?.toList() ?: emptyList()
         child(fcTeacher()) {
            attrs.lessons = items
        }
    }
}