package com.farma.parkinsoftapp.domain.usecases.native_test

import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestQuestion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import com.farma.parkinsoftapp.domain.models.Result

class GetNativeTestResultUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    operator fun invoke(testPreviewId: Long): Flow<Result<List<TestQuestion>>> = flow {
        mainRepository.getResultNativeTests(testPreviewId).collect {
            when(it) {
                is Result.Error -> {
                    emit(it)
                }
                is Result.Loading -> {
                    emit(it)
                }
                is Result.Success -> {
                    emit(Result.Success(it.result.convertToTestQuestion()))
                }
            }
        }
    }
}

fun NativeTestRequest.convertToTestQuestion(): List<TestQuestion> {
    val resultList: MutableList<TestQuestion> = mutableListOf()

    return emptyList()
}