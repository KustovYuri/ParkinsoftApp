package com.farma.parkinsoftapp.data.local.data_store

enum class UserRoleValues(val value: String){
    DOCTOR("doctor"),
    PATIENT("patient"),
    UNAUTHORIZED("unauthorized");

    companion object {
        fun fromValue(value: String): UserRoleValues? {
            return entries.find { it.value == value }
        }
    }
}

