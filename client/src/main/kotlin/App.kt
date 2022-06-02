import component.*
import kotlinx.browser.document
import react.createElement
import react.dom.br
import react.dom.render
import react.query.QueryClient
import react.query.QueryClientProvider
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.dom.Link
import wrappers.cReactQueryDevtools

val queryClient = QueryClient()

fun main() {
    render(document.getElementById("root")!!) {
        HashRouter {
            QueryClientProvider {
                attrs.client = queryClient
                Link {
                    attrs.to = "/teachers"
                    +"Teachers"
                }
                br { }
                Link {
                    attrs.to = "/groups"
                    +"Groups"
                }
                br{

                }
                Link{
                    attrs.to="/lesson"
                    +"Lesson"
                }
                br{

                }
                Link{
                    attrs.to="/auds"
                    +"Audience"
                }
                Routes {
                    Route {
                        attrs.path = "/teachers"
                        attrs.element =
                            createElement(fcContainerTeachersList())
                    }
                    Route {
                        attrs.path = "/teachers/:lastname"
                        attrs.element =
                            createElement(fcContainerTeacher())
                    }
                    Route {
                        attrs.path = "/groups"
                        attrs.element =
                            createElement(fcContainerGroupsList())
                    }
                    Route {
                        attrs.path = "/groups/:name"
                        attrs.element =
                            createElement(fcContainerGroup())
                    }
                    Route{
                        attrs.path="/lesson"
                        attrs.element=
                            createElement(fcContainerLessonList())
                    }
                    Route{
                        attrs.path="/lesson/:id"
                        attrs.element= createElement(fcContainerLesson())
                    }
                    Route {
                        attrs.path = "/auds"
                        attrs.element =
                            createElement(fcContainerAudList())
                    }
                    Route {
                        attrs.path = "/auds/:name"
                        attrs.element =
                            createElement(fcContainerAud())
                    }
                }
                child(cReactQueryDevtools()) {}
            }
        }
    }
}

