package com.farma.parkinsoftapp.presentation.mappers

import com.farma.parkinsoftapp.data.network.models.CommentAnswerRequest
import com.farma.parkinsoftapp.data.network.models.DisplaySliderAnswerRequest
import com.farma.parkinsoftapp.data.network.models.GraphicAnswerRequest
import com.farma.parkinsoftapp.data.network.models.HumanPointsRequest
import com.farma.parkinsoftapp.data.network.models.NativeTestRequest
import com.farma.parkinsoftapp.data.network.models.SingleAnswerRequest
import com.farma.parkinsoftapp.data.network.models.SliderAnswerRequest
import com.farma.parkinsoftapp.data.network.models.YesNoAnswerRequest
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion

fun List<TestQuestion>.convertToNativeTestRequest(testPreviewId: Long, testType: TestType): NativeTestRequest {
    var nativeTestRequest = NativeTestRequest(
        testPreviewId,
        this.getSummaryPoints(),
        this.getMaxPoints()
    )

    if (testType == TestType.SF36) {
        nativeTestRequest = nativeTestRequest.copy(
            pf = this.convertToPf(),
            rp = this.convertToRp(),
            bp = this.convertToBp(),
            gh = this.convertToGh(),
            vt = this.convertToVt(),
            sf = this.convertToSf(),
            re = this.convertToRe(),
            mh = this.convertToMh(),
        )
    }

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

private fun List<TestQuestion>.convertToMh(): Float {
    val g9 = (this.find { it.id == 21L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val g9Score = when(g9) {
        1.0F -> 6.0f
        2.0F -> 5.4f
        3.0F -> 4.4f
        4.0F -> 3.0f
        5.0F -> 2.0f
        6.0F -> 1.0f
        else -> 0.0f
    }

    val z9 = (this.find { it.id == 25L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val z9Score = when(z9) {
        1.0F -> 6.0f
        2.0F -> 5.4f
        3.0F -> 4.4f
        4.0F -> 3.0f
        5.0F -> 2.0f
        6.0F -> 1.0f
        else -> 0.0f
    }

    val b9 = (this.find { it.id == 19L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val v9 = (this.find { it.id == 20L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val e9 = (this.find { it.id == 23L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F

    val Mhsum = b9 + v9 + g9Score + e9 + z9Score

    return ((Mhsum - 5) / 25) * 100
}

private fun List<TestQuestion>.convertToRe(): Float {
    val reSum = (this.find { it.id == 14L } as TestQuestion.YesNo).answers.sumOf {
        it.score
    }.toFloat()

    val rp = ((reSum - 3) / 3) * 100

    return rp
}

private fun List<TestQuestion>.convertToSf(): Float {
    val sf6 = (this.find { it.id == 15L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val sf6Score = when(sf6) {
        1.0F -> 5.0f
        2.0F -> 4.0f
        3.0F -> 3.0f
        4.0F -> 2.0f
        5.0F -> 1.0f
        else -> 0.0f
    }

    val sf10 = (this.find { it.id == 27L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F

    val sfSum = sf6Score + sf10

    return ((sfSum - 2) / 8) * 100
}

private fun List<TestQuestion>.convertToVt(): Float {
    val a9 = (this.find { it.id == 18L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val a9Score = when(a9) {
        1F -> 6f
        2F -> 5f
        3F -> 4f
        4F -> 3f
        5F -> 2f
        6F -> 1f
        else -> 0f
    }

    val d9 = (this.find { it.id == 22L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val d9Score = when(d9) {
        1F -> 6.0f
        2F -> 5.4f
        3F -> 4.4f
        4F -> 3.0f
        5F -> 2.0f
        6F -> 1.0f
        else -> 0.0f
    }

    val j9 = (this.find { it.id == 24L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val i9 = (this.find { it.id == 26L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F

    val VTsum = a9Score + d9Score + j9 + i9

    return ((VTsum - 4) / 20) * 100
}

private fun List<TestQuestion>.convertToGh(): Float {
    val gh1 = (this.find { it.id == 1L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val gh1Score = when(gh1) {
        1F -> 5.0f
        2F -> 4.4f
        3F -> 3.4f
        4F -> 2.0f
        5F -> 1.0f
        else -> 0.0f
    }

    val b11 = (this.find { it.id == 29L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val b11Score = when(b11) {
        1F -> 5f
        2F -> 4f
        3F -> 3f
        4F -> 2f
        5F -> 1f
        else -> 0f
    }

    val g11 = (this.find { it.id == 31L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val g11Score = when(g11) {
        1F -> 5f
        2F -> 4f
        3F -> 3f
        4F -> 2f
        5F -> 1f
        else -> 0f
    }

    val a11 = (this.find { it.id == 28L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F
    val v11 = (this.find { it.id == 30L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0.0F

    val GHsum = gh1Score + a11 + b11Score + v11 + g11Score

    return ((GHsum - 5) / 20) * 100
}

private fun List<TestQuestion>.convertToBp(): Float {
    val bp7Raw = (this.find { it.id == 16L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0f
    val bp8Raw = (this.find { it.id == 17L } as TestQuestion.SingleAnswer).selectedAnswer?.second?.toFloat() ?: 0f

    val scoreBp7 = when(bp7Raw) {
        1f -> 6.0f
        2f -> 5.4f
        3f -> 4.2f
        4f -> 3.1f
        5f -> 2.2f
        6f -> 1.0f
        else -> 0.0f
    }

    val scoreBp8 = when {
        bp8Raw == 1f && scoreBp7 == 1f -> 6f
        bp8Raw == 1f -> 5f
        bp8Raw == 2f -> 4f
        bp8Raw == 3f -> 3f
        bp8Raw == 4f -> 2f
        bp8Raw == 5f -> 1f
        else -> 0f
    }

    val bp = (((scoreBp7 + scoreBp8) - 2) / 10) * 100
    return bp
}

private fun List<TestQuestion>.convertToRp(): Float {
    val rpSum = (this.find { it.id == 13L } as TestQuestion.YesNo).answers.sumOf {
        it.score
    }.toFloat()

    val rp = ((rpSum - 4) / 4) * 100

    return rp
}

private fun List<TestQuestion>.convertToPf(): Float {
    val pfSum = (3L..12L).sumOf { idx ->
        val testQuestion = this.find { it.id == idx } as TestQuestion.SingleAnswer
        testQuestion.selectedAnswer?.second ?: 0
    }.toFloat()

    val pf = ((pfSum - 10) / 20) * 100

    return pf
}

fun convertToPsyAndPhiComponents(
    PF: Float,
    RP: Float,
    BP: Float,
    GH: Float,
    VT: Float,
    SF: Float,
    RE: Float,
    MH: Float,
): Pair<Float, Float> {
    val PF_Z = (PF - 84.52404) / 22.89490
    val RP_Z = (RP - 81.19907) / 33.797290
    val BP_Z = (BP - 75.49196) / 23.558790
    val GH_Z = (GH - 72.21316) / 20.16964
    val VT_Z = (VT - 61.05453) / 20.86942
    val SF_Z = (SF - 83.59753) / 22.37642
    val RE_Z = (RE - 81.29467) / 33.02717
    val MH_Z = (MH - 74.84212) / 18.01189

    val PHsum = (PF_Z * 0.42402) + (RP_Z* 0.35119) + (BP_Z* 0.31754)+ (SF_Z * -0.00753) + (MH_Z * -0.22069) + (RE_Z * -0.19206) + (VT_Z * 0.02877) + (GH_Z * 0.24954)
    val PH = (PHsum * 10) + 50

    val MHsum = (PF_Z* -0.22999) + (RP_Z * -0.12329) + (BP_Z * -0.09731) + (SF * 0.26876) + (MH_Z* 0.48581) + (RE_Z * 0.43407) + (VT_Z * 0.23534) + (GH_Z * -0.01571)
    val MH = (MHsum * 10) + 50

    return Pair(PH.toFloat(), MH.toFloat())
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