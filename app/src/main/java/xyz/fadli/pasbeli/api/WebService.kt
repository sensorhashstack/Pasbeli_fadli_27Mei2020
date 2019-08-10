package xyz.fadli.pasbeli.api

import androidx.databinding.ObservableField
import retrofit2.Call
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*
import xyz.fadli.pasbeli.model.*

/**
 * @author Mahendri
 */

interface WebService {

    @POST("harga")
    fun postHargaBarang(@Body daftarPasar: HargaKonsumen): Call<String>

    @POST("addpasar")
    fun sendAddPasarFadli(@Body daftarPasar: AddPasar): Call<String>

    //karena perlu id_pasar so memamai yang addpasarget dibandingin addpasar
    @POST("editpasar")
    fun sendEditPasar(@Body daftarPasar: AddPasarGet?): Call<String>

    @POST("updateHarga")
    fun updateHarga(@Body barangHarga: BarangHarga): Call<String>



    @DELETE("deletePasar/{id}")
    fun deletePasar(@Path("id") id:Int):Call<Int>

    @DELETE("deleteHarga/{idDataInt}")
    fun deleteHarga(@Path("idDataInt") idDataInt:Int):Call<Int>




    @GET("getPasarAdmin")
    fun getPasarAdmin() : Observable<List<AddPasarGet>>

    @GET("getallBarang")
    fun getallBarang() : Observable<List<BarangNamaDISTINCTGet>>

    @GET("getKualitasUnit/{nama}")
    fun getKualitasUnit(@Path("nama") nama: String): Observable<List<KualitasUnit>>

    @GET("getIDBarangOnfab/{nama}/{kualitas}/{satuan}")
    fun getIDBarangOnfab( @Path("nama") nama:String?,
                          @Path("kualitas") kualitas:String?,
                          @Path("satuan") satuan:String?): Observable<BarangGet>

//    @GET("getallBarang")
//    fun getallBarang() : Observable<List<BarangNamaDISTINCTGet>>

    @GET("user/{IDName}/{Password}")
    fun splashStatus(@Path("IDName") IDName:String?,
                     @Path("Password") Password:String?): Observable<User>

    @GET("getPasarBaru/{idPasar}")
    fun getPasarBaru(@Path("idPasar") idPasar:Int): Observable<AddPasarGet>

    //version 0 dan hanya admin
    @GET("getDataHargaView")
    fun getDataHargaView():Observable<ArrayList<BarangHarga>>

    //version 1 semua lihat
    @GET("getDataLayoutBaru")
    fun getDataLayoutBaru():Observable<ArrayList<BarangHarga>>

    //version 1 semua lihat dan terverifikasi
    @GET("getPasarHarga/{id_pasar}")
    fun getPasarHarga(@Path("id_pasar") id_pasar:Int):Observable<ArrayList<BarangHarga>>

    @GET("dapatcatatan/{id_user}")
    fun dapatcatatan(@Path("id_user") id_user:Int):Observable<List<BarangHarga>>
}
