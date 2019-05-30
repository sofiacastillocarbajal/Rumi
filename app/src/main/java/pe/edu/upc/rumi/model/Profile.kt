package pe.edu.upc.rumi.model

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

data class Profile (
    var name: String?,
    var surname: String?,
    var phone: String?,
    var gender: String?,
    var birthdate: String?,
    var image: String?,
    var description: String?,
    var occupation: String?,
    var score: String?,
    var profileId: String?,
    var skills: ArrayList<String>?
) : Serializable {
    constructor() : this("", "", "", "", "", "", "", "", "", "", ArrayList<String>())

    fun skillsToString(): String {
        var s_skills = ""
        for (s in skills!!) {
            s_skills += "$s,"
        }
        if (s_skills.isNotEmpty())
            s_skills = s_skills.substring(0, s_skills.length - 1)

        return s_skills
    }

    companion object {
        fun from(jsonArray: JSONArray): List<Profile> {
            val profiles = ArrayList<Profile>()
            for (i in 0..(jsonArray.length() - 1)) {
                profiles.add(from(jsonArray.getJSONObject(i)))
            }
            return profiles
        }

        fun from(jsonObject: JSONObject): Profile {
            val profile = Profile()
            profile.name = jsonObject.getString("name")
            profile.surname = jsonObject.getString("surname")
            profile.phone = jsonObject.getString("phone")
            profile.gender = jsonObject.getString("gender")
            //profile.birthdate = jsonObject.getString("birthdate")
            profile.birthdate = "null"
            profile.image = jsonObject.getString("image")
            profile.description = jsonObject.getString("description")
            profile.occupation = jsonObject.getString("occupation")
            profile.score = jsonObject.getString("score")
            profile.profileId = jsonObject.getString("profileId")

            val skillsJSONArray = jsonObject.getJSONArray("skills")
            for (i in 0..(skillsJSONArray.length() - 1)) {
                profile.skills?.add(skillsJSONArray.getString(i))
            }

            return profile
        }
    }
}