package eu.pl.snk.senseibunny.websitedownloaddemo

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch{
            var x=CallAPILogin().callAPILogin()
            Log.d("x",x.toString())

            val jsobObject = JSONObject(x) //creating JSON object
            val text= jsobObject.optString("name")//getting text column
            Log.i("name",text.toString())

            val id = jsobObject.optInt("id") //getting id column
            Log.i("id",id.toString())

            val profileDetails = jsobObject.optJSONObject("profile_details") //getting profile_details column
            val profileStatus = profileDetails?.optString("status") //getting profile_status column
            Log.i("profile_status",profileStatus.toString())

            val parents = jsobObject.optJSONArray("parents")
            val firstParent = parents?.optString(0)
            Log.i("Size",parents?.length().toString()) //size of array
            Log.i("parents",firstParent.toString())

            val parents_details = jsobObject.optJSONArray("parents_details")
            val firstParent_details = parents_details?.optJSONObject(0)
            val age=firstParent_details?.optString("age")
            Log.i("age",age.toString())


            if (parents_details != null) {
                for(item in 0 until parents_details.length()){
                    val parent_details=parents_details.optJSONObject(item)
                    Log.i("parent_details",parent_details.toString())
                }
            }
        }
    }

    inner class CallAPILogin(){

        private lateinit var progressDialog: Dialog
        suspend fun callAPILogin() : String{

            runOnUiThread{
                showProgressDialog()
            }

            var result: String? = null

            withContext(Dispatchers.IO) {

                var connection: HttpURLConnection?= null

                try{
                    val url = URL("https://run.mocky.io/v3/73877daa-163f-4dc9-8bd5-bf6b7607dbe5") //paste your url here
                    connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true //do we get data?
                    connection.doOutput = true //do we post data?

                    val httpResult: Int = connection.responseCode //Status Code fe 200 OK

                    if(httpResult == HttpURLConnection.HTTP_OK){
                        val inputStream = connection.inputStream

                        val reader = BufferedReader(InputStreamReader(inputStream))

                        val stringBuilder = StringBuilder()

                        var line: String?

                        try{
                            while (reader.readLine().also { line = it }!= null) { //while there is a line
                                stringBuilder.append(line + "\n") //append the line to the string builder
                            }
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                        finally {
                            try{
                                inputStream.close()
                            }
                            catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        result = stringBuilder.toString()
                    }
                    else{
                        result = connection.responseMessage
                    }
                }
                catch (e: Exception) {
                    e.printStackTrace()
                }
                finally {
                    connection?.disconnect()
                }



            }

            return result!!

        }

        private fun showProgressDialog(){
            progressDialog = ProgressDialog(this@MainActivity)
            progressDialog.setContentView(R.layout.progress_dialog)
            progressDialog.show()
        }

        private fun hideProgressDialog(){
            progressDialog.dismiss()
        }
    }
}