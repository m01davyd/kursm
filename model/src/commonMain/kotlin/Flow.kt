package ru.altmanea.edu.server.model

import kotlinx.serialization.Serializable

@Serializable
class Flow(
    val name: String
)
{
    val fullname: String = "$name"
}