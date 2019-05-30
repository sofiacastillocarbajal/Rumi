package pe.edu.upc.rumi.model

import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

data class Task (
    var taskId: String?,
    var title: String?,
    var status: String?,
    var startAt: String?,
    var modifiedAt: String?,
    var endsAt: String?,
    var groupName: String?,
    var description: String?
) : Serializable {
    constructor() : this("", "", "", "", "", "", "", "")

    companion object {
        fun from(jsonArray: JSONArray): List<Task> {
            val tasks = ArrayList<Task>()
            for (i in 0..(jsonArray.length() - 1)) {
                tasks.add(from(jsonArray.getJSONObject(i)))
            }
            return tasks
        }

        fun from(jsonObject: JSONObject): Task {
            val task = Task()
            task.taskId = jsonObject.getString("taskId")
            task.title = jsonObject.getString("title")
            task.status = jsonObject.getString("status")
            task.startAt = jsonObject.getString("startAt")
            task.modifiedAt = jsonObject.getString("modifiedAt")
            task.endsAt = jsonObject.getString("endsAt")
            task.groupName = jsonObject.getString("groupName")

            val d = jsonObject.getString("description")
            if (d == "null") task.description = ""
            else task.description = d

            return task
        }
    }
}