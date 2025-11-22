package com.farma.parkinsoftapp.presentation.mappers

import com.farma.parkinsoftapp.data.network.models.CommentAnswerRequest
import com.farma.parkinsoftapp.data.network.models.DisplaySliderAnswerRequest
import com.farma.parkinsoftapp.data.network.models.HumanPointsRequest
import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.network.models.SingleAnswerRequest
import com.farma.parkinsoftapp.data.network.models.SliderAnswerRequest
import com.farma.parkinsoftapp.data.network.models.YesNoAnswerRequest
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.models.TestStimulationTestQuestion

fun List<TestStimulationTestQuestion>.convertToNativeTestRequest(testPreviewId: Long): NativeTestRequest {
    var nativeTestRequest = NativeTestRequest(testPreviewId)
    this.map {
        when (val test = it) {
            is TestStimulationTestQuestion.Comment -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    commentAnswers = nativeTestRequest.commentAnswers?.plus(convertedTest)
                        ?: listOf(convertedTest)
                )
            }

            is TestStimulationTestQuestion.DisplaySlider -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    displaySliderAnswers = nativeTestRequest.displaySliderAnswers?.plus(
                        convertedTest
                    )
                        ?: listOf(convertedTest)
                )
            }

            is TestStimulationTestQuestion.HumanPoint -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    humanPoints = nativeTestRequest.humanPoints?.plus(convertedTest)
                        ?: listOf(convertedTest),
                    commentAnswers = if (test.comment != null) {
                        nativeTestRequest.commentAnswers?.plus(
                            CommentAnswerRequest(
                                testId = test.testId,
                                comment = test.comment
                            )
                        ) ?: listOf(
                            CommentAnswerRequest(
                                testId = test.testId,
                                comment = test.comment
                            )
                        )
                    } else {
                        nativeTestRequest.commentAnswers
                    }
                )
            }

            is TestStimulationTestQuestion.SingleAnswer -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    singleAnswers = nativeTestRequest.singleAnswers?.plus(
                        convertedTest
                    ) ?: listOf(convertedTest)
                )
            }

            is TestStimulationTestQuestion.Slider -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    sliderAnswers = nativeTestRequest.sliderAnswers?.plus(
                        convertedTest
                    ) ?: convertedTest,
                    commentAnswers = if (test.comment != null) {
                        nativeTestRequest.commentAnswers?.plus(
                            CommentAnswerRequest(
                                testId = test.testId,
                                comment = test.comment
                            )
                        ) ?: listOf(
                            CommentAnswerRequest(
                                testId = test.testId,
                                comment = test.comment
                            )
                        )
                    } else {
                        nativeTestRequest.commentAnswers
                    }
                )
            }

            is TestStimulationTestQuestion.YesNo -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    yesNoAnswers = nativeTestRequest.yesNoAnswers?.plus(
                        convertedTest
                    ) ?: convertedTest,
                    commentAnswers = if (test.comment.isNotBlank()) {
                        nativeTestRequest.commentAnswers?.plus(
                            CommentAnswerRequest(
                                testId = test.testId,
                                comment = test.comment
                            )
                        ) ?: listOf(
                            CommentAnswerRequest(
                                testId = test.testId,
                                comment = test.comment
                            )
                        )
                    } else {
                        nativeTestRequest.commentAnswers
                    }
                )
            }

            is TestStimulationTestQuestion.PreQuestion -> {}
        }
    }

    return nativeTestRequest
}

private fun TestStimulationTestQuestion.Comment.convertToAnswerRequest(): CommentAnswerRequest {
    return CommentAnswerRequest(
        testId = this.testId,
        comment = this.comment
    )
}

private fun TestStimulationTestQuestion.DisplaySlider.convertToAnswerRequest(): DisplaySliderAnswerRequest {
    return DisplaySliderAnswerRequest(
        testId = this.testId,
        sliderValue = this.sliderValue,
        score = this.score
    )
}

private fun TestStimulationTestQuestion.SingleAnswer.convertToAnswerRequest(): SingleAnswerRequest {
    return SingleAnswerRequest(
        testId = this.testId,
        selectedAnswer = this.selectedAnswer?.first ?: "",
        score = this.selectedAnswer?.second ?: 0,
    )
}

private fun TestStimulationTestQuestion.HumanPoint.convertToAnswerRequest(): HumanPointsRequest {
    return HumanPointsRequest(
        testId = this.testId,
        type = this.type,
        selectedPoints = this.selectedPoints,
        score = this.score,
    )
}

private fun TestStimulationTestQuestion.YesNo.convertToAnswerRequest(): List<YesNoAnswerRequest> {
    return buildList {
        this@convertToAnswerRequest.answers.forEach {
            add(
                YesNoAnswerRequest(
                    testId = this@convertToAnswerRequest.testId,
                    questionId = it.questionId,
                    answer = it.answer ?: false,
                    score = it.score,
                )
            )
        }
    }
}

private fun TestStimulationTestQuestion.Slider.convertToAnswerRequest(): List<SliderAnswerRequest> {
    return buildList {
        this@convertToAnswerRequest.sliderAnswers.forEach {
            add(
                SliderAnswerRequest(
                    testId = this@convertToAnswerRequest.testId,
                    questionId = it.questionId,
                    sliderValue = it.value,
                    score = it.score,
                )
            )
        }
    }
}