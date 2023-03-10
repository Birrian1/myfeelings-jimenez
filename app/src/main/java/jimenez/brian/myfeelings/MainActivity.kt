package jimenez.brian.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import jimenez.brian.myfeelings.utilities.CustomBarDrawable
import jimenez.brian.myfeelings.utilities.CustomCircleDrawable
import jimenez.brian.myfeelings.utilities.Emociones
import jimenez.brian.myfeelings.utilities.JSONFile
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject



class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile? = null
    var veryHappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verySad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        jsonFile = JSONFile()
        fechingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            grap.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this, Emociones("Muy feliz", 0.0F, R.color.mustard, veryHappy))
            graphHappy.background = CustomBarDrawable(this, Emociones("Feliz", 0.0F, R.color.orange, veryHappy))
            graphNeutral.background = CustomBarDrawable(this, Emociones("Neutral", 0.0F, R.color.greenie, veryHappy))
            graphSad.background = CustomBarDrawable(this, Emociones("Triste", 0.0F, R.color.blue, veryHappy))
            graphVerySad.background = CustomBarDrawable(this, Emociones("Muy triste", 0.0F, R.color.deepBlue, veryHappy))

        }else{
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener {
            guardar()
        }

        veryHappyButton.setOnClickListener {
            veryHappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener {
            verySad++
            iconoMayoria()
            actualizarGrafica()
        }
    }

    fun fechingData(){
        try{
            var json: String = jsonFile?.getData(this)?: ""
            if(json != ""){
                this.data = true
                var jsonArray:JSONArray = JSONArray(json)
                this.lista = parseJson(jsonArray)

                for (i in lista){
                    when(i.nombre){
                        "Muy feliz" -> veryHappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verySad = i.total
                    }
                }
            }else{
                this.data = false
            }
        }catch (exception : JSONException){
            exception.printStackTrace()
        }
    }

    fun iconoMayoria(){
        if(happy>veryHappy && happy>neutral && happy>sad && happy>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }
        if(veryHappy>happy && veryHappy>neutral && veryHappy>sad && veryHappy>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }
        if(neutral>happy && neutral>veryHappy && neutral>sad && neutral>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }
        if(sad>happy && sad>neutral && sad>veryHappy && sad>verySad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }
        if(verySad>happy && verySad>neutral && verySad>sad && verySad>veryHappy){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
    }

    fun actualizarGrafica(){
        val total = veryHappy+happy+neutral+verySad+sad
        var pVH: Float = (veryHappy * 100 / total).toFloat()
        var pH: Float = (happy * 100 / total).toFloat()
        var pN: Float = (neutral * 100 / total).toFloat()
        var pS: Float = (sad * 100 / total).toFloat()
        var pVS: Float = (verySad * 100 / total).toFloat()

        Log.d("porcentajes", "very happy"  +pVH)
        Log.d("porcentajes", " happy "  +pH)
        Log.d("porcentajes", "neutral "  +pN)
        Log.d("porcentajes", "sad "  +pS)
        Log.d("porcentajes", "very sad"  +pVS)

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard, veryHappy))
        lista.add(Emociones("Feliz", pH, R.color.orange, happy))
        lista.add(Emociones("Neutral", pN, R.color.greenie, neutral))
        lista.add(Emociones("Triste", pS, R.color.blue, sad))
        lista.add(Emociones("Muy triste", pVS, R.color.deepBlue, verySad))

        val fondo = CustomCircleDrawable(this, lista)



        graphVeryHappy.background = CustomBarDrawable(this,Emociones("Muy feliz", pVH, R.color.mustard,veryHappy))
        graphHappy.background = CustomBarDrawable(this,Emociones("Feliz", pH, R.color.orange, happy))
        graphNeutral.background = CustomBarDrawable(this,Emociones("Neutral", pN, R.color.greenie, neutral))
        graphSad.background = CustomBarDrawable(this,Emociones("Triste", pS, R.color.blue, sad))
        graphVerySad.background = CustomBarDrawable(this,Emociones("Muy triste", pVS, R.color.deepBlue, verySad))


        grap.background = fondo
    }

    fun parseJson(jsonArray: JSONArray): ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()
        for (i in 0..jsonArray.length()){
            try{
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            }catch (exception: JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun guardar(){
        var jsonArray = JSONArray()
        var o: Int = 0
        for(i  in lista){
            Log.d("objetos", i.toString())
            var j: JSONObject = JSONObject()
            j.put("nombre", i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color", i.color)
            j.put("total", i.total)

            jsonArray.put(o, j)
            o++
        }
        jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}