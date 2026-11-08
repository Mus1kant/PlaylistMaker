package com.example.androidiscool.data

import com.example.androidiscool.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}