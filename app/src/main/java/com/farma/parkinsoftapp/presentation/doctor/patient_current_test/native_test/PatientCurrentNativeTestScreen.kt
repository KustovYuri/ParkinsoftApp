package com.farma.parkinsoftapp.presentation.doctor.patient_current_test.native_test

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.farma.parkinsoftapp.R
import com.farma.parkinsoftapp.domain.models.patient.TestType
import com.farma.parkinsoftapp.presentation.common.ScreenState
import com.farma.parkinsoftapp.presentation.mappers.convertToPsyAndPhiComponents
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant.GraphicVariant
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.HumanPointVariant
import com.farma.parkinsoftapp.presentation.patient.test.pain_detected.test_variant.SingleAnswersVariant
import com.farma.parkinsoftapp.presentation.patient.test.models_common.TestQuestion
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.CommentVariant
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.DisplaySliderVariant
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.SliderVariant
import com.farma.parkinsoftapp.presentation.patient.test.test_stimulation.test_variants.YesNoVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientCurrentNativeTestScreen(
    viewModel: PatientCurrentNativeTestViewModel = hiltViewModel<PatientCurrentNativeTestViewModel>(),
    backNavigation: () -> Boolean,
    patientInitials: String,
    testDate: String,
    maxPoints: Int,
    summaryPoints: Int,
    pf: Float?,
    rp: Float?,
    bp: Float?,
    gh: Float?,
    vt: Float?,
    sf: Float?,
    re: Float?,
    mh: Float?,
) {
    val uiState by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopScreenBar(backNavigation = backNavigation, patientInitials) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is ScreenState.Error -> {
                    Text((uiState as ScreenState.Error).message)
                }

                is ScreenState.Loading -> {
                    CircularProgressIndicator(
                        color = Color(0xFF178399)
                    )
                }

                is ScreenState.Success -> {
                    Screen(
                        paddingValues,
                        testDate,
                        (uiState as ScreenState.Success).data,
                        viewModel.testType,
                        maxPoints,
                        summaryPoints,
                        pf,
                        rp,
                        bp,
                        gh,
                        vt,
                        sf,
                        re,
                        mh,
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun Screen(
    paddingValues: PaddingValues,
    testDate: String,
    testAnswers: List<TestQuestion>,
    testType: TestType,
    maxPoints: Int,
    summaryPoints: Int,
    pf: Float?,
    rp: Float?,
    bp: Float?,
    gh: Float?,
    vt: Float?,
    sf: Float?,
    re: Float?,
    mh: Float?,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF))
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f),
        ) {
            item {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = when (testType) {
                        TestType.TEST_STIMULATION_DIARY -> "Дневник тестовой стимуляции"
                        TestType.HADS1 -> "HADS 1"
                        TestType.HADS2 -> "HADS 2"
                        TestType.OSVESTRY -> "Освестри"
                        TestType.LANSS -> "LANSS"
                        TestType.DN4 -> "DN4"
                        TestType.SF36 -> "SF36"
                        TestType.PAIN_DETECTED -> "Pain Detected"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(16.dp))
                TestDate(testDate)
                Spacer(Modifier.height(16.dp))
                Text(
                    text = if (testType != TestType.SF36) {
                        "Оценка: $summaryPoints / $maxPoints"
                    } else {
                        val ph_mh = convertToPsyAndPhiComponents(
                            pf ?: 0F,
                            rp ?: 0F,
                            bp ?: 0F,
                            gh ?: 0F,
                            vt ?: 0F,
                            sf ?: 0F,
                            re ?: 0F,
                            mh ?: 0F,
                        )
                        "Оценка: ${String.format("%.2f", ph_mh.first)} | ${String.format("%.2f", ph_mh.second)}"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                if (testType != TestType.TEST_STIMULATION_DIARY) {
                    val ph_mh = convertToPsyAndPhiComponents(
                        pf ?: 0F,
                        rp ?: 0F,
                        bp ?: 0F,
                        gh ?: 0F,
                        vt ?: 0F,
                        sf ?: 0F,
                        re ?: 0F,
                        mh ?: 0F,
                    )

                    Spacer(Modifier.height(16.dp))
                    TextInterpretation(testType, maxPoints, summaryPoints, ph_mh)
                    Spacer(Modifier.height(16.dp))
                    ExpandableInfoCard(
                        testType,
                        pf,
                        rp,
                        bp,
                        gh,
                        vt,
                        sf,
                        re,
                        mh,
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
            items(testAnswers) { testAnswer ->
                when (testAnswer) {
                    is TestQuestion.Comment -> CommentVariant(testAnswer, {}, readOnly = true)
                    is TestQuestion.DisplaySlider -> DisplaySliderVariant(
                        testAnswer,
                        {},
                        readOnly = true
                    )

                    is TestQuestion.Graphic -> GraphicVariant(testAnswer, { }, readOnly = true)
                    is TestQuestion.HumanPoint -> HumanPointVariant(testAnswer, readOnly = true)
                    is TestQuestion.PreQuestion -> {}
                    is TestQuestion.SingleAnswer -> SingleAnswersVariant(
                        testAnswer,
                        selectAnswer = { },
                        readOnly = true
                    )

                    is TestQuestion.Slider -> SliderVariant(
                        testAnswer,
                        { a, b -> }, {},
                        readOnly = true
                    )

                    is TestQuestion.YesNo -> YesNoVariant(
                        testAnswer,
                        { a, b -> },
                        {},
                        isComment = testAnswer.comment.isNotBlank(),
                        readOnly = true
                    )
                }
                Spacer(Modifier.height(55.dp))
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TextInterpretation(
    testType: TestType,
    maxPoints: Int,
    summaryPoints: Int,
    ph_mh1: Pair<Float, Float>? = null
) {
    val text: String = when (testType) {
        TestType.TEST_STIMULATION_DIARY -> {
            ""
        }

        TestType.HADS1 -> {
            if (summaryPoints <= 7) {
                "Норма: отсутсвие достоверно выраженных симптомов тревоги"
            } else if (summaryPoints <= 10) {
                "Субклинически выраженная тревога"
            } else {
                "Клинически выраженная тревога"
            }
        }

        TestType.HADS2 -> {
            if (summaryPoints <= 7) {
                "Норма: отсутсвие достоверно выраженных симптомов депрессии"
            } else if (summaryPoints <= 10) {
                "Субклинически выраженная депрессия"
            } else {
                "Клинически выраженная депрессия"
            }
        }

        TestType.OSVESTRY -> {
            if (summaryPoints <= 20) {
                "Минимальное нарушение (пациент может осуществлять все виды жизнедеятельности)"
            } else if (summaryPoints <= 40) {
                "Умеренное нарушение (пациент испытывает значительные боли и трудности при сидении, поднимании предметов и стоянии)"
            } else if (summaryPoints <= 60) {
                "Тяжелое нарушение (у пациента боль становится основной проблемой, активность повседневной жизни у него затруднена)"
            } else if (summaryPoints <= 80) {
                "Крайне тяжелое (боль в спине ухудшает все аспекты жизни пациента"
            } else {
                "Такие пациенты либо прикованы к постели, либо агравируют свои симптомы"
            }
        }

        TestType.LANSS -> {
            if (summaryPoints < 12) {
                "Нейропатический механизм формирования болевых ощущений маловероятен"
            } else {
                "Нейропатический механизм формирования болевых ощущений вероятен"
            }
        }

        TestType.DN4 -> {
            if (summaryPoints >= 4) {
                "Боль является нейропатической или имеется нейропатический компонент боли"
            } else {
                "Нейропатического компонента боли не обнаруженно"
            }
        }

        TestType.SF36 -> {
            "${String.format("%.2f", ph_mh1?.first)} — оценка физического состояния.\n" +
            "${String.format("%.2f", ph_mh1?.second)} — оценка психологического состояния."
        }

        TestType.PAIN_DETECTED -> {
            if (summaryPoints <= 12) {
                "Наличие невропатического компонента боли маловероятно (< 15%)"
            } else if (summaryPoints <= 18) {
                "Результат неопределенный, однако, возможно наличие невропатического компонента боли"
            } else {
                "Высокая вероятность наличия невропатического компонента боли (> 90%)"
            }
        }
    }

    Text(
        text,
        color = Color(0xFF62767A),
        fontSize = 15.sp
    )
}

@SuppressLint("DefaultLocale")
@Composable
fun ExpandableInfoCard(
    testType: TestType,
    pf: Float? = null,
    rp: Float? = null,
    bp: Float? = null,
    gh: Float? = null,
    vt: Float? = null,
    sf: Float? = null,
    re: Float? = null,
    mh: Float? = null,
) {
    val title = "Как считается оценка?"
    var content: String

    when(testType) {
        TestType.TEST_STIMULATION_DIARY -> {
            content = ""
        }
        TestType.HADS1 -> {
            content = "HADS1\n\n" +
                "0 - 7 баллов - норма: отсутствие достоверно выраженных симптомов тревоги.\n\n" +
                    "8 - 10 баллов - субклинически выраженная тревога.\n\n" +
                    "11 баллов и выше клинически выраженная тревога."
        }
        TestType.HADS2 -> {
            content = "HADS2\n\n" +
                "0 - 7 баллов - норма: отсутствие достоверно выраженных симптомов депрессии.\n\n" +
                    "8 - 10 баллов - субклинически выраженная депрессия.\n\n" +
                    "11 баллов и выше клинически выраженная депрессия."
        }
        TestType.OSVESTRY -> {
            content = "OSVESTRY\n\n" +
                "0-20% - минимальное нарушение (пациент может осуществлять все виды жизнедеятельности);\n\n" +
                    "21-40% - умеренное нарушение (пациент испытывает значительные боли и трудности при сидении, поднимании предметов и стоянии);\n\n" +
                    "41-60% - тяжелое нарушение (у пациента боль становится основной проблемой, активность повседневной жизни у него затруднена);\n\n" +
                    "61-80% - крайне тяжелое (боль в спине ухудшает все аспекты жизни пациента;\n\n" +
                    "81-100% - такие пациенты либо прикованы к постели, либо агравируют свои симптомы."
        }
        TestType.LANSS -> {
            content = "LANSS\n\n" +
                "Если сумма < 12, нейропатический механизм формирования болевых ощущений маловероятен.\n\n" +
                    "Если сумма ≥ 12, нейропатический механизм формирования болевых ощущений вероятен."
        }
        TestType.DN4 -> {
            content = "DN4\n\n" +
                    "Если сумма составляет 4 и более баллов, это указывает на то, что боль у пациента является нейропатической, или имеется нейропатический компонент боли (при смешанных ноцицептивно- нейропатических болевых синдромах."
        }
        TestType.SF36 -> {
            val ph_mh = convertToPsyAndPhiComponents(
                pf ?: 0F,
                rp ?: 0F,
                bp ?: 0F,
                gh ?: 0F,
                vt ?: 0F,
                sf ?: 0F,
                re ?: 0F,
                mh ?: 0F,
            )

            content = "Физический компонент здоровья: ${String.format("%.2f", ph_mh.first)}\n\n" +
                    "Физическое функционирование (PF): ${String.format("%.2f", pf)}\n" +
                    "Ролевое функционирование (RP): ${String.format("%.2f", rp)}\n" +
                    "Интенсивность боли (ВР): ${String.format("%.2f", bp)}\n" +
                    "Общее состояние здоровья (GH): ${String.format("%.2f", gh)}\n\n" +
                    "Психологический компонент здоровья: ${String.format("%.2f", ph_mh.second)}\n\n" +
                    "Жизненная активность (VT): ${String.format("%.2f", vt)}\n" +
                    "Социальное функционирование (SF): ${String.format("%.2f", sf)}\n" +
                    "Ролевое функционирование (RE): ${String.format("%.2f", re)}\n" +
                    "Психическое здоровье (МН): ${String.format("%.2f", mh)}"
        }
        TestType.PAIN_DETECTED -> {
            content = """
PainDetect

0–12: Наличие невропатического компонента боли маловероятно (< 15%, отрицательный результат)

13–18: Возможно наличие невропатического компонента боли (неопределенный результат)

19–38: Высокая вероятность наличия невропатического компонента боли (> 90%, положительный результат)
""".trimIndent()
        }
    }

    if (testType != TestType.TEST_STIMULATION_DIARY) {
        ExpandableInfoCard(title, content)
    }
}

@Composable
fun ExpandableInfoCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrowRotation"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEDF1F2)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.question),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(R.drawable.keyboard_arrow_down),
                    contentDescription = null,
                    modifier = Modifier.rotate(rotation)
                )
            }

            // ─── Скрываемый контент ───
            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
private fun TestDate(testDate: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.clock),
            contentDescription = null,
            tint = Color(0xFF62767A)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = testDate,
            color = Color(0xFF62767A),
            fontSize = 15.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopScreenBar(backNavigation: () -> Boolean, patientInitials: String) {
    TopAppBar(
        title = {
            Text(
                text = patientInitials,
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium
            )
        },
        navigationIcon = {
            IconButton(onClick = { backNavigation() }) {
                Icon(
                    painterResource(R.drawable.arrow_left),
                    contentDescription = "Назад",
                    tint = Color(0xFF002A33)
                )
            }
        },
        actions = {
            IconButton(onClick = { /* TODO сортировка */ }) {
                Icon(
                    painter = painterResource(R.drawable.printer),
                    contentDescription = "Печать",
                    tint = Color(0xFF002A33)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFFFFFFF)
        )
    )
}