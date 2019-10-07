package ruacon.api

import android.os.AsyncTask
import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONObject
import ruacon.entity.RuaConApp
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpGetRequest(private var listener: RuaConApi.ICallback<ArrayList<RuaConApp>>) : AsyncTask<String, Void, String>() {

    override fun doInBackground(vararg params: String): String? {
        try {
            val stringUrl = "http://ruacon.000webhostapp.com/ruacon/api/get_list_apps"
            val myUrl = URL(stringUrl)
            val connection = myUrl.openConnection() as HttpURLConnection

            connection.requestMethod = "GET"
            connection.readTimeout = 10000
            connection.connectTimeout = 10000
            connection.connect()

            val streamReader = InputStreamReader(connection.inputStream)
            val reader = BufferedReader(streamReader)
            val stringBuilder = StringBuilder()
            var inputLine = reader.readLine()

            while (inputLine != null) {
                stringBuilder.append(inputLine)
                inputLine = reader.readLine()
            }

            reader.close()
            streamReader.close()
            return stringBuilder.toString()
        } catch (e: IOException) {
            return null
        }
    }

    override fun onPostExecute(result: String?) {
        if (!TextUtils.isEmpty(result)) {
            val listApps = ArrayList<RuaConApp>()
            try {
                val jsonArray = JSONArray(result)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.get(i) as JSONObject
                    val name = jsonObject.getString("name")
                    val icon = jsonObject.getString("icon")
                    val link = jsonObject.getString("link")
                    listApps.add(RuaConApp(name, icon, link))
                }
                listener.onSuccess(listApps)
            } catch (e: Exception) {
                listener.onError()
            }
        } else {
            listener.onError()
        }
    }
}