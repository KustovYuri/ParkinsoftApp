package com.farma.parkinsoftapp.presentation.mappers

import com.farma.parkinsoftapp.data.network.models.CommentAnswerRequest
import com.farma.parkinsoftapp.data.network.models.DisplaySliderAnswerRequest
import com.farma.parkinsoftapp.data.network.models.GraphicAnswerRequest
import com.farma.parkinsoftapp.data.network.models.HumanPointsRequest
import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.network.models.SingleAnswerRequest
import com.farma.parkinsoftapp.data.network.models.SliderAnswerRequest
import com.farma.parkinsoftapp.data.network.models.YesNoAnswerRequest
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

fun List<TestQuestion>.convertToNativeTestRequest(testPreviewId: Long): NativeTestRequest {
    var nativeTestRequest = NativeTestRequest(testPreviewId, this.getSummaryPoints(),this.getMaxPoints())
    this.map {
        when (val test = it) {
            is TestQuestion.Comment -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    commentAnswers = nativeTestRequest.commentAnswers?.plus(convertedTest)
                        ?: listOf(convertedTest)
                )
            }

            is TestQuestion.DisplaySlider -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    displaySliderAnswers = nativeTestRequest.displaySliderAnswers?.plus(
                        convertedTest
                    )
                        ?: listOf(convertedTest)
                )
            }

            is TestQuestion.HumanPoint -> {
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
                    },
                    sliderAnswers = if (test.sliderIsEnabled) {
                        nativeTestRequest.sliderAnswers?.plus(
                            SliderAnswerRequest(
                                testId = test.testId,
                                questionId = 0,
                                sliderValue = test.sliderValue ?: 0,
                                score = test.sliderScore
                            )
                        ) ?: listOf(
                            SliderAnswerRequest(
                                testId = test.testId,
                                questionId = 0,
                                sliderValue = test.sliderValue ?: 0,
                                score = test.sliderScore
                            )
                        )
                    } else {
                        nativeTestRequest.sliderAnswers
                    }
                )
            }

            is TestQuestion.SingleAnswer -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    singleAnswers = nativeTestRequest.singleAnswers?.plus(
                        convertedTest
                    ) ?: listOf(convertedTest)
                )
            }

            is TestQuestion.Slider -> {
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

            is TestQuestion.YesNo -> {
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

            is TestQuestion.Graphic -> {
                val convertedTest = test.convertToAnswerRequest()
                nativeTestRequest = nativeTestRequest.copy(
                    graphicAnswers = nativeTestRequest.graphicAnswers?.plus(
                        convertedTest
                    ) ?: listOf(convertedTest)
                )
            }

            is TestQuestion.PreQuestion -> {}
        }
    }

    return nativeTestRequest
}

private fun TestQuestion.Comment.convertToAnswerRequest(): CommentAnswerRequest {
    return CommentAnswerRequest(
        testId = this.testId,
        comment = this.comment
    )
}

private fun TestQuestion.DisplaySlider.convertToAnswerRequest(): DisplaySliderAnswerRequest {
    return DisplaySliderAnswerRequest(
        testId = this.testId,
        sliderValue = this.sliderValue,
        score = this.score
    )
}

private fun TestQuestion.SingleAnswer.convertToAnswerRequest(): SingleAnswerRequest {
    return SingleAnswerRequest(
        testId = this.testId,
        selectedAnswer = this.selectedAnswer?.first ?: "",
        score = this.selectedAnswer?.second ?: 0,
    )
}

private fun TestQuestion.HumanPoint.convertToAnswerRequest(): HumanPointsRequest {
    return HumanPointsRequest(
        testId = this.testId,
        type = this.type,
        selectedPoints = this.selectedPoints,
        score = this.score,
    )
}

private fun TestQuestion.Graphic.convertToAnswerRequest(): GraphicAnswerRequest {
    return GraphicAnswerRequest(
        testId = this.testId,
        selectedVariant = selectedVariant?.question ?: "",
        score = selectedVariant?.score ?: 0,
    )
}

private fun TestQuestion.YesNo.convertToAnswerRequest(): List<YesNoAnswerRequest> {
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

private fun TestQuestion.Slider.convertToAnswerRequest(): List<SliderAnswerRequest> {
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

private fun List<TestQuestion>.getMaxPoints(): Int {
    var maxPoints = 0
    this.forEach {
        when(val test = it) {
            is TestQuestion.Comment -> {}
            is TestQuestion.DisplaySlider -> { maxPoints += test.maxScore }
            is TestQuestion.Graphic -> { maxPoints += test.maxScore }
            is TestQuestion.HumanPoint -> { maxPoints += test.maxScore }
            is TestQuestion.PreQuestion -> {}
            is TestQuestion.SingleAnswer -> { maxPoints += test.maxScore }
            is TestQuestion.Slider -> { maxPoints += test.maxScore }
            is TestQuestion.YesNo -> { maxPoints += test.maxScore }
        }
    }

    return maxPoints
}

private fun List<TestQuestion>.getSummaryPoints(): Int {
    var summaryPoints = 0
    this.forEach { it ->
        when(val test = it) {
            is TestQuestion.Comment -> {}
            is TestQuestion.DisplaySlider -> { summaryPoints += test.score }
            is TestQuestion.Graphic -> { summaryPoints += test.score }
            is TestQuestion.HumanPoint -> { summaryPoints += test.score }
            is TestQuestion.PreQuestion -> {}
            is TestQuestion.SingleAnswer -> { summaryPoints += test.selectedAnswer?.second ?: 0 }
            is TestQuestion.Slider -> { summaryPoints += test.maxScore }
            is TestQuestion.YesNo -> { summaryPoints += test.answers.sumOf { it.score } }
        }
    }

    return summaryPoints
}