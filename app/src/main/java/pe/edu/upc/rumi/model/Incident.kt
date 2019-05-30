package pe.edu.upc.rumi.model

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

data class Incident (
    var incidenceId: String?,
    var groupId: String?,
    var description: String?,
    var response: String?,
    var resolved: String?,
    var profile: Profile?
) : Serializable {
    constructor() : this("", "", "", "", "", Profile())

    companion object {
        fun from(jsonArray: JSONArray): List<Incident> {
            val incidents = ArrayList<Incident>()
            for (i in 0..(jsonArray.length() - 1)) {
                incidents.add(from(jsonArray.getJSONObject(i)))
            }
            return incidents
        }

        fun from(jsonObject: JSONObject): Incident {
            val incident = Incident()
            incident.incidenceId = jsonObject.getString("incidenceId")
            incident.groupId = jsonObject.getString("groupId")
            incident.description = jsonObject.getString("description")
            incident.resolved = jsonObject.getString("resolved")

            val r = jsonObject.getString("response")
            if (r == "null") incident.response = ""
            else incident.response = r

            incident.profile = Profile.from(jsonObject.getJSONObject("profile"))
            return incident
        }
    }
}