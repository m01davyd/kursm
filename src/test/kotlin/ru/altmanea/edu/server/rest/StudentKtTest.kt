//package ru.altmanea.edu.server.rest
//
//import io.ktor.application.*
//import io.ktor.http.*
//import io.ktor.server.testing.*
//import org.junit.Test
//import ru.altmanea.edu.server.main
//import ru.altmanea.edu.server.model.Config
//import ru.altmanea.edu.server.model.Student
//import ru.altmanea.edu.server.repo.RepoItem
//import kotlin.test.assertEquals
//import kotlinx.serialization.json.Json
//import kotlinx.serialization.encodeToString
//
//internal class StudentsKtTest {
//    @Test
//    fun testStudentRoute() {
//        withTestApplication(Application::main) {
//
//            val studentItems = handleRequest(HttpMethod.Get, Config.groupsPath).run {
//                assertEquals(HttpStatusCode.OK, response.status())
//                decodeBody<List<RepoItem<Groups>>>()
//            }
//            assertEquals(3, studentItems.size)
//            val sheldon = studentItems.find { it.elem.groups == "29z" }
//            check(sheldon != null)
//
//            handleRequest(HttpMethod.Get, Config.groupsPath + sheldon.uuid).run {
//                assertEquals(HttpStatusCode.OK, response.status())
//                assertEquals("29z", decodeBody<RepoItem<Groups>>().elem.groups)
//            }
//            handleRequest(HttpMethod.Get, Config.groupsPath + "100i").run {
//                assertEquals(HttpStatusCode.NotFound, response.status())
//            }
//            //
//            handleRequest(HttpMethod.Delete, Config.groupsPath + sheldon.uuid)
//                .apply {assertEquals(HttpStatusCode.Accepted, response.status())}
//
//
//
//        }
//    }
//}