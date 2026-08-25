package com.cedacri.arete.data.remote.api

import com.cedacri.arete.data.remote.response.CreateReservationResponse
import com.cedacri.arete.domain.model.login.LoginRequest
import com.cedacri.arete.domain.model.login.LoginResponse
import com.cedacri.arete.data.remote.response.EmployeeResponse
import com.cedacri.arete.data.remote.response.GroupRequestResponse
import com.cedacri.arete.data.remote.response.OfficeReservationsResponse
import com.cedacri.arete.data.remote.response.OfficeResponse
import com.cedacri.arete.data.remote.response.ReservationsRangeResponse
import com.cedacri.arete.domain.model.reservation.ReservationRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("employees")
    suspend fun getEmployees(): List<EmployeeResponse>

    @GET("offices")
    suspend fun getOffices(): List<OfficeResponse>

    @GET("reservations")
    suspend fun getReservations(
        @Query("date") date: String
    ): List<OfficeReservationsResponse>

    @POST("reservations/byUser")
    suspend fun createReservation(
        @Body request: ReservationRequest
    ): Response<CreateReservationResponse>

    @GET("groups")
    suspend fun getGroups(): List<GroupRequestResponse>

    @GET("reservations/range")
    suspend fun getReservationsRange(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("employeeId") employeeId: String,
        @Query("status") status: String
    ): List<ReservationsRangeResponse>
}