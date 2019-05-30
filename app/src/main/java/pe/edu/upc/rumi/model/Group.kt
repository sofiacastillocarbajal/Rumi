package pe.edu.upc.rumi.model

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

data class Group (
    var groupId: String?,
    var name: String?,
    var owner: String?,
    var image: String?,
    var active: String?,
    var participants: List<Profile>?
) : Serializable {
    constructor() : this("", "", "", "", "", ArrayList<Profile>())

    companion object {
        fun from(jsonArray: JSONArray): ArrayList<Group> {
            val groups = ArrayList<Group>()
            for (i in 0..(jsonArray.length() - 1)) {
                groups.add(from(jsonArray.getJSONObject(i)))
            }
            return groups
        }

        fun from(jsonObject: JSONObject): Group {
            val group = Group()
            group.groupId = jsonObject.getString("groupId")
            group.name = jsonObject.getString("name")
            group.owner = jsonObject.getString("owner")
            group.image = jsonObject.getString("image")
            group.active = jsonObject.getString("active")
            group.participants = Profile.from(jsonObject.getJSONArray("participants"))
            return group
        }
    }
}