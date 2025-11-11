package com.farma.parkinsoftapp.domain.models.patient

enum class AllTestsTypes(val testName: String, val testType: String) {
    STATE_OF_HEALTH_DIARY("Дневник тестовой стимуляции", "test_stimulation_diary"),
    TEST_STIMULATION_DIARY("Дневник общего самочувствия", "state_of_health_diary"),
    HADS("HADS", "hads"),
    DN4("DN4", "dn4"),
    OSVESTRY("OSVESTRY", "osvestry"),
    SF36("SF-36", "sf36"),
    LANSS("LANSS", "lanss"),
    PAIN_DETECTED("PainDetect", "pain_detected");

    companion object {
        fun listDailyTests(): List<AllTestsTypes> {
            return listOf(
                STATE_OF_HEALTH_DIARY,
                TEST_STIMULATION_DIARY,
            )
        }

        fun listControlTests(): List<AllTestsTypes> {
            return listOf(
                HADS,
                DN4,
                OSVESTRY,
                SF36,
                LANSS,
                PAIN_DETECTED,
            )
        }
    }
}