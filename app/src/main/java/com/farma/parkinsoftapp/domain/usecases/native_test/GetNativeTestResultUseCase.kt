package com.farma.parkinsoftapp.domain.usecases.native_test

import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.raw_native_tests.getDN4TestData
import com.farma.parkinsoftapp.data.raw_native_tests.getPainDetectedTestData
import com.farma.parkinsoftapp.data.raw_native_tests.getSF36TestData
import com.farma.parkinsoftapp.data.raw_native_tests.getTestStimulationTestData
import com.farma.parkinsoftapp.domain.repositories.MainRepository
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.farma.parkinsoftapp.domain.models.Result
import com.farma.parkinsoftapp.domain.models.patient.TestType
import kotlinx.coroutines.flow.map

class GetNativeTestResultUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {
    suspend operator fun invoke(testPreviewId: Long, testType: TestType): Flow<Result<List<TestQuestion>>> {
        return mainRepository.getResultNativeTests(testPreviewId).map {
            when (it) {
                is Result.Error -> it
                is Result.Loading -> it
                is Result.Success -> Result.Success(it.result.convertToTestQuestion(testType))
            }
        }
    }
}

private fun NativeTestRequest.convertToTestQuestion(testType: TestType): List<TestQuestion> {
    val resultList: MutableList<TestQuestion> = mutableListOf()
    val rawTest = when (testType) {
        TestType.TEST_STIMULATION_DIARY -> getTestStimulationTestData()
        TestType.DN4 -> getDN4TestData()
        TestType.SF36 -> getSF36TestData()
        TestType.PAIN_DETECTED -> getPainDetectedTestData()
        else -> throw Exception("Invalid native test")
    }

    rawTest.forEach { rawTestQuestion ->
        val testId = rawTestQuestion.id

        when (rawTestQuestion) {
            is TestQuestion.Comment -> {
                this.commentAnswers?.find { it.testId == testId }?.let { comment ->
                    resultList.add(
                        rawTestQuestion.copy(
                            comment = comment.comment
                        )
                    )
                }
            }

            is TestQuestion.DisplaySlider -> {
                this.displaySliderAnswers?.find { it.testId == testId }?.let { slider ->
                    resultList.add(
                        rawTestQuestion.copy(
                            sliderValue = slider.sliderValue
                        )
                    )
                }
            }

            is TestQuestion.Graphic -> {
                this.graphicAnswers?.find { it.testId == testId }?.let { graphic ->
                    val graphicVariant =
                        rawTestQuestion.graphicVariant.find { it.question == graphic.selectedVariant }
                    resultList.add(
                        rawTestQuestion.copy(
                            selectedVariant = graphicVariant
                        )
                    )
                }
            }

            is TestQuestion.HumanPoint -> {
                val humanPoint = this.humanPoints?.find { it.testId == testId }
                val comment = this.commentAnswers?.find { it.testId == testId }
                val slider = this.sliderAnswers?.find { it.testId == testId }

                resultList.add(
                    rawTestQuestion.copy(
                        selectedPoints = humanPoint?.selectedPoints ?: emptyList(),
                        comment = comment?.comment,
                        sliderValue = slider?.sliderValue
                    )
                )
            }

            is TestQuestion.PreQuestion -> {}
            is TestQuestion.SingleAnswer -> {
                this.singleAnswers?.find { it.testId == testId }?.let { singleAnswer ->
                    resultList.add(
                        rawTestQuestion.copy(
                            selectedAnswer = singleAnswer.selectedAnswer to singleAnswer.score
                        )
                    )
                }
            }

            is TestQuestion.Slider -> {
                val requestSliderAnswers = this.sliderAnswers?.filter { it.testId == testId }
                val comment = this.commentAnswers?.find { it.testId == testId }

                val resultSliderAnswers = requestSliderAnswers?.mapNotNull { requestSlider ->
                    val currentSlider = rawTestQuestion.sliderAnswers.find {
                        it.questionId == requestSlider.questionId
                    }

                    currentSlider?.copy(
                        value = requestSlider.sliderValue
                    )
                } ?: rawTestQuestion.sliderAnswers

                resultList.add(
                    rawTestQuestion.copy(
                        sliderAnswers = resultSliderAnswers,
                        comment = comment?.comment,
                    )
                )
            }

            is TestQuestion.YesNo -> {
                val requestYesNoAnswers = this.yesNoAnswers?.filter { it.testId == testId }
                val comment = this.commentAnswers?.find { it.testId == testId }

                val resultSliderAnswers = requestYesNoAnswers?.mapNotNull { requestYesNo ->
                    val currentSlider = rawTestQuestion.answers.find {
                        it.questionId == requestYesNo.questionId
                    }

                    currentSlider?.copy(
                        answer = requestYesNo.answer
                    )
                } ?: rawTestQuestion.answers

                resultList.add(
                    rawTestQuestion.copy(
                        answers = resultSliderAnswers,
                        comment = comment?.comment ?: "",
                    )
                )
            }
        }
    }

    return resultList
}