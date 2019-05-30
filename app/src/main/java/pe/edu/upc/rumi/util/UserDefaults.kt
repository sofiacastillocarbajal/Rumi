package pe.edu.upc.rumi.util

import android.content.Context
import pe.edu.upc.rumi.model.Profile

object UserDefaults {
    var profileId: String? = ""
    var name: String? = ""
    var surname: String? = ""
    var phone: String? = ""
    var gender: String? = ""
    var birthdate: String? = ""
    var image: String? = ""
    var description: String? = ""
    var occupation: String? = ""
    var skills: String? = ""
    var score: String? = ""
    var role: String? = ""
    var token: String? = ""

    fun toProfile(): Profile {
        var perfil = Profile()
        perfil.profileId = profileId
        perfil.name = name
        perfil.surname = surname
        perfil.phone = phone
        perfil.gender = gender
        perfil.image = image
        perfil.description = description
        perfil.occupation = occupation
        perfil.score = score
        perfil.skills = skillsStringToList()
        return perfil
    }

    private fun skillsStringToList(): ArrayList<String> {
        var l = ArrayList<String>()
        var list = skills?.split(",")
        for (s in list!!){
            if (s != ""){
                l.add(s)
            }
        }
        return l
    }

    fun saveInSharedPreferences(context: Context) {
        val sh = context.getSharedPreferences("RumiApp", Context.MODE_PRIVATE)
        val myEdit = sh.edit()
        myEdit.putString("profileId", profileId)
        myEdit.putString("name", name)
        myEdit.putString("surname", surname)
        myEdit.putString("phone", phone)
        myEdit.putString("gender", gender)
        myEdit.putString("birthdate", birthdate)
        myEdit.putString("image", image)
        myEdit.putString("description", description)
        myEdit.putString("occupation", occupation)
        myEdit.putString("skills", skills)
        myEdit.putString("score", score)
        myEdit.putString("role", role)
        myEdit.putString("token", token)
        myEdit.apply()
    }

    fun loadFromSharedPreferences(context: Context) {
        val sh1 = context.getSharedPreferences("RumiApp", Context.MODE_PRIVATE)
        profileId = sh1.getString("profileId", "")
        name = sh1.getString("name", "")
        surname = sh1.getString("surname", "")
        phone = sh1.getString("phone", "")
        gender = sh1.getString("gender", "")
        birthdate = sh1.getString("birthdate", "")
        image = sh1.getString("image", "")
        description = sh1.getString("description", "")
        occupation = sh1.getString("occupation", "")
        skills = sh1.getString("skills", "")
        score = sh1.getString("score", "")
        role = sh1.getString("role", "")
        token = sh1.getString("token", "")
    }

    fun clearFromSharedPreferences(context: Context) {
        val sh = context.getSharedPreferences("RumiApp", Context.MODE_PRIVATE)
        val myEdit = sh.edit()
        myEdit.putString("profileId", "")
        myEdit.putString("name", "")
        myEdit.putString("surname", "")
        myEdit.putString("phone", "")
        myEdit.putString("gender", "")
        myEdit.putString("birthdate", "")
        myEdit.putString("image", "")
        myEdit.putString("description", "")
        myEdit.putString("occupation", "")
        myEdit.putString("skills", "")
        myEdit.putString("score", "")
        myEdit.putString("role", "")
        myEdit.putString("token", "")
        myEdit.apply()
    }
}