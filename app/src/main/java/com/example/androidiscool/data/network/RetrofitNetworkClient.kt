package com.example.androidiscool.data.network

import com.example.androidiscool.data.NetworkClient
import com.example.androidiscool.data.dto.BaseResponse
import com.example.androidiscool.data.dto.TracksSearchResponse

class RetrofitNetworkClient : NetworkClient {

    override fun doRequest(dto: Any): BaseResponse {
        return TracksSearchResponse(emptyList())
    }
}