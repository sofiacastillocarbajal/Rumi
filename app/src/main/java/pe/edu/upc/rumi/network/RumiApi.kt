package pe.edu.upc.rumi.network

class RumiApi {
    companion object {
        private const val BASE_URL = "https://rumiapi.azurewebsites.net/api"
        val groupsUrl = "$BASE_URL/groups"
        val tasksUrl = "$BASE_URL/tasks"
        val incidencesUrl = "$BASE_URL/incidences"
        val signInUrl = "$BASE_URL/account/signin"
        val signUp = "$BASE_URL/account/signup"
        val validUsernameUrl = "$BASE_URL/account/username"

        fun incidencesByGroup(groupId: String?): String {
            return "$groupsUrl/$groupId/incidences"
        }

        fun incidencesById(incidenceId: String?): String {
            return "$incidencesUrl/$incidenceId"
        }

        fun tasksByGroup(groupId: String?): String {
            return "$groupsUrl/$groupId/tasks"
        }

        fun tasksById(taskId: String?): String {
            return "$tasksUrl/$taskId"
        }
    }
}